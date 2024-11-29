package model

case class Airport(
    id: Int,
    ident: String,
    `type`: String,
    name: String,
    latitude_deg: Double,
    longitude_deg: Double,
    elevation_ft: Option[Int],
    continent: String,
    iso_country: String,
    iso_region: String,
    municipality: Option[String],
    scheduled_service: String,
    gps_code: Option[String],
    iata_code: Option[String],
    local_code: Option[String],
    home_link: Option[String],
    wikipedia_link: Option[String],
    keywords: Option[String]
)

object Airport {
  def fromCSV(line: String): Option[Airport] = {
    val fields = line.split(",", -1).map(_.trim)

    if (fields.length >= 18) {
      Some(
        Airport(
          id = fields(0).toIntOption.getOrElse(-1),
          ident = fields(1),
          `type` = fields(2),
          name = fields(3),
          latitude_deg = fields(4).toDoubleOption.getOrElse(-1),
          longitude_deg = fields(5).toDoubleOption.getOrElse(-1),
          elevation_ft =
            if (fields(6).isEmpty) None
            else Some(fields(6).toIntOption.getOrElse(0)),
          continent = fields(7),
          iso_country = fields(8),
          iso_region = fields(9),
          municipality = if (fields(10).isEmpty) None else Some(fields(10)),
          scheduled_service = fields(11),
          gps_code = if (fields(12).isEmpty) None else Some(fields(12)),
          iata_code = if (fields(13).isEmpty) None else Some(fields(13)),
          local_code = if (fields(14).isEmpty) None else Some(fields(14)),
          home_link = if (fields(15).isEmpty) None else Some(fields(15)),
          wikipedia_link = if (fields(16).isEmpty) None else Some(fields(16)),
          keywords = if (fields(17).isEmpty) None else Some(fields(17))
        )
      )
    } else {
      None
    }
  }
}
