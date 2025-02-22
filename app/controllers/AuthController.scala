package controllers

import model.LoginRequest


import javax.inject._
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}
import services.AuthService
import utils.JwtUtils


@Singleton
class AuthController @Inject()(
                                cc: ControllerComponents,
                                authService: AuthService,
                                jwtUtils: JwtUtils // Inyectar JwtUtils
                              )(implicit ec: ExecutionContext) extends AbstractController(cc) {

  // Endpoint para manejar solicitudes preflight (CORS)
  def preflight(all: String) = Action {
    NoContent.withHeaders(
      "Access-Control-Allow-Origin" -> "http://localhost:4200",
      "Access-Control-Allow-Methods" -> "GET, POST, PUT, DELETE, OPTIONS",
      "Access-Control-Allow-Headers" -> "Content-Type, Authorization"
    )
  }

  // Endpoint para el login
  def login = Action.async(parse.json) { implicit request =>
    request.body.validate[LoginRequest].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors)))
          .withHeaders("Access-Control-Allow-Origin" -> "http://localhost:4200"))
      },
      loginRequest => {
        authService.authenticate(loginRequest).map {
          case Some((userId, email, role)) =>
            // Generar un token JWT
            val token = jwtUtils.createToken(userId, email, role)

            // Devolver la respuesta con el token y los datos del usuario
            Ok(Json.obj(
              "message" -> "Login exitoso",
              "token" -> token, // Incluir el token en la respuesta
              "userId" -> userId,
              "email" -> email,
              "role" -> role
            )).withHeaders("Access-Control-Allow-Origin" -> "http://localhost:4200")
          case None =>
            Unauthorized(Json.obj("message" -> "Credenciales inválidas"))
              .withHeaders("Access-Control-Allow-Origin" -> "http://localhost:4200")
        }
      }
    )
  }
}