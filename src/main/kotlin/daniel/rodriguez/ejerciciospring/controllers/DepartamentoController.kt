package daniel.rodriguez.ejerciciospring.controllers

import daniel.rodriguez.ejerciciospring.config.APIConfig
import daniel.rodriguez.ejerciciospring.dto.DepartamentoDTO
import daniel.rodriguez.ejerciciospring.dto.DepartamentoDTOcreacion
import daniel.rodriguez.ejerciciospring.models.User
import daniel.rodriguez.ejerciciospring.services.DepartamentoService
import jakarta.validation.Valid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("${APIConfig.API_PATH}/departamentos")
class DepartamentoController
@Autowired constructor(
    private val service: DepartamentoService
) {
    @GetMapping("")
    suspend fun findAll(
        @AuthenticationPrincipal u: User
    ): ResponseEntity<List<DepartamentoDTO>> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.findAllDepartamentos())
    }

    @GetMapping("/{id}")
    suspend fun findByUUID(
        @AuthenticationPrincipal u: User,
        @PathVariable id: UUID
    ): ResponseEntity<DepartamentoDTO> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.findDepartamentoByUuid(id))
    }

    @PostMapping("")
    suspend fun create(
        @Valid @RequestBody dto: DepartamentoDTOcreacion,
        @AuthenticationPrincipal u: User
    ): ResponseEntity<DepartamentoDTO> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.saveDepartamento(dto))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    suspend fun delete(
        @AuthenticationPrincipal u: User,
        @PathVariable id: UUID
    ): ResponseEntity<DepartamentoDTO> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.deleteDepartamento(id))
    }

    // Este es solo para la carga de datos iniciales de departamento
    suspend fun createInit(dto: DepartamentoDTOcreacion) = withContext(Dispatchers.IO) {
        service.saveDepartamento(dto)
    }
}