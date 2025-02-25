// app/services/AlumnoService.scala

package services

import javax.inject._
import model.Alumno
import repositories.{AlumnoRepository, ProfesorRepository, DirectivoRepository}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AlumnoService @Inject()(
                               alumnoRepository: AlumnoRepository,
                               profesorRepository: ProfesorRepository,
                               directivoRepository: DirectivoRepository)(implicit ec: ExecutionContext) {

  def registrarAlumno(alumno: Alumno): Future[Either[String, Alumno]] = {
    val correo = alumno.correo

    // Verificar si el correo ya existe en Alumno, Profesor o Directivo
    val verificacionCorreo = for {
      existeAlumno <- alumnoRepository.findByEmail(correo)
      existeProfesor <- profesorRepository.findByEmail(correo)
      existeDirectivo <- directivoRepository.findByEmail(correo)
    } yield (existeAlumno, existeProfesor, existeDirectivo)

    // Procesar el resultado de la verificación
    verificacionCorreo.flatMap {
      case (Some(_), _, _) => Future.successful(Left("El correo ya está registrado como alumno"))
      case (_, Some(_), _) => Future.successful(Left("El correo ya está registrado como profesor"))
      case (_, _, Some(_)) => Future.successful(Left("El correo ya está registrado como directivo"))
      case (None, None, None) =>
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