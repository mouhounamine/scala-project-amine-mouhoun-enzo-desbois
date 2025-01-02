package repositories

import javax.inject._
import model.Country
import org.mongodb.scala._
import org.mongodb.scala.bson.collection.mutable.Document
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}
import repository.MongoConfig

@Singleton
class CountryRepository @Inject()(implicit ec: ExecutionContext) {

  private val countryCollection: MongoCollection[Document] =
    MongoConfig.database.getCollection("countries")

  def loadToMongo(countries: List[Country]): Future[Int] = {
    val isCollectionEmpty = Try {
      Await.result(countryCollection.countDocuments().toFuture(), 10.seconds) == 0
    }.getOrElse(false)

    if (!isCollectionEmpty) {
      println(
        "La collection 'countries' n'est pas vide. Aucun document n'a été inséré."
      )
      return Future.successful(0) // Aucun document inséré
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
      val insertFuture = countryCollection.insertMany(documents)
      Await.result(insertFuture.toFuture(), 10.seconds)
      println(s"${documents.size} pays ont été insérés dans MongoDB.")
    } match {
      case Success(_) =>
        Future.successful(documents.size) // Retourne le nombre de documents insérés
      case Failure(ex) =>
        println(s"Erreur lors de l'insertion des pays : ${ex.getMessage}")
        Future.successful(0) // En cas d'erreur, renvoie 0
    }
  }
}
