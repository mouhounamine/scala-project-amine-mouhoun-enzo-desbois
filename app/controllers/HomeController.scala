package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/** This controller creates an Action to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  /** Create an Action to render a string for the / endpoint.
    */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok("Welcome to the Home Page!")
  }

  /** Create an Action to render a string for the /explore endpoint.
    */
  def explore() = Action {
    Ok("Explore the features of our application!")
  }

  /** Create an Action to render a string for the /tutorial endpoint.
    */
  def tutorial() = Action { implicit request: Request[AnyContent] =>
    Ok("Follow our tutorial to get started!")
  } 

} 