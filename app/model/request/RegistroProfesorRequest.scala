package model.request
import play.api.libs.json._

case class RegistroProfesorRequest (
                                   first_name : String,
                                   last_name : String,
                                   email : String,
                                   password : String,
                                   confirm_password : String,
                                   codigo_directivo : String
                                   )
object RegistroProfesorRequest {
  implicit  val format : OFormat[RegistroProfesorRequest] = Json.format[RegistroProfesorRequest]
}

