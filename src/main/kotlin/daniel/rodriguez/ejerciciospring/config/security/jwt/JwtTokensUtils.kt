package daniel.rodriguez.ejerciciospring.config.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import daniel.rodriguez.ejerciciospring.models.Role
import daniel.rodriguez.ejerciciospring.models.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

private val algorithm: Algorithm = Algorithm.HMAC512("Ejercicio Spring de loli (viva el chocolate con menta)")

@Component
class JwtTokensUtils {
    fun create(user: User): String {
        return JWT.create()
            .withSubject(user.id.toString())
            .withHeader(mapOf("typ" to TOKEN_TYPE))
            .withClaim("username", user.username)
            .withClaim("email", user.email)
            .withClaim("role", user.role.name)
            .withExpiresAt(Date(System.currentTimeMillis() + (60 * 60 * 1_000)))
            .sign(algorithm)
    }

    fun decode(token: String): DecodedJWT? {
        val verifier = JWT.require(algorithm)
            .build()

        return try {
            verifier.verify(token)
        } catch (_: Exception) {
            null
        }
    }

    fun checkToken(token: String, role: Role): ResponseEntity<String>? {
        val decoded = decode(token)
            ?: return ResponseEntity("No token detected.", HttpStatus.UNAUTHORIZED)
        if (decoded.getClaim("role").isMissing || decoded.getClaim("role").isNull)
            return ResponseEntity("Invalid token.", HttpStatus.UNAUTHORIZED)
        if (decoded.expiresAtAsInstant.isBefore(Instant.now()))
            return ResponseEntity("Token expired.", HttpStatus.UNAUTHORIZED)
        when (role) {
            Role.ADMIN -> {
                if (!decoded.getClaim("role").asString().equals(Role.ADMIN.name)) {
                    return ResponseEntity("You are not allowed to to this.", HttpStatus.FORBIDDEN)
                }
            }

            Role.EMPLEADO -> {}
        }
        return null
    }

    companion object {
        const val TOKEN_HEADER = "Authorization"
        const val TOKEN_PREFIX = "Bearer " // Importante
        const val TOKEN_TYPE = "JWT"
    }
}