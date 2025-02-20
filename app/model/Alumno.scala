// app/models/Alumno.scala

package model

import play.api.libs.json._

case class Alumno(
                   codigoAlumno: String,
                   codigoProfesor: String,
                   nombre: String,
                   apellido: String,
                   grado: Option[Int],
                   grupo: Option[String],
                   correo: String,
                   password: String,
                   escuela: String,
                   codigoEscuela: String,
                   urlImagen: String = "/src/img/imgPerfilesMaestro/user.png",
                   oneSignalUserId: Option[String]
                 )extends User

object Alumno {
  implicit val alumnoFormat: OFormat[Alumno] = Json.format[Alumno]
}