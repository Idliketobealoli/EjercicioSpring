package daniel.rodriguez.ejerciciospring.repositories.departamento

import daniel.rodriguez.ejerciciospring.models.Departamento
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class DepartamentoRepositoryCached
@Autowired constructor(
    private val repo: DepartamentoRepository
) : IDepartamentoRepositoryCached {
    override suspend fun findAll(): Flow<Departamento> = withContext(Dispatchers.IO) {
        repo.findAll()
    }

    @Cacheable("departments")
    override suspend fun findById(id: Long): Departamento? = withContext(Dispatchers.IO) {
        repo.findById(id)
    }

    override suspend fun findByUUID(id: UUID): Departamento? = withContext(Dispatchers.IO) {
        repo.findByUuid(id).firstOrNull()
    }

    @CachePut("departments")
    override suspend fun save(entity: Departamento): Departamento = withContext(Dispatchers.IO) {
        repo.save(entity)
    }

    @CacheEvict("departments")
    override suspend fun delete(id: Long): Departamento? = withContext(Dispatchers.IO) {
        val res = repo.findById(id) ?: return@withContext null
        repo.deleteById(id)
        res
    }
}