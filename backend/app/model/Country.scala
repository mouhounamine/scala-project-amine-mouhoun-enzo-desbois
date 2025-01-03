package model

import repository.MongoConfig
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.collection.mutable.Document
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try
import scala.util.Success
import scala.util.Failure

case class Country(
    id: Int,
    code: String,
    name: String,
    continent: String,
    wikipedia_link: Option[String],
    keywords: Option[List[String]]
)

object Country {
  def fromCSV(line: String): Option[Country] = {
    val transformedLine = line
      .replace("\",\"", "|")
      .replace(",\"", "|")
      .replace("\",", "|")
      .replace("\"", "|")

    val fields = transformedLine.split("\\|", -1)

    fields.length match {
      case 6 =>
        Some(
          Country(
            id = fields(0).toIntOption.getOrElse(-1),
            code = fields(1),
            name = fields(2),
            continent = fields(3),
            wikipedia_link = if (fields(4).isEmpty) None else Some(fields(4)),
            keywords = None
          )
        )
      case 7 =>
        val keywordsList = fields(5)
          .split(",")
          .toList

        Some(
          Country(
            id = fields(0).toIntOption.getOrElse(-1),
            code = fields(1),
            name = fields(2),
            continent = fields(3),
            wikipedia_link = if (fields(4).isEmpty) None else Some(fields(4)),
            keywords =
              if (keywordsList.isEmpty) None
              else Some(keywordsList)
          )
        )
      case _ =>
        None
    }
  }

  def clean(countries: List[Country]): List[Country] = {
    countries.distinct
      .filter(_.id >= 0)
      .map(country =>
        country.copy(
          code = country.code.toUpperCase().trim().replace("\"", ""),
          name = country.name
            .toUpperCase()
            .trim()
            .replace("\"", ""),
          continent = country.continent
            .toUpperCase()
            .trim()
            .replace("\"", "")
            .replace("NA", ""),
          wikipedia_link = Some(
            country.wikipedia_link
              .getOrElse("")
              .replace("\"", "")
          ),
          keywords = Some(
            country.keywords
              .getOrElse(List())
          )
        )
      )
  }
}