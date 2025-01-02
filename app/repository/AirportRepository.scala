package repositories

import javax.inject._
import model.Airport
import org.mongodb.scala._
import org.mongodb.scala.model.Filters
import play.api.libs.json._
import scala.concurrent.{ExecutionContext, Future}
import repository.MongoConfig

@Singleton
class AirportRepository @Inject()(implicit ec: ExecutionContext) {

  private val db = MongoConfig.database

  private val airportsCollection: MongoCollection[Document] = db.getCollection("airports")

  implicit val airportFormat: OFormat[Airport] = Json.format[Airport]

  def getAirportsByCountry(country: String): Future[List[Airport]] = {
    val query = Filters.equal("iso_country", country)

    airportsCollection.find(query).toFuture().map { documents =>
      documents.collect {
        case doc if Json.parse(doc.toJson()).asOpt[Airport].isDefined =>
          Json.parse(doc.toJson()).as[Airport] 
      }.toList 
    }
  }

  def getAllAirports(): Future[List[Airport]] = {
    airportsCollection.find().toFuture().map { documents =>
      documents.collect {
        case doc if Json.parse(doc.toJson()).asOpt[Airport].isDefined =>
          Json.parse(doc.toJson()).as[Airport] // Désérialiser en Airport
      }.toList
    }
  }
}
