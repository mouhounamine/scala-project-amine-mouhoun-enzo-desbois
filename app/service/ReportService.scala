package services

import javax.inject._
import repositories.{AirportRepository, RunwayRepository}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ReportService @Inject()(
    airportRepository: AirportRepository,
    runwayRepository: RunwayRepository
)(implicit ec: ExecutionContext) {

  def getTopCountries(): Future[List[(String, Int)]] = {
    airportRepository.getAirportCountsByCountry().map { countryCounts =>
      countryCounts.toList.sortBy(-_._2).take(10) 
    }
  }

  def getBottomCountries(): Future[List[(String, Int)]] = {
    airportRepository.getAirportCountsByCountry().map { countryCounts =>
      countryCounts.toList.sortBy(_._2).take(10) 
    }
  }

  def getRunwayTypesByCountry: Future[Map[String, Set[String]]] = {
    runwayRepository.getRunwayTypesByCountry
  }
  
  def getTopRunwayLatitudes(): Future[List[(String, Int)]] = {
    runwayRepository.getTopRunwayLatitudes()
  }
}
