package daniel.rodriguez.ejerciciospring.repositories.empleado

import daniel.rodriguez.ejerciciospring.models.Empleado
import daniel.rodriguez.ejerciciospring.repositories.ICRUDRepository
import java.util.*

interface IEmpleadoRepositoryCached : ICRUDRepository<Empleado, UUID>