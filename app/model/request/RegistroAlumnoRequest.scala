package model.request

import play.api.libs.json._

case class RegistroAlumnoRequest(
                                  first_name: String,
                                  last_name: String,
                                  email: String,
                                  password: String,
                                  confirm_password : String,
                                  codigo_maestro: String
                                )

object RegistroAlumnoRequest {
  implicit val format: OFormat[RegistroAlumnoRequest] = Json.format[RegistroAlumnoRequest]
}