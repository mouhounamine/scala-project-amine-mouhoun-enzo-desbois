package parser

import model.{Airport, Country, Runway}
import parser.CSVParser

object Cleaner {
  def clean(): (List[Country], List[Airport], List[Runway]) = {
    val countries =
      Country.clean(
        CSVParser.parseFile("src/main/resources/countries.csv", Country.fromCSV)
      )

    val airports =
      Airport.cleanAll(
        CSVParser.parseFile("src/main/resources/airports.csv", Airport.fromCSV)
      )

    val runways =
      Runway.cleanAll(
        CSVParser.parseFile("src/main/resources/runways.csv", Runway.fromCSV)
      )

    (countries, airports, runways)
  }
}
