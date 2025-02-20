package utils

import javax.inject.{Inject, Singleton}
import play.api.Configuration
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date
import scala.util.Try

@Singleton
class JwtUtils @Inject()(config: Configuration) {

  private val secret = config.get[String]("jwt.secret")
  private val algorithm = Algorithm.HMAC256(secret)

  def createToken(userId: String, email: String, role : String): String = {
    val currentTime = System.currentTimeMillis()
    val expirationTime = currentTime + (3600 * 1000) // 1 hora

    JWT.create()
      .withSubject(email)
      .withIssuedAt(new Date(currentTime))
      .withExpiresAt(new Date(expirationTime))
      .withClaim("user_id", userId)
      .withClaim("email", email)
      .withClaim("role", role)
      .sign(algorithm)
  }

  def verifyToken(token: String): Try[TokenContent] = {
    Try {
      val verifier = JWT.require(algorithm).build()
      val decodedJWT = verifier.verify(token)

      TokenContent(
        userId = decodedJWT.getClaim("user_id").asString(),
        email = decodedJWT.getClaim("email").asString(),
        role = decodedJWT.getClaim("role").asString()
      )
    }
  }
}

case class TokenContent(userId: String, email: String, role :String)