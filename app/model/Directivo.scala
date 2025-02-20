// app/models/Directivo.scala

package model

import play.api.libs.json._

case class Directivo(
                      codigoDirectivo: String,
                      nombre: String,
                      apellido: String,
                      correo: String,
                      password: String,
                      escuela: String,
                      codigoEscuela: String,
                      urlImagen: Option[String]
                    )extends User

object Directivo {
  implicit val directivoFormat: OFormat[Directivo] = Json.format[Directivo]
}