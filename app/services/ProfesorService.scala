// app/services/ProfesorService.scala

package services

import model.Profesor
import repositories.{DirectivoRepository, ProfesorRepository, AlumnoRepository}
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProfesorService @Inject()(
                                 profesorRepository: ProfesorRepository,
                                 directivoRepository: DirectivoRepository,
                                 alumnoRepository: AlumnoRepository)(implicit ec: ExecutionContext) {

  def registrarProfesor(profesor: Profesor): Future[Either[String, Profesor]] = {
    val correo = profesor.correo

    // Verificar si el correo ya existe en Profesor, Alumno o Directivo
    val verificacionCorreo = for {
      existeProfesor <- profesorRepository.findByEmail(correo)
      existeAlumno <- alumnoRepository.findByEmail(correo)
      existeDirectivo <- directivoRepository.findByEmail(correo)
    } yield (existeProfesor, existeAlumno, existeDirectivo)

    // Procesar el resultado de la verificación
    verificacionCorreo.flatMap {
      case (Some(_), _, _) => Future.successful(Left("El correo ya está registrado como profesor"))
      case (_, Some(_), _) => Future.successful(Left("El correo ya está registrado como alumno"))
      case (_, _, Some(_)) => Future.successful(Left("El correo ya está registrado como directivo"))
      case (None, None, None) =>
        // Verificar si el código del directivo existe
        directivoRepository.findByCode(profesor.codigoDirectivo).flatMap {
          case Some(directivo) =>
            // Crear profesor con datos de la escuela del directivo
            val profesorCompleto = profesor.copy(
              escuela = directivo.escuela,
              codigoEscuela = directivo.codigoEscuela
            )
            profesorRepository.create(profesorCompleto)
              .map(Right(_))
              .recover {
                case e => Left(s"Error al registrar profesor: ${e.getMessage}")
              }
          case None => Future.successful(Left("Código de directivo inválido"))
        }
    }
  }
}