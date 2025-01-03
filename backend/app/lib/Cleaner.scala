package lib

import model.{Airport, Country, Runway}
import lib.CSVParser

object Cleaner {

  def cleanCountries(): List[Country] = {
    Country.clean(CSVParser.parseFile("public/countries.csv", Country.fromCSV))
  }

  def cleanAirports(): List[Airport] = {
    Airport.cleanAll(CSVParser.parseFile("public/airports.csv", Airport.fromCSV))
  }

  def cleanRunways(): List[Runway] = {
    Runway.cleanAll(CSVParser.parseFile("public/runways.csv", Runway.fromCSV))
  }
}
