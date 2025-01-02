package controllers

import javax.inject._
import play.api.mvc._
import services.ReportService
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._

@Singleton
class ReportController @Inject()(cc: ControllerComponents, reportService: ReportService)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  // Rapport des 10 pays avec le plus grand nombre d'aéroports
  def topCountries() = Action.async { implicit request: Request[AnyContent] =>
    reportService.getTopCountries.map { countries =>
      Ok(Json.toJson(countries))
    }
  }

  // Rapport des types de pistes par pays
  def runwayTypes() = Action.async { implicit request: Request[AnyContent] =>
    reportService.getRunwayTypesByCountry.map { runwayTypesByCountry =>
      Ok(Json.toJson(runwayTypesByCountry))
    }
  }

  // Rapport des latitudes des pistes les plus communes
  def topRunwayLatitudes() = Action.async { implicit request: Request[AnyContent] =>
    val latitudes = "Top 10 common runway latitudes"
    Future.successful(Ok(latitudes))
  }
}
