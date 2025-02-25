// app/repositories/AlumnoRepository.scala

package repositories

import javax.inject._
import model.Alumno
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AlumnoRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  private class AlumnoTable(tag: Tag) extends Table[Alumno](tag, "alumno") {
    def codigoAlumno = column[String]("codigoAlumno", O.PrimaryKey)
    def codigoProfesor = column[String]("codigoProfesor")
    def nombre = column[String]("nombre")
    def apellido = column[String]("apellido")
    def grado = column[Option[Int]]("grado")
    def grupo = column[Option[String]]("grupo")
    def correo = column[String]("correo")
    def password = column[String]("password")
    def escuela = column[String]("escuela")
    def codigoEscuela = column[String]("codigoEscuela")
    def urlImagen = column[String]("urlImagen")
    def oneSignalUserId = column[Option[String]]("oneSignalUserId")

    def * = (codigoAlumno, codigoProfesor, nombre, apellido, grado, grupo, correo, password, escuela, codigoEscuela, urlImagen, oneSignalUserId) <> ((Alumno.apply _).tupled, Alumno.unapply)
  }

  private val alumnos = TableQuery[AlumnoTable]

  // Buscar un alumno por correo y contraseña
  def findByEmailAndPassword(correo: String, password: String): Future[Option[Alumno]] = {
    db.run(alumnos.filter(a => a.correo === correo && a.password === password).result.headOption)
  }

  def create(alumno: Alumno): Future[Alumno] = {
    db.run(alumnos += alumno).map(_ => alumno)
  }

  def findByEmail(email: String): Future[Option[Alumno]] = {
    db.run(alumnos.filter(_.correo === email).result.headOption)
  }
  def updatePassword(email: String, newPassword: String): Future[Int] = {
    db.run(alumnos.filter(_.correo === email).map(_.password).update(newPassword))
  }
}