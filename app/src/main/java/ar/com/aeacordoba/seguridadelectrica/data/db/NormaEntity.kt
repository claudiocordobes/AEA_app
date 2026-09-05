package ar.com.aeacordoba.seguridadelectrica.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "normas")
data class NormaEntity(
    @PrimaryKey val id: String,
    val categoria: String,
    val titulo: String,
    val resumen: String,
    val articulosJson: String,
    val vigencia: String,
    val fuenteTitulo: String,
    val fuenteUrl: String,
    val esFavorito: Boolean = false
)
