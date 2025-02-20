package controllers

import play.api.mvc._
import javax.inject._

@Inject
class CorsController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def preflight() = Action {
    NoContent
      .withHeaders(
        "Access-Control-Allow-Origin" -> "http://localhost:4200",
        "Access-Control-Allow-Methods" -> "GET, POST, PUT, DELETE, OPTIONS",
        "Access-Control-Allow-Headers" -> "Accept, Content-Type, Origin, Authorization"
      )
  }
}
