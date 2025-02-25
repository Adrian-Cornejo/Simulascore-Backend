// app/models/PasswordReset.scala
package models

import java.time.LocalDateTime
import play.api.libs.json._

case class PasswordResetRequest(
                                 correo: String
                               )

case class NewPasswordRequest(
                               token: String,
                               newPassword: String
                             )

case class PasswordResetToken(
                               email: String,
                               token: String,
                               expiryDate: LocalDateTime
                             )

object PasswordResetRequest {
  implicit val format: Format[PasswordResetRequest] = Json.format[PasswordResetRequest]
}

object NewPasswordRequest {
  implicit val format: Format[NewPasswordRequest] = Json.format[NewPasswordRequest]
}