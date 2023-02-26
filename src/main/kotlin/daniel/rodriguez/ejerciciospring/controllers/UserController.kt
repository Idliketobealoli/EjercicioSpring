package daniel.rodriguez.ejerciciospring.controllers

import daniel.rodriguez.ejerciciospring.config.APIConfig
import daniel.rodriguez.ejerciciospring.config.security.jwt.JwtTokensUtils
import daniel.rodriguez.ejerciciospring.dto.*
import daniel.rodriguez.ejerciciospring.exception.UserExceptionBadRequest
import daniel.rodriguez.ejerciciospring.mappers.toDTO
import daniel.rodriguez.ejerciciospring.models.User
import daniel.rodriguez.ejerciciospring.services.UserService
import jakarta.validation.Valid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("${APIConfig.API_PATH}/users")
class UserController
@Autowired constructor(
    private val service: UserService,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenUtils: JwtTokensUtils
) {
    @PostMapping("/register")
    suspend fun register(
        @Valid @RequestBody dto: UserDTOcreacion
    ): ResponseEntity<UserDTOandToken> = withContext(Dispatchers.IO) {
        try {
            val saved = service.saveUser(dto)
            ResponseEntity.ok(UserDTOandToken(saved.toDTO(), jwtTokenUtils.create(saved)))
        } catch (e: Exception) {
            throw UserExceptionBadRequest(e.message)
        }
    }

    @PostMapping("/login")
    suspend fun login(
        @Valid @RequestBody dto: UserDTOlogin
    ): ResponseEntity<UserDTOandToken> = withContext(Dispatchers.IO) {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(dto.username, dto.password)
        )
        SecurityContextHolder.getContext().authentication = authentication

        val user = authentication.principal as User

        ResponseEntity.ok(UserDTOandToken(user.toDTO(), jwtTokenUtils.create(user)))
    }

    @GetMapping("/me")
    suspend fun findMe(
        @AuthenticationPrincipal user: User
    ): ResponseEntity<UserDTO> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(user.toDTO())
    }

    @GetMapping("")
    suspend fun findAll(
        @AuthenticationPrincipal user: User
    ): ResponseEntity<List<UserDTO>> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.findAllUsers())
    }

    @GetMapping("/{id}")
    suspend fun findByUUID(
        @AuthenticationPrincipal u: User,
        @PathVariable id: UUID
    ): ResponseEntity<UserDTO> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.findUserByUuid(id))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    suspend fun delete(
        @AuthenticationPrincipal u: User,
        @PathVariable id: UUID
    ): ResponseEntity<UserDTO> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.deleteUser(id))
    }

    // Esto es unicamente para la carga de datos inicial de usuarios
    suspend fun createInit(dto: UserDTOcreacion) = withContext(Dispatchers.IO) {
        service.saveUser(dto)
    }
}