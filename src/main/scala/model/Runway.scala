package model

case class Runway(
    id: Int,
    airport_ref: Int,
    airport_ident: String,
    length_ft: Option[Int],
    width_ft: Option[Int],
    surface: Option[String],
    lighted: Boolean,
    closed: Boolean,
    le_ident: Option[String],
    le_latitude_deg: Option[Double],
    le_longitude_deg: Option[Double],
    le_elevation_ft: Option[Int],
    le_heading_degT: Option[Double],
    le_displaced_threshold_ft: Option[Int],
    he_ident: Option[String],
    he_latitude_deg: Option[Double],
    he_longitude_deg: Option[Double],
    he_elevation_ft: Option[Int],
    he_heading_degT: Option[Double],
    he_displaced_threshold_ft: Option[Int]
)

object Runway {
  def fromCSV(line: String): Option[Runway] = {
    val fields = line.split(",", -1).map(_.trim)

    if (fields.length >= 20) {
      Some(
        Runway(
          id = fields(0).toIntOption.getOrElse(-1),
          airport_ref = fields(1).toIntOption.getOrElse(-1),
          airport_ident = fields(2),
          length_ft =
            if (fields(3).isEmpty) None
            else Some(fields(3).toIntOption.getOrElse(0)),
          width_ft =
            if (fields(4).isEmpty) None
            else Some(fields(4).toIntOption.getOrElse(0)),
          surface = if (fields(5).isEmpty) None else Some(fields(5)),
          lighted = fields(6).toLowerCase == "true", // Convertit en booléen
          closed = fields(7).toLowerCase == "true", // Convertit en booléen
          le_ident = if (fields(8).isEmpty) None else Some(fields(8)),
          le_latitude_deg =
            if (fields(9).isEmpty) None
            else Some(fields(9).toDoubleOption.getOrElse(0.0)),
          le_longitude_deg =
            if (fields(10).isEmpty) None
            else Some(fields(10).toDoubleOption.getOrElse(0.0)),
          le_elevation_ft =
            if (fields(11).isEmpty) None
            else Some(fields(11).toIntOption.getOrElse(0)),
          le_heading_degT =
            if (fields(12).isEmpty) None
            else Some(fields(12).toDoubleOption.getOrElse(0.0)),
          le_displaced_threshold_ft =
            if (fields(13).isEmpty) None
            else Some(fields(13).toIntOption.getOrElse(0)),
          he_ident = if (fields(14).isEmpty) None else Some(fields(14)),
          he_latitude_deg =
            if (fields(15).isEmpty) None
            else Some(fields(15).toDoubleOption.getOrElse(0.0)),
          he_longitude_deg =
            if (fields(16).isEmpty) None
            else Some(fields(16).toDoubleOption.getOrElse(0.0)),
          he_elevation_ft =
            if (fields(17).isEmpty) None
            else Some(fields(17).toIntOption.getOrElse(0)),
          he_heading_degT =
            if (fields(18).isEmpty) None
            else Some(fields(18).toDoubleOption.getOrElse(0.0)),
          he_displaced_threshold_ft =
            if (fields(19).isEmpty) None
            else Some(fields(19).toIntOption.getOrElse(0))
        )
      )
    } else {
      None
    }
  }
}
