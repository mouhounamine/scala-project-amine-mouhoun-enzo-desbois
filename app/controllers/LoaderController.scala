package controllers

import javax.inject._
import play.api.mvc._
import services.LoaderService
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class LoaderController @Inject()(
    cc: ControllerComponents,
    loaderService: LoaderService
)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  def loadCountries() = Action.async { implicit request: Request[AnyContent] =>
    loaderService.loadCountries.map { result =>
      Ok(s"Countries loaded: $result")
    }
  }

  def loadRunways() = Action.async { implicit request: Request[AnyContent] =>
    loaderService.loadRunways.map { result =>
      Ok(s"Runways loaded: $result")
    }
  }

  def loadAirports() = Action.async { implicit request: Request[AnyContent] =>
    loaderService.loadAirports.map { result =>
      Ok(s"Airports loaded: $result")
    }
  }
}
