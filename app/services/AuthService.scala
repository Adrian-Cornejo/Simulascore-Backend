// app/services/AuthService.scala

package services

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import model._
import repositories._

@Singleton
class AuthService @Inject()(
                             alumnoRepository: AlumnoRepository,
                             profesorRepository: ProfesorRepository,
                             directivoRepository: DirectivoRepository
                           )(implicit ec: ExecutionContext) {

  // Método para autenticar un usuario según su rol
  def authenticate(loginRequest: LoginRequest): Future[Option[(String, String, String)]] = {
    alumnoRepository.findByEmailAndPassword(loginRequest.correo, loginRequest.password).flatMap {
      case Some(alumno) => Future.successful(Some((alumno.codigoAlumno, alumno.correo, "Alumno")))
      case None =>
        profesorRepository.findByEmailAndPassword(loginRequest.correo, loginRequest.password).flatMap {
          case Some(profesor) => Future.successful(Some((profesor.codigoProfesor, profesor.correo, "Profesor")))
          case None =>
            directivoRepository.findByEmailAndPassword(loginRequest.correo, loginRequest.password).map {
              case Some(directivo) => Some((directivo.codigoDirectivo, directivo.correo, "Directivo"))
              case None => None
            }
        }
    }
  }
}
