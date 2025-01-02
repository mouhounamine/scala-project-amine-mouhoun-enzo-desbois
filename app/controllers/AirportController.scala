package controllers

import javax.inject._
import play.api.mvc._
import services.AirportService
import model.Airport
import play.api.libs.json._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AirportController @Inject()(cc: ControllerComponents, airportService: AirportService)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def query(country: String) = Action.async { implicit request: Request[AnyContent] =>
    airportService.getAirportsByCountry(country).map { airports =>
      Ok(Json.toJson(airports)) // Utilisation de Json.toJson pour sérialiser les objets Airport en JSON
    }
  }
}
