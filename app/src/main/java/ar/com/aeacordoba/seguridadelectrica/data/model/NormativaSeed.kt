package ar.com.aeacordoba.seguridadelectrica.data.model

data class NormativaSeed(
    val id: String,
    val categoria: String,
    val titulo: String,
    val resumen: String,
    val articulos: List<String>,
    val vigencia: String,
    val fuenteTitulo: String,
    val fuenteUrl: String
)
