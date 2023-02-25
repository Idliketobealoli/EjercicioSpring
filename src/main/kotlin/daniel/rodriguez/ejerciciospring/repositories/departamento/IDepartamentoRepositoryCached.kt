package daniel.rodriguez.ejerciciospring.repositories.departamento

import daniel.rodriguez.ejerciciospring.models.Departamento
import daniel.rodriguez.ejerciciospring.repositories.ICRUDRepository
import java.util.*

interface IDepartamentoRepositoryCached : ICRUDRepository<Departamento, UUID>