package controllers

import javax.inject._
import play.api.mvc._
import services.ReportService
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._

@Singleton
class ReportController @Inject()(cc: ControllerComponents, reportService: ReportService)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def topCountries() = Action.async { implicit request: Request[AnyContent] =>
    reportService.getTopCountries().map { topCountries =>
      Ok(Json.toJson(topCountries))
    }
  }

  def bottomCountries() = Action.async { implicit request: Request[AnyContent] =>
    reportService.getBottomCountries().map { bottomCountries =>
      Ok(Json.toJson(bottomCountries))
    }
  }

  def runwayTypes() = Action.async { implicit request: Request[AnyContent] =>
    reportService.getRunwayTypesByCountry.map { runwayTypesByCountry =>
      Ok(Json.toJson(runwayTypesByCountry))
    }
  }

  def topRunwayLatitudes() = Action.async { implicit request: Request[AnyContent] =>
    reportService.getTopRunwayLatitudes().map { latitudes =>
      Ok(Json.toJson(latitudes))
    }
  }
}
