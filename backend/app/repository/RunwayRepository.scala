package repositories

import javax.inject._
import model.Runway
import org.mongodb.scala._
import org.mongodb.scala.bson.collection.mutable.Document
import org.mongodb.scala.model.Aggregates._
import org.mongodb.scala.model.Accumulators._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Sorts._
import repository.MongoConfig
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}
import scala.jdk.CollectionConverters._

@Singleton
class RunwayRepository @Inject()(implicit ec: ExecutionContext) {

  private val runwaysCollection: MongoCollection[Document] =
    MongoConfig.database.getCollection("runways")
  private val airportsCollection: MongoCollection[Document] =
    MongoConfig.database.getCollection("airports")

  def getRunwayTypesByCountry: Future[Map[String, Set[String]]] = {
    val runwaysFuture  = runwaysCollection.find().toFuture()
    val airportsFuture = airportsCollection.find().toFuture()

    // On "zip" les deux Futures pour les récupérer simultanément.
    runwaysFuture.zip(airportsFuture).map { case (runways, airports) =>

      val airportToCountry: Map[Int, String] =
        airports.flatMap { doc =>
          val bsonDoc = doc.toBsonDocument

          Option(bsonDoc.getInt32("id")).map(_.getValue).flatMap { id =>
            Option(bsonDoc.getString("iso_country")).map(_.getValue).map { country =>
              id -> country
            }
          }
        }.toMap

      // Construction de la liste (country -> surface) à partir des runways
      val countryToSurfacePairs: Seq[(String, String)] =
        runways.flatMap { doc =>
          val bsonDoc = doc.toBsonDocument

          Option(bsonDoc.getInt32("airport_ref")).map(_.getValue).flatMap { airportRef =>
            Option(bsonDoc.getString("surface")).map(_.getValue).flatMap { surface =>
              airportToCountry.get(airportRef).map { country =>
                (country, surface)
              }
            }
          }
        }

      // Regroupement par clé (country), puis création d’un Set de surfaces
      countryToSurfacePairs
        .groupBy(_._1)
        .view
        .mapValues(_.map(_._2).toSet)
        .toMap
    }
  }


  def loadToMongo(runways: List[Runway]): Future[Int] = {
    if (runways.isEmpty) {
      println("La liste des pistes est vide. Aucun document à insérer.")
      Future.successful(0)
    }

    val isCollectionEmpty = Try {
      Await.result(runwaysCollection.countDocuments().toFuture(), 40.seconds) == 0
    }.getOrElse(false)

    if (!isCollectionEmpty) {
      println("La collection 'runways' n'est pas vide. Aucun document n'a été inséré.")
      Future.successful(0) 
    }

    val documents = runways.map { runway =>
      Document(
        "id" -> runway.id,
        "airport_ref" -> runway.airport_ref,
        "airport_ident" -> runway.airport_ident,
        "length_ft" -> runway.length_ft.getOrElse(0),
        "width_ft" -> runway.width_ft.getOrElse(0),
        "surface" -> runway.surface.getOrElse(""),
        "lighted" -> runway.lighted,
        "closed" -> runway.closed,
        "le_ident" -> runway.le_ident.getOrElse(""),
        "le_latitude_deg" -> runway.le_latitude_deg.getOrElse(0.0),
        "le_longitude_deg" -> runway.le_longitude_deg.getOrElse(0.0),
        "le_elevation_ft" -> runway.le_elevation_ft.getOrElse(0),
        "le_heading_degT" -> runway.le_heading_degT.getOrElse(0.0),
        "le_displaced_threshold_ft" -> runway.le_displaced_threshold_ft.getOrElse(0),
        "he_ident" -> runway.he_ident.getOrElse(""),
        "he_latitude_deg" -> runway.he_latitude_deg.getOrElse(0.0),
        "he_longitude_deg" -> runway.he_longitude_deg.getOrElse(0.0),
        "he_elevation_ft" -> runway.he_elevation_ft.getOrElse(0),
        "he_heading_degT" -> runway.he_heading_degT.getOrElse(0.0),
        "he_displaced_threshold_ft" -> runway.he_displaced_threshold_ft.getOrElse(0)
      )
    }

    if (documents.isEmpty) {
      println("Aucun document valide n'a été généré à partir de la liste des pistes.")
      Future.successful(0)
    }

    Try {
      val insertFuture = runwaysCollection.insertMany(documents)
      Await.result(insertFuture.toFuture(), 40.seconds)
      println(s"${documents.size} pistes ont été insérées dans MongoDB.")
    } match {
      case Success(_) =>
        Future.successful(documents.size) 
      case Failure(ex) =>
        println(s"Erreur lors de l'insertion des pistes : ${ex.getMessage}")
        Future.successful(0) 
    }
  }

  def getTopRunwayLatitudes(): Future[List[(String, Int)]] = {
    runwaysCollection.find().toFuture().map { documents =>
      documents.flatMap { doc =>
        val bsonDoc = doc.toBsonDocument
        Option(bsonDoc.getString("le_ident")).map(_.getValue) 
      }
      .groupBy(identity) 
      .view.mapValues(_.size) 
      .toList.sortBy(-_._2) 
      .take(10) 
    }
  }
}
