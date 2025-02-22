// utils/CodeGenerator.scala

package utils

import scala.util.Random

object CodeGenerator {
  private val letras = ('A' to 'Z').toArray
  private val alfanumerico = (('A' to 'Z') ++ ('0' to '9')).toArray

  def generateUniqueCode(): String = {
    // Generar las primeras 3 letras mayúsculas
    val prefix = (1 to 3).map(_ => letras(Random.nextInt(letras.length))).mkString

    // Generar los 3 caracteres alfanuméricos
    val suffix = (1 to 3).map(_ => alfanumerico(Random.nextInt(alfanumerico.length))).mkString

    s"$prefix-$suffix"
  }
}