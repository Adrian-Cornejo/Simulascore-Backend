// app/models/LoginRequest.scala

package model

import play.api.libs.json._

case class LoginRequest(correo: String, password: String)

object LoginRequest {
  implicit val loginRequestFormat: OFormat[LoginRequest] = Json.format[LoginRequest]
}