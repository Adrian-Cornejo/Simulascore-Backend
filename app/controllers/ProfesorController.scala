// app/controllers/ProfesorController.scala

package controllers


import model.{Alumno, Profesor}
import model.request.RegistroProfesorRequest
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.mvc._
import repositories.ProfesorRepository
import services.ProfesorService
import utils.CodeGenerator

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProfesorController @Inject()(
                                  cc : ControllerComponents,
                                  profesorService: ProfesorService,
                                  profesorRepository: ProfesorRepository)(implicit  ec :ExecutionContext)
extends AbstractController(cc){

  def registrarProfesor = Action.async(parse.json){ request =>
    request.body.validate[RegistroProfesorRequest] match {
      case  JsSuccess(registroRequest, _) =>
        val codigoProfesor = CodeGenerator.generateUniqueCode()

        val nuevoProfesor = Profesor(
          codigoProfesor = codigoProfesor,
          codigoDirectivo = registroRequest.codigo_directivo,
          nombre = registroRequest.first_name,
          apellido = registroRequest.last_name,
          correo = registroRequest.email,
          password = registroRequest.password,
          escuela = "",
          codigoEscuela = "",
          urlImagen = "/src/img/imgPerfilesMaestro/user.png",
          oneSignalUserId = None
        )

        profesorService.registrarProfesor(nuevoProfesor).map {
          case Right(profesor) => Created(Json.obj(
            "mensaje" -> "Profesor registrado exitosamente",
            "profesor" -> profesor
          ))
          case Left(error) => BadRequest(Json.obj("error" -> error))
        }

      case JsError(errors) =>
        Future.successful(BadRequest(Json.obj("error" -> "Datos inválidos")))
    }
    }

}