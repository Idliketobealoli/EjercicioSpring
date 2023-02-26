package daniel.rodriguez.ejerciciospring.controllers

import daniel.rodriguez.ejerciciospring.config.APIConfig
import daniel.rodriguez.ejerciciospring.dto.EmpleadoDTO
import daniel.rodriguez.ejerciciospring.dto.EmpleadoDTOcreacion
import daniel.rodriguez.ejerciciospring.mappers.toDTO
import daniel.rodriguez.ejerciciospring.models.User
import daniel.rodriguez.ejerciciospring.services.EmpleadoService
import jakarta.validation.Valid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("${APIConfig.API_PATH}/empleados")
class EmpleadoController
@Autowired constructor(
    private val service: EmpleadoService
) {
    @GetMapping("")
    suspend fun findAll(
        @AuthenticationPrincipal u: User
    ): ResponseEntity<List<EmpleadoDTO>> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.findAllEmpleados())
    }

    @GetMapping("/{id}")
    suspend fun findByUUID(
        @AuthenticationPrincipal u: User,
        @PathVariable id: UUID
    ): ResponseEntity<EmpleadoDTO> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.findEmpleadoByUuid(id).toDTO())
    }

    @PostMapping("")
    suspend fun create(
        @Valid @RequestBody dto: EmpleadoDTOcreacion,
        @AuthenticationPrincipal u: User
    ): ResponseEntity<EmpleadoDTO> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.saveEmpleado(dto))
    }

    @PutMapping("/avatar/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    private suspend fun updateAvatar(
        @AuthenticationPrincipal u: User,
        @RequestPart("file") file: MultipartFile,
        @PathVariable id: UUID
    ): ResponseEntity<EmpleadoDTO> = withContext(Dispatchers.IO) {
        val empleado = service.findEmpleadoByUuid(id)
        val saved = service.updateAvatar(empleado, file)

        ResponseEntity.ok(saved.toDTO())
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    suspend fun delete(
        @AuthenticationPrincipal u: User,
        @PathVariable id: UUID
    ): ResponseEntity<EmpleadoDTO> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.deleteEmpleado(id))
    }

    // Este es solo para la carga de datos iniciales de empleados
    suspend fun createInit(dto: EmpleadoDTOcreacion) = withContext(Dispatchers.IO) {
        service.saveEmpleado(dto)
    }
}