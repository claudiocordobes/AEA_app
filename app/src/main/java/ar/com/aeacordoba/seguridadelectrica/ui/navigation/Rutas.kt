package ar.com.aeacordoba.seguridadelectrica.ui.navigation

sealed class Ruta(val ruta: String) {
    data object Buscar : Ruta("buscar")
    data object Favoritos : Ruta("favoritos")
    data object Detalle : Ruta("detalle/{id}") {
        fun crear(id: String) = "detalle/$id"
    }
}
