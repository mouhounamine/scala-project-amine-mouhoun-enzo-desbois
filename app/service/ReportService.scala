package services

import javax.inject._
import repositories.{AirportRepository, RunwayRepository}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ReportService @Inject()(
    airportRepository: AirportRepository,
    runwayRepository: RunwayRepository
)(implicit ec: ExecutionContext) {

  // Méthode existante pour les pays avec le plus grand nombre d'aéroports
  def getTopCountries: Future[List[(String, Int)]] = {
    airportRepository.getAllAirports().map { airports =>
      // Compter le nombre d'aéroports par pays
      val countryCounts = airports.groupBy(_.iso_country).map {
        case (country, airports) => (country, airports.size)
      }

      // Trier par nombre d'aéroports et prendre les 10 premiers
      countryCounts.toList.sortBy(-_._2).take(10)
    }
  }

  // Nouvelle méthode pour obtenir les types de pistes par pays
  def getRunwayTypesByCountry: Future[Map[String, Set[String]]] = {
    runwayRepository.getRunwayTypesByCountry
  }
}
