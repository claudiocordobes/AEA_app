package ar.com.aeacordoba.seguridadelectrica.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ar.com.aeacordoba.seguridadelectrica.data.db.AppDatabase
import ar.com.aeacordoba.seguridadelectrica.data.db.NormaDao
import ar.com.aeacordoba.seguridadelectrica.data.db.NormaEntity
import ar.com.aeacordoba.seguridadelectrica.data.model.NormativaSeed
import kotlinx.coroutines.flow.Flow

class NormativaRepository(
    private val context: Context,
    private val dao: NormaDao
) {

    fun observarTodas(): Flow<List<NormaEntity>> = dao.observarTodas()

    fun observarFavoritas(): Flow<List<NormaEntity>> = dao.observarFavoritas()

    fun observarCategorias(): Flow<List<String>> = dao.observarCategorias()

    fun buscar(consulta: String): Flow<List<NormaEntity>> = dao.buscar(consulta.trim())

    suspend fun obtenerPorId(id: String): NormaEntity? = dao.obtenerPorId(id)

    suspend fun alternarFavorito(norma: NormaEntity) {
        dao.marcarFavorito(norma.id, !norma.esFavorito)
    }

    /** Carga la normativa semilla desde assets/normativa.json la primera vez que se abre la app. */
    suspend fun sembrarSiEsNecesario() {
        if (dao.contar() > 0) return

        val json = context.assets.open("normativa.json").bufferedReader(Charsets.UTF_8).use { it.readText() }
        val tipoLista = object : TypeToken<List<NormativaSeed>>() {}.type
        val semillas: List<NormativaSeed> = Gson().fromJson(json, tipoLista)

        val entidades = semillas.map { semilla ->
            NormaEntity(
                id = semilla.id,
                categoria = semilla.categoria,
                titulo = semilla.titulo,
                resumen = semilla.resumen,
                articulosJson = Gson().toJson(semilla.articulos),
                vigencia = semilla.vigencia,
                fuenteTitulo = semilla.fuenteTitulo,
                fuenteUrl = semilla.fuenteUrl
            )
        }
        dao.insertarTodas(entidades)
    }

    companion object {
        @Volatile
        private var instancia: NormativaRepository? = null

        fun obtener(context: Context): NormativaRepository {
            return instancia ?: synchronized(this) {
                instancia ?: NormativaRepository(
                    context.applicationContext,
                    AppDatabase.obtener(context).normaDao()
                ).also { instancia = it }
            }
        }
    }
}

fun NormaEntity.articulos(): List<String> {
    val tipoLista = object : TypeToken<List<String>>() {}.type
    return Gson().fromJson(articulosJson, tipoLista)
}
