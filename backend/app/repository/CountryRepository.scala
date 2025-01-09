package repositories

import javax.inject._
import model.Country
import org.mongodb.scala._
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Filters.regex
import org.mongodb.scala.bson.collection.mutable.Document
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}
import repository.MongoConfig
import play.api.libs.json.Json

@Singleton
class CountryRepository @Inject()(implicit ec: ExecutionContext) {

  private val db = MongoConfig.database

  private val countriesCollection: MongoCollection[Document] = db.getCollection("countries")

  implicit val countryFormat = Json.format[Country]

  def getCodeByNameFuzzy(countryName: String): Future[Option[String]] = {
    val exactQuery   = Filters.regex("name", s"(?i)^${countryName}$$")  // Recherche exacte
    val partialQuery = Filters.regex("name", s"(?i).*${countryName}.*") // Sous-chaîne (évite conflit, ex. "United States" vs "United States Minor...")

    countriesCollection
      .find(exactQuery)
      .toFuture()
      .flatMap { exactDocs =>
        // On regarde s’il y a au moins un document correspondant à la recherche exacte
        exactDocs.headOption match {
          case Some(doc) =>
            val code = Json.parse(doc.toJson()).as[Country].code
            Future.successful(Some(code))

          // Sinon, on tente la recherche partielle
          case None =>
            countriesCollection
              .find(partialQuery)
              .toFuture()
              .map { partialDocs =>
                partialDocs.headOption.map { doc =>
                  Json.parse(doc.toJson()).as[Country].code
                }
              }
        }
      }
  }



  def loadToMongo(countries: List[Country]): Future[Int] = {
    val isCollectionEmpty = Try {
      Await.result(countriesCollection.countDocuments().toFuture(), 10.seconds) == 0
    }.getOrElse(false)

    if (!isCollectionEmpty) {
      println(
        "La collection 'countries' n'est pas vide. Aucun document n'a été inséré."
      )
      Future.successful(0) 
    }

    val documents = countries.map { country =>
      Document(
        "id" -> country.id,
        "code" -> country.code,
        "name" -> country.name,
        "continent" -> country.continent,
        "wikipedia_link" -> country.wikipedia_link.getOrElse(""),
        "keywords" -> country.keywords.getOrElse(List())
      )
    }

    Try {
      val insertFuture = countriesCollection.insertMany(documents)
      Await.result(insertFuture.toFuture(), 10.seconds)
      println(s"${documents.size} pays ont été insérés dans MongoDB.")
    } match {
      case Success(_) =>
        Future.successful(documents.size) 
      case Failure(ex) =>
        println(s"Erreur lors de l'insertion des pays : ${ex.getMessage}")
        Future.successful(0) 
    }
  }
}
