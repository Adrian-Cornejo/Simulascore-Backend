// app/services/AlumnoService.scala

package services

import javax.inject._
import model.Alumno
import repositories.{AlumnoRepository, ProfesorRepository}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AlumnoService @Inject()(
                               alumnoRepository: AlumnoRepository,
                               profesorRepository: ProfesorRepository)(implicit ec: ExecutionContext) {

  def registrarAlumno(alumno: Alumno): Future[Either[String, Alumno]] = {
    // Verificar si el correo ya existe
    alumnoRepository.findByEmail(alumno.correo).flatMap {
      case Some(_) => Future.successful(Left("El correo ya está registrado"))
      case None =>
        // Verificar si el código del profesor existe
        profesorRepository.findByCode(alumno.codigoProfesor).flatMap {
          case Some(profesor) =>
            // Crear alumno con datos de la escuela del profesor
            val alumnoCompleto = alumno.copy(
              escuela = profesor.escuela,
              codigoEscuela = profesor.codigoEscuela
            )
            alumnoRepository.create(alumnoCompleto)
              .map(Right(_))
              .recover {
                case e => Left(s"Error al registrar alumno: ${e.getMessage}")
              }
          case None => Future.successful(Left("Código de profesor inválido"))
        }
    }
  }
}