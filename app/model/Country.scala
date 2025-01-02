package model

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
          code = fields(1),
          name = fields(2),
          continent = fields(3),
          wikipedia_link = if (fields(4).isEmpty) None else Some(fields(4)),
          keywords =
            if (fields.isDefinedAt(5) && fields(5).nonEmpty) Some(fields(5))
            else None
        )
      ).filter(_.id >= 0)
    } else None
  }
}
