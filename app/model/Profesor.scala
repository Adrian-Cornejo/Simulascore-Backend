// app/models/Profesor.scala

package model

import play.api.libs.json._

case class Profesor(
                     codigoProfesor: String,
                     codigoDirectivo: String,
                     nombre: String,
                     apellido: String,
                     correo: String,
                     password: String,
                     escuela: String,
                     codigoEscuela: String,
                     urlImagen: String = "/src/img/imgPerfilesMaestro/user.png",
                     oneSignalUserId: String
                   )extends User

object Profesor {
  implicit val profesorFormat: OFormat[Profesor] = Json.format[Profesor]
}