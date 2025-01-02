package model

import play.api.libs.json._  

case class Country(
    id: Int,
    code: String,
    name: String,
    continent: String,
    wikipedia_link: Option[String],
    keywords: Option[String]
)

object Country {
  def fromCSV(line: String): Option[Country] = {
    val fields = line.split(",", -1).map(_.trim)
    if (fields.length >= 5) {
      Some(
        Country(
          id = fields(0).toIntOption.getOrElse(-1),
          code = fields(1).toUpperCase().trim(),
          name = fields(2).toUpperCase().trim(),
          continent = fields(3).toUpperCase().trim(),
          wikipedia_link = if (fields(4).isEmpty) None else Some(fields(4)),
          keywords = if (fields.isDefinedAt(5) && fields(5).nonEmpty) Some(fields(5).trim) else None
        )
      ).filter(_.id >= 0)
    } else None
  }
  
  def clean(countries: List[Country]): List[Country] = {
    countries
      .distinct
      .filter(_.id >= 0)
      .map(country => country.copy(
        code = country.code.toUpperCase.trim,
        name = country.name.split(" ").map(_.capitalize).mkString(" ").trim
      ))
  }
}