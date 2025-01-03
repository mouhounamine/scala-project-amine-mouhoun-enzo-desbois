package services

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import rest.LoaderRest

@Singleton
class LoaderService @Inject()(loaderRest: LoaderRest)(implicit ec: ExecutionContext) {

  def loadCountries: Future[Int] = {
    loaderRest.loadCountries()
  }

  def loadRunways: Future[Int] = {
    loaderRest.loadRunways()
  }

  def loadAirports: Future[Int] = {
    loaderRest.loadAirports()
  }
}
