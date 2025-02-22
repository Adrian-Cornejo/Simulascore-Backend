 // app/services/ProfesorService.scala

package services


import model.Profesor
import repositories.{DirectivoRepository, ProfesorRepository}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProfesorService @Inject()(
                               profesorRepository: ProfesorRepository,
                               directivoRepository: DirectivoRepository)(implicit  ec : ExecutionContext) {
  def registrarProfesor(profesor : Profesor): Future[Either[String, Profesor]] ={
    profesorRepository.findByEmail(profesor.correo).flatMap{
      case Some(_) => Future.successful(Left("El correo ya esta registrado"))
      case None =>
        directivoRepository.findByCode(profesor.codigoDirectivo).flatMap{
          case Some(directivo)=>
            val  profesorCompleto = profesor.copy(
              escuela = directivo.escuela,
              codigoEscuela = directivo.codigoEscuela
            )
            profesorRepository.create(profesorCompleto)
            .map(Right(_))
            .recover{
              case e => Left(s"Error al registrar alumno: ${e.getMessage}")
            }
          case None => Future.successful(Left("Codigo de directivo invalido"))
        }
    }
  }
}
