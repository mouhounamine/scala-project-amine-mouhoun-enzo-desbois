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

  // Méthode existante : récupération des types de pistes par pays
  def getRunwayTypesByCountry: Future[Map[String, Set[String]]] = {
    runwaysCollection.aggregate(
      Seq(
        group("$iso_country", addToSet("runwayTypes", "$surface")),
        project(fields(computed("country", "$_id"), include("runwayTypes"), excludeId()))
      )
    ).toFuture()
      .map { documents =>
        documents.map { doc =>
          // Convertir le document en BsonDocument pour un accès typé
          val bsonDoc = doc.toBsonDocument
          val country = bsonDoc.getString("country").getValue
          val runwayTypes = bsonDoc.getArray("runwayTypes").getValues.asScala
            .map(_.asString().getValue).toSet

          country -> runwayTypes
        }.toMap
      }
  }


  // Nouvelle méthode : insertion de données dans MongoDB
  def loadToMongo(runways: List[Runway]): Future[Int] = {
    if (runways.isEmpty) {
      println("La liste des pistes est vide. Aucun document à insérer.")
      return Future.successful(0)
    }

    val isCollectionEmpty = Try {
      Await.result(runwaysCollection.countDocuments().toFuture(), 40.seconds) == 0
    }.getOrElse(false)

    if (!isCollectionEmpty) {
      println("La collection 'runways' n'est pas vide. Aucun document n'a été inséré.")
      return Future.successful(0) // Aucun document inséré
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
      return Future.successful(0)
    }

    Try {
      val insertFuture = runwaysCollection.insertMany(documents)
      Await.result(insertFuture.toFuture(), 40.seconds)
      println(s"${documents.size} pistes ont été insérées dans MongoDB.")
    } match {
      case Success(_) =>
        Future.successful(documents.size) // Retourne le nombre de documents insérés
      case Failure(ex) =>
        println(s"Erreur lors de l'insertion des pistes : ${ex.getMessage}")
        Future.successful(0) // En cas d'erreur, renvoie 0
    }
  }

}
