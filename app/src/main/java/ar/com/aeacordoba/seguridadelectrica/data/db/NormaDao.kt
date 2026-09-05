package ar.com.aeacordoba.seguridadelectrica.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NormaDao {

    @Query("SELECT * FROM normas ORDER BY categoria, titulo")
    fun observarTodas(): Flow<List<NormaEntity>>

    @Query("SELECT * FROM normas WHERE esFavorito = 1 ORDER BY titulo")
    fun observarFavoritas(): Flow<List<NormaEntity>>

    @Query(
        """
        SELECT * FROM normas
        WHERE titulo LIKE '%' || :consulta || '%'
           OR resumen LIKE '%' || :consulta || '%'
           OR articulosJson LIKE '%' || :consulta || '%'
           OR categoria LIKE '%' || :consulta || '%'
        ORDER BY titulo
        """
    )
    fun buscar(consulta: String): Flow<List<NormaEntity>>

    @Query("SELECT * FROM normas WHERE id = :id")
    suspend fun obtenerPorId(id: String): NormaEntity?

    @Query("SELECT DISTINCT categoria FROM normas ORDER BY categoria")
    fun observarCategorias(): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM normas")
    suspend fun contar(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodas(normas: List<NormaEntity>)

    @Update
    suspend fun actualizar(norma: NormaEntity)

    @Query("UPDATE normas SET esFavorito = :esFavorito WHERE id = :id")
    suspend fun marcarFavorito(id: String, esFavorito: Boolean)
}
