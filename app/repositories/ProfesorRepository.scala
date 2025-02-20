// app/repositories/ProfesorRepository.scala

package repositories

import javax.inject._
import model.Profesor
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProfesorRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  private class ProfesorTable(tag: Tag) extends Table[Profesor](tag, "profesor") {
    def codigoProfesor = column[String]("codigoProfesor", O.PrimaryKey)
    def codigoDirectivo = column[String]("codigoDirectivo")
    def nombre = column[String]("nombre")
    def apellido = column[String]("apellido")
    def correo = column[String]("correo")
    def password = column[String]("password")
    def escuela = column[String]("escuela")
    def codigoEscuela = column[String]("codigoEscuela")
    def urlImagen = column[String]("urlImagen")
    def oneSignalUserId = column[String]("oneSignalUserId")

    def * = (codigoProfesor, codigoDirectivo, nombre, apellido, correo, password, escuela, codigoEscuela, urlImagen, oneSignalUserId) <> ((Profesor.apply _).tupled, Profesor.unapply)

  }

  private val profesores = TableQuery[ProfesorTable]

  // Buscar un profesor por correo y contraseña
  def findByEmailAndPassword(correo: String, password: String): Future[Option[Profesor]] = {
    db.run(profesores.filter(p => p.correo === correo && p.password === password).result.headOption)
  }
}