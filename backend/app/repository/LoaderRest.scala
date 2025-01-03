package rest

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import lib.Cleaner
import repositories.{CountryRepository, RunwayRepository, AirportRepository}

@Singleton
class LoaderRest @Inject()(
    countryRepository: CountryRepository,
    runwayRepository: RunwayRepository,
    airportRepository: AirportRepository
)(implicit ec: ExecutionContext) {

  def loadCountries(): Future[Int] = {
    val countries = Cleaner.cleanCountries()
    countryRepository.loadToMongo(countries)
  }

  def loadRunways(): Future[Int] = {
    val runways = Cleaner.cleanRunways()
    runwayRepository.loadToMongo(runways)
  }

  def loadAirports(): Future[Int] = {
    val airports = Cleaner.cleanAirports()
    airportRepository.loadToMongo(airports)
  }
}
