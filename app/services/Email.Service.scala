// app/services/EmailService.scala
package services

import play.api.{Configuration, Logger}
import play.api.libs.mailer.{Email, MailerClient}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EmailService @Inject()(
                              mailerClient: MailerClient,
                              configuration: Configuration
                            )(implicit ec: ExecutionContext) {

  private val logger = Logger(this.getClass)
  private val fromEmail = configuration.get[String]("play.mailer.from")

  def sendPasswordResetEmail(toEmail: String, token: String): Future[Unit] = Future {
    try {
      val resetLink = s"${configuration.get[String]("app.frontend.url")}/new-password?token=$token"

      val email = Email(
        subject = "Recuperación de Contraseña - Simulascore",
        from = fromEmail,
        to = Seq(toEmail),
        bodyText = Some(s"""
                           |Has solicitado restablecer tu contraseña en Simulascore.
                           |
                           |Por favor, haz clic en el siguiente enlace para crear una nueva contraseña:
                           |$resetLink
                           |
                           |Este enlace expirará en 1 hora.
                           |
                           |Si no solicitaste este cambio, puedes ignorar este correo.
          """.stripMargin),
        bodyHtml = Some(s"""
                           |<html>
                           |<body style="font-family: Arial, sans-serif;">
                           |<div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                           |  <h2>Recuperación de Contraseña - Simulascore</h2>
                           |  <p>Has solicitado restablecer tu contraseña.</p>
                           |  <p>Por favor, haz clic en el siguiente enlace para crear una nueva contraseña:</p>
                           |  <p>
                           |    <a href="$resetLink" style="background-color: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">
                           |      Restablecer Contraseña
                           |    </a>
                           |  </p>
                           |  <p>Este enlace expirará en 1 hora.</p>
                           |  <p style="color: #666;">Si no solicitaste este cambio, puedes ignorar este correo.</p>
                           |</div>
                           |</body>
                           |</html>
          """.stripMargin)
      )

      mailerClient.send(email)
      logger.info(s"Email de recuperación enviado a $toEmail")
    } catch {
      case e: Exception =>
        logger.error(s"Error enviando email a $toEmail: ${e.getMessage}")
        throw e
    }
  }
}