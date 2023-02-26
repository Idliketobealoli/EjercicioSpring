package daniel.rodriguez.ejerciciospring.validators

import daniel.rodriguez.ejerciciospring.dto.EmpleadoDTOcreacion
import daniel.rodriguez.ejerciciospring.exception.EmpleadoExceptionBadRequest
import daniel.rodriguez.ejerciciospring.exception.UserExceptionBadRequest

fun EmpleadoDTOcreacion.validate(): EmpleadoDTOcreacion {
    if (this.nombre.isBlank())
        throw EmpleadoExceptionBadRequest("Nombre cannot be blank.")
    else if (this.email.isBlank())
        throw UserExceptionBadRequest("Email cannot be blank.")
    else if (!this.email.matches(Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")))
        throw UserExceptionBadRequest("Invalid email.")
    else return this
}