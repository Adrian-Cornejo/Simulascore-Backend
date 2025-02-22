// app/controllers/AlumnoController.scala

package controllers

import javax.inject._
import model.request
import model.request.RegistroAlumnoRequest
import model.Alumno
import play.api.libs.json._
import play.api.mvc._
import repositories.AlumnoRepository
import services.AlumnoService
import utils.CodeGenerator

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AlumnoController @Inject()(
                                  cc: ControllerComponents,
                                  alumnoService: AlumnoService,
                                  alumnoRepository: AlumnoRepository)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def registrar() = Action.async(parse.json) { request =>
    request.body.validate[RegistroAlumnoRequest] match {
      case JsSuccess(registroRequest, _) =>
        // Generar código único para el alumno
        val codigoAlumno = CodeGenerator.generateUniqueCode()

        val nuevoAlumno = Alumno(
          codigoAlumno = codigoAlumno,
          codigoProfesor = registroRequest.codigo_maestro,
          nombre = registroRequest.first_name,
          apellido = registroRequest.last_name,
          grado = None,
          grupo = None,
          correo = registroRequest.email,
          password = registroRequest.password, // Nota: Debería encriptarse
          escuela = "", // Se puede obtener del profesor
          codigoEscuela = "", // Se puede obtener del profesor
          urlImagen = "/src/img/imgPerfilesMaestro/user.png",
          oneSignalUserId = None
        )

        alumnoService.registrarAlumno(nuevoAlumno).map {
          case Right(alumno) => Created(Json.obj(
            "mensaje" -> "Alumno registrado exitosamente",
            "alumno" -> alumno
          ))
          case Left(error) => BadRequest(Json.obj("error" -> error))
        }

      case JsError(errors) =>
        Future.successful(BadRequest(Json.obj("error" -> "Datos inválidos")))
    }
  }
}