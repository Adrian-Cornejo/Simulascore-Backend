// app/controllers/PasswordResetController.scala
package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.{PasswordResetRequest, NewPasswordRequest}
import services.PasswordResetService
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PasswordResetController @Inject()(
                                         cc: ControllerComponents,
                                         passwordResetService: PasswordResetService
                                       )(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def requestReset(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[PasswordResetRequest] match {
      case JsSuccess(resetRequest, _) =>
        passwordResetService.requestPasswordReset(resetRequest.correo).map {
          case Right(_) => Ok(Json.obj(
            "message" -> "Si el correo existe, recibirás un email con las instrucciones"
          ))
          case Left(error) => BadRequest(Json.obj("error" -> error))
        }
      case JsError(errors) =>
        Future.successful(BadRequest(Json.obj(
          "error" -> "Formato de solicitud inválido",
          "details" -> JsError.toJson(errors)
        )))
    }
  }

  def resetPassword(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[NewPasswordRequest] match {
      case JsSuccess(newPasswordRequest, _) =>
        passwordResetService.resetPassword(
          newPasswordRequest.token,
          newPasswordRequest.newPassword
        ).map {
          case Right(_) => Ok(Json.obj(
            "message" -> "Contraseña actualizada exitosamente"
          ))
          case Left(error) => BadRequest(Json.obj("error" -> error))
        }
      case JsError(errors) =>
        Future.successful(BadRequest(Json.obj(
          "error" -> "Formato de solicitud inválido",
          "details" -> JsError.toJson(errors)
        )))
    }
  }
}