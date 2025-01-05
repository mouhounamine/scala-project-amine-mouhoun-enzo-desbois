package repositories

import javax.inject._
import model.Airport
import org.mongodb.scala._
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.ReplaceOptions
import play.api.libs.json._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import repository.MongoConfig

@Singleton
class AirportRepository @Inject()(countryRepository: CountryRepository)(implicit ec: ExecutionContext) {

  private val db = MongoConfig.database

  private val airportsCollection: MongoCollection[Document] = db.getCollection("airports")

  implicit val airportFormat: OFormat[Airport] = Json.format[Airport]

  def getAirportsByCountry(isoCode: String): Future[List[Airport]] = {
      val query = Filters.equal("iso_country", isoCode)
      airportsCollection
        .find(query)
        .toFuture()
        .map { documents =>
          documents.collect {
            case doc if Json.parse(doc.toJson()).asOpt[Airport].isDefined =>
              Json.parse(doc.toJson()).as[Airport]
          }.toList
        }
  }

    // Nouvelle méthode qui gère code ou nom :
  def getAirportsByCountryOrName(countryOrCode: String): Future[List[Airport]] = {
    // 1) Tenter la recherche comme si c'était le code ISO
    getAirportsByCountry(countryOrCode).flatMap { airports =>
      if (airports.nonEmpty) {
        // on a trouvé en supposant que countryOrCode est un code
        Future.successful(airports)
      } else {
        // 2) Sinon, on essaie de trouver un code depuis la table "countries"
        countryRepository.getCodeByName(countryOrCode).flatMap {
          case Some(code) =>
            // on relance la recherche avec iso_country = code
            getAirportsByCountry(code)
          case None =>
            // rien trouvé => liste vide
            Future.successful(Nil)
        }
      }
    }
  }

  def getAirportCountsByCountry(): Future[Map[String, Int]] = {
    airportsCollection.find().toFuture().map { documents =>
      documents
        .flatMap { doc =>
          val json = Json.parse(doc.toJson())
          (json \ "iso_country").asOpt[String]
        }
        .groupBy(identity) 
        .view.mapValues(_.size)
        .toMap
    }
  }

  def getAllAirports(): Future[List[Airport]] = {
    airportsCollection.find().toFuture().map { documents =>
      documents.collect {
        case doc if Json.parse(doc.toJson()).asOpt[Airport].isDefined =>
          Json.parse(doc.toJson()).as[Airport] 
      }.toList
    }
  }

  def loadToMongo(airports: List[Airport]): Future[Int] = {
    if (airports.isEmpty) {
      println("La liste des aéroports est vide. Aucun document à insérer.")
      return Future.successful(0)
    }

    val isCollectionEmpty = Try {
      Await.result(airportsCollection.countDocuments().toFuture(), 40.seconds) == 0
    }.getOrElse(false)

    if (!isCollectionEmpty) {
      println("La collection 'airports' n'est pas vide. Aucun document n'a été inséré.")
      return Future.successful(0) 
    }

    val documents = airports.map { airport =>
      Document(
        "id" -> airport.id,
        "ident" -> airport.ident,
        "type" -> airport.`type`,
        "name" -> airport.name,
        "latitude_deg" -> airport.latitude_deg,
        "longitude_deg" -> airport.longitude_deg,
        "elevation_ft" -> airport.elevation_ft.getOrElse(0),
        "continent" -> airport.continent,
        "iso_country" -> airport.iso_country,
        "iso_region" -> airport.iso_region,
        "municipality" -> airport.municipality.getOrElse(""),
        "scheduled_service" -> airport.scheduled_service,
        "gps_code" -> airport.gps_code.getOrElse(""),
        "iata_code" -> airport.iata_code.getOrElse(""),
        "local_code" -> airport.local_code.getOrElse(""),
        "home_link" -> airport.home_link.getOrElse(""),
        "wikipedia_link" -> airport.wikipedia_link.getOrElse(""),
        "keywords" -> airport.keywords.getOrElse("")
      )
    }

    if (documents.isEmpty) {
      println("Aucun document valide n'a été généré à partir de la liste des aéroports.")
      return Future.successful(0)
    }

    Try {
      val insertFuture = airportsCollection.insertMany(documents)
      Await.result(insertFuture.toFuture(), 40.seconds)
      println(s"${documents.size} aéroports ont été insérés dans MongoDB.")
    } match {
      case Success(_) =>
        Future.successful(documents.size) 
      case Failure(ex) =>
        println(s"Erreur lors de l'insertion des aéroports : ${ex.getMessage}")
        Future.successful(0) 
    }
  }
}
