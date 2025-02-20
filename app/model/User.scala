// app/models/User.scala

package model

import play.api.libs.json._

trait User {
  def correo: String
  def password: String
}

object User {
  // Método para convertir un User a JsValue
  def toJson(user: User): JsValue = user match {
    case alumno: Alumno => Json.toJson(alumno)
    case profesor: Profesor => Json.toJson(profesor)
    case directivo: Directivo => Json.toJson(directivo)
    case _ => throw new IllegalArgumentException("Tipo de usuario no soportado")
  }
}