package repositories

import javax.inject._
import play.api.db.slick._
import models.PasswordResetToken
import scala.concurrent.{ExecutionContext, Future}
import java.time.LocalDateTime
import java.sql.Timestamp
import slick.jdbc.MySQLProfile.api._

object MappedTypes {
  // Mapeo para convertir LocalDateTime <--> Timestamp
  implicit val localDateTimeColumnType = MappedColumnType.base[LocalDateTime, Timestamp](
    ldt => Timestamp.valueOf(ldt),  // LocalDateTime -> Timestamp (MySQL)
    ts => ts.toLocalDateTime        // Timestamp -> LocalDateTime (Scala)
  )
}

@Singleton
class PasswordResetRepository @Inject()(
                                         protected val dbConfigProvider: DatabaseConfigProvider
                                       )(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[slick.jdbc.MySQLProfile] {

  import MappedTypes._ // Importa los mapeos aquí

  private class PasswordResetTokens(tag: Tag) extends Table[PasswordResetToken](tag, "password_reset_tokens") {
    def email = column[String]("email")
    def token = column[String]("token", O.PrimaryKey)
    def expiryDate = column[LocalDateTime]("expiry_date") // Usa LocalDateTime con la conversión

    def * = (email, token, expiryDate) <> ((PasswordResetToken.apply _).tupled, PasswordResetToken.unapply)
  }

  private val tokens = TableQuery[PasswordResetTokens]

  def create(resetToken: PasswordResetToken): Future[Unit] = {
    db.run(tokens += resetToken).map(_ => ())
  }

  def findByToken(token: String): Future[Option[PasswordResetToken]] = {
    db.run(tokens.filter(_.token === token).result.headOption)
  }

  def deleteToken(token: String): Future[Int] = {
    db.run(tokens.filter(_.token === token).delete)
  }

  def deleteExpiredTokens(): Future[Int] = {
    val now = LocalDateTime.now()
    db.run(tokens.filter(_.expiryDate < now).delete)
  }
}
