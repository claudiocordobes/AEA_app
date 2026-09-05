package ar.com.aeacordoba.seguridadelectrica.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ar.com.aeacordoba.seguridadelectrica.data.db.NormaEntity
import ar.com.aeacordoba.seguridadelectrica.data.repository.NormativaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NormativaViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = NormativaRepository.obtener(application)

    private val _consulta = MutableStateFlow("")
    val consulta: StateFlow<String> = _consulta

    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)
    val categoriaSeleccionada: StateFlow<String?> = _categoriaSeleccionada

    val categorias: StateFlow<List<String>> = repositorio.observarCategorias()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoritas: StateFlow<List<NormaEntity>> = repositorio.observarFavoritas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val resultados: StateFlow<List<NormaEntity>> = combine(
        _consulta,
        _categoriaSeleccionada
    ) { consulta, categoria -> consulta to categoria }
        .flatMapLatest { (consulta, categoria) ->
            val base = if (consulta.isBlank()) repositorio.observarTodas() else repositorio.buscar(consulta)
            base.map { lista -> if (categoria == null) lista else lista.filter { it.categoria == categoria } }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repositorio.sembrarSiEsNecesario()
        }
    }

    fun actualizarConsulta(texto: String) {
        _consulta.value = texto
    }

    fun seleccionarCategoria(categoria: String?) {
        _categoriaSeleccionada.value = if (_categoriaSeleccionada.value == categoria) null else categoria
    }

    fun alternarFavorito(norma: NormaEntity) {
        viewModelScope.launch { repositorio.alternarFavorito(norma) }
    }

    suspend fun obtenerPorId(id: String): NormaEntity? = repositorio.obtenerPorId(id)
}
