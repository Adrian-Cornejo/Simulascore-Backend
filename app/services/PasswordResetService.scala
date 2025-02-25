// app/services/PasswordResetService.scala
package services

import model.Alumno
import javax.inject._
import models.{PasswordResetToken}
import repositories.{PasswordResetRepository, AlumnoRepository}
import java.time.LocalDateTime
import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}
import play.api.Logger

@Singleton
class PasswordResetService @Inject()(
                                      passwordResetRepository: PasswordResetRepository,
                                      alumnoRepository: AlumnoRepository,
                                      emailService: EmailService
                                    )(implicit ec: ExecutionContext) {

  private val logger = Logger(this.getClass)

  def requestPasswordReset(email: String): Future[Either[String, Unit]] = {
    alumnoRepository.findByEmail(email).flatMap {
      case None =>
        logger.warn(s"Intento de recuperación de contraseña para email no registrado: $email")
        Future.successful(Left("Si el correo existe, recibirás un email con las instrucciones"))
      case Some(_) =>
        val token = UUID.randomUUID().toString
        val expiryDate = LocalDateTime.now().plusHours(1)
        val resetToken = PasswordResetToken(email, token, expiryDate)

        (for {
          _ <- passwordResetRepository.create(resetToken)
          _ <- emailService.sendPasswordResetEmail(email, token)
        } yield Right(())).recover { case e =>
          logger.error(s"Error en proceso de recuperación para $email: ${e.getMessage}")
          Left("Error al procesar la solicitud. Por favor, intenta más tarde.")
        }
    }
  }

  def resetPassword(token: String, newPassword: String): Future[Either[String, Unit]] = {
    passwordResetRepository.findByToken(token).flatMap {
      case None =>
        logger.warn(s"Intento de reset con token inválido: $token")
        Future.successful(Left("El enlace de recuperación es inválido o ha expirado"))
      case Some(resetToken) if resetToken.expiryDate.isBefore(LocalDateTime.now()) =>
        passwordResetRepository.deleteToken(token).map { _ =>
          logger.info(s"Token expirado eliminado para ${resetToken.email}")
          Left("El enlace de recuperación ha expirado")
        }
      case Some(resetToken) =>
        (for {
          _ <- alumnoRepository.updatePassword(resetToken.email, newPassword)
          _ <- passwordResetRepository.deleteToken(token)
        } yield {
          logger.info(s"Contraseña actualizada exitosamente para ${resetToken.email}")
          Right(())
        }).recover { case e =>
          logger.error(s"Error actualizando contraseña para ${resetToken.email}: ${e.getMessage}")
          Left("Error al actualizar la contraseña. Por favor, intenta más tarde.")
        }
    }
  }
}