package model

import play.api.libs.json._  

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

  /** Nettoie une instance de Runway */
  def clean(runway: Runway): Runway = {
    runway.copy(
      airport_ident = runway.airport_ident.trim.toUpperCase, // Normalise en majuscules
      length_ft = runway.length_ft.filter(_ > 0), // Supprime les longueurs invalides
      width_ft = runway.width_ft.filter(_ > 0), // Supprime les largeurs invalides
      surface = runway.surface.map(_.trim.toLowerCase), // Normalise les surfaces
      le_ident = runway.le_ident.map(_.trim.toUpperCase), // Identifiant LE en majuscules
      he_ident = runway.he_ident.map(_.trim.toUpperCase), // Identifiant HE en majuscules
      le_latitude_deg = runway.le_latitude_deg.filter(isValidLatitude), // Valide les latitudes
      le_longitude_deg = runway.le_longitude_deg.filter(isValidLongitude), // Valide les longitudes
      he_latitude_deg = runway.he_latitude_deg.filter(isValidLatitude), // Valide les latitudes
      he_longitude_deg = runway.he_longitude_deg.filter(isValidLongitude), // Valide les longitudes
      le_heading_degT = runway.le_heading_degT.filter(isValidHeading), // Valide les orientations
      he_heading_degT = runway.he_heading_degT.filter(isValidHeading) // Valide les orientations
    )
  }

  /** Nettoie une liste complète de Runway */
  def cleanAll(runways: List[Runway]): List[Runway] = {
    runways.map(clean)
  }

  /** Vérifie si une latitude est valide (-90 à 90) */
  private def isValidLatitude(latitude: Double): Boolean = {
    latitude >= -90.0 && latitude <= 90.0
  }

  /** Vérifie si une longitude est valide (-180 à 180) */
  private def isValidLongitude(longitude: Double): Boolean = {
    longitude >= -180.0 && longitude <= 180.0
  }

  /** Vérifie si une orientation est valide (0 à 360 degrés) */
  private def isValidHeading(heading: Double): Boolean = {
    heading >= 0.0 && heading <= 360.0
  }
}