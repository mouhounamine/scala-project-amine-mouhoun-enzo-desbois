package services

import javax.inject._
import repositories.AirportRepository
import model.Airport
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._

@Singleton
class AirportService @Inject()(airportRepository: AirportRepository)(implicit ec: ExecutionContext) {

  def getAirportsByCountry(country: String): Future[List[Airport]] = {
    airportRepository.getAirportsByCountry(country)
  }
}
