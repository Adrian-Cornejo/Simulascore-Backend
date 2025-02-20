// app/repositories/DirectivoRepository.scala

package repositories

import javax.inject._
import model.Directivo
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DirectivoRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  private class DirectivoTable(tag: Tag) extends Table[Directivo](tag, "directivo") {
    def codigoDirectivo = column[String]("codigoDirectivo", O.PrimaryKey)
    def nombre = column[String]("nombre")
    def apellido = column[String]("apellido")
    def correo = column[String]("correo")
    def password = column[String]("password")
    def escuela = column[String]("escuela")
    def codigoEscuela = column[String]("codigoEscuela")
    def urlImagen = column[Option[String]]("urlImagen")

    def * = (codigoDirectivo, nombre, apellido, correo, password, escuela, codigoEscuela, urlImagen) <> ((Directivo.apply _).tupled, Directivo.unapply)
  }

  private val directivos = TableQuery[DirectivoTable]

  // Buscar un directivo por correo y contraseña
  def findByEmailAndPassword(correo: String, password: String): Future[Option[Directivo]] = {
    db.run(directivos.filter(d => d.correo === correo && d.password === password).result.headOption)
  }
}