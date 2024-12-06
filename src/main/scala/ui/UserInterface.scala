package userinterface

import model.{Airport, Country, Runway}
import parser.{CSVParser, Cleaner}

object UserInterface {

  def start(): Unit = {
    val (countries, airports, runways) = Cleaner.clean()


    println("Welcome to the Airport & Runway Information System!")
    var continue = true

    while (continue) {
      println("\nPlease choose an option:")
      println("1. Query")
      println("2. Reports")
      println("3. Exit")

      val choice = scala.io.StdIn.readLine().trim
      choice match {
        case "1" => queryOption(countries, airports, runways)
        case "2" => reportsOption(countries, airports, runways)
        case "3" =>
          println("Goodbye!")
          continue = false
        case _ => println("Invalid choice. Please enter 1, 2, or 3.")
      }
    }
  }

  /** Query Option: Display airports and runways for a given country name or
    * code.
    */
  private def queryOption(
      countries: List[Country],
      airports: List[Airport],
      runways: List[Runway]
  ): Unit = {
    println("\nEnter a country name or code:")
    val input = scala.io.StdIn.readLine().trim.toLowerCase

    val countryOpt = countries.find(c =>
      c.code.toLowerCase == input || c.name.toLowerCase == input
    )
    countryOpt match {
      case Some(country) =>
        val countryAirports = airports.filter(_.iso_country == country.code)
        println(s"\nAirports in ${country.name}:")
        if (countryAirports.isEmpty) {
          println("No airports found.")
        } else {
          countryAirports.foreach { airport =>
            println(s"- ${airport.name} (ID: ${airport.id})")
            val airportRunways = runways.filter(_.airport_ref == airport.id)
            if (airportRunways.isEmpty) {
              println("  No runways found for this airport.")
            } else {
              println("  Runways:")
              airportRunways.foreach(r =>
                println(s"    - ${r.surface.getOrElse("Unknown surface")}")
              )
            }
          }
        }
      case None =>
        println("Country not found. Please try again.")
    }
  }
  


  /** Reports Option: Generate reports based on airports and runways data. */
  private def reportsOption(
      countries: List[Country],
      airports: List[Airport],
      runways: List[Runway]
  ): Unit = {
    println("\nReports Menu:")
    println("1. Top 10 countries with highest and lowest number of airports")
    println("2. Runway types per country")
    println("3. Top 10 most common runway latitude identifiers")
    println("Enter your choice:")

    val reportChoice = scala.io.StdIn.readLine().trim
    reportChoice match {
      case "1" => reportAirportsByCountry(countries, airports)
      case "2" => reportRunwayTypesByCountry(countries, airports, runways)
      case "3" => reportCommonRunwayIdentifiers(runways)
      case _   => println("Invalid choice. Please enter 1, 2, or 3.")
    }
  }

  /** Report: Top 10 countries with highest and lowest number of airports. */
  private def reportAirportsByCountry(
      countries: List[Country],
      airports: List[Airport]
  ): Unit = {
    val airportCounts = countries.map { country =>
      country.name -> airports.count(_.iso_country == country.code)
    }

    val sortedCounts = airportCounts.sortBy(-_._2)
    println("\nTop 10 countries with highest number of airports:")
    sortedCounts.take(10).foreach { case (country, count) =>
      println(s"- $country: $count airports")
    }

    println("\nTop 10 countries with lowest number of airports:")
    sortedCounts.reverse.take(10).foreach { case (country, count) =>
      println(s"- $country: $count airports")
    }
  }

  /** Report: Runway types per country. */
  private def reportRunwayTypesByCountry(
      countries: List[Country],
      airports: List[Airport],
      runways: List[Runway]
  ): Unit = {
    println("\nRunway types per country:")
    countries.foreach { country =>
      val countryAirports = airports.filter(_.iso_country == country.code)
      val countryRunways = countryAirports.flatMap(airport =>
        runways.filter(_.airport_ref == airport.id)
      )
      val runwayTypes = countryRunways.flatMap(_.surface).distinct

      println(s"- ${country.name}: ${runwayTypes.mkString(", ")}")
    }
  }

  /** Report: Top 10 most common runway latitude identifiers. */
  private def reportCommonRunwayIdentifiers(runways: List[Runway]): Unit = {
    val runwayCounts =
      runways.flatMap(_.le_ident).groupBy(identity).mapValues(_.size)
    val sortedRunways = runwayCounts.toList.sortBy(-_._2)

    println("\nTop 10 most common runway latitude identifiers:")
    sortedRunways.take(10).foreach { case (identifier, count) =>
      println(s"- $identifier: $count occurrences")
    }
  }
}
