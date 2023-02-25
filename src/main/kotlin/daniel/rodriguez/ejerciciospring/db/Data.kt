package daniel.rodriguez.ejerciciospring.db

import daniel.rodriguez.ejerciciospring.models.Departamento
import daniel.rodriguez.ejerciciospring.models.Empleado
import daniel.rodriguez.ejerciciospring.models.Role
import daniel.rodriguez.ejerciciospring.models.User
import java.util.*

fun users() = listOf(
    User(
        uuid = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea83aa"),
        username = "loli", email = "loli@gmail.com",
        password = "loli1707", role = Role.ADMIN
    ),
    User(
        uuid = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0000"),
        username = "user", email = "user@gmail.com",
        password = "1234567", role = Role.EMPLEADO
    )
)

fun empleados() = listOf(
    Empleado(
        uuid = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0001"),
        nombre = "Nombre Generico", email = "empleado1@gmail.com", avatar = "",
        departamentoId = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0003")
    ),
    Empleado(
        uuid = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0002"),
        nombre = "Juan Manodeobra", email = "empleado2@gmail.com", avatar = "",
        departamentoId = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0003")
    )
)

fun departamentos() = listOf(
    Departamento(
        uuid = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0003"),
        nombre = "Departamento falso", presupuesto = 69_420.69
    ),
    Departamento(
        uuid = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea0004"),
        nombre = "Departamento falso 2", presupuesto = 15.0
    )
)