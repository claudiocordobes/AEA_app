package ar.com.aeacordoba.seguridadelectrica.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ar.com.aeacordoba.seguridadelectrica.data.db.NormaEntity
import ar.com.aeacordoba.seguridadelectrica.data.repository.articulos
import ar.com.aeacordoba.seguridadelectrica.ui.NormativaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    id: String,
    viewModel: NormativaViewModel,
    onVolver: () -> Unit
) {
    var norma by remember { mutableStateOf<NormaEntity?>(null) }
    val contexto = LocalContext.current

    LaunchedEffect(id) {
        norma = viewModel.obtenerPorId(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(norma?.categoria ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    norma?.let { actual ->
                        IconButton(onClick = {
                            viewModel.alternarFavorito(actual)
                            norma = actual.copy(esFavorito = !actual.esFavorito)
                        }) {
                            Icon(
                                imageVector = if (actual.esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Marcar como favorito"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingInterno ->
        val actual = norma
        if (actual == null) {
            CircularProgressIndicator(modifier = Modifier.padding(paddingInterno).size(32.dp))
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AssistChip(onClick = {}, label = { Text(actual.categoria) })
            Text(
                text = actual.titulo,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
            )
            Text(text = actual.resumen, style = MaterialTheme.typography.bodyLarge)

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text(text = "Puntos clave", style = MaterialTheme.typography.titleMedium)
            Column(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                actual.articulos().forEach { punto ->
                    Row {
                        Text(text = "•  ", style = MaterialTheme.typography.bodyMedium)
                        Text(text = punto, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text(text = "Vigencia", style = MaterialTheme.typography.titleMedium)
            Text(
                text = actual.vigencia,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            Text(text = "Fuente oficial", style = MaterialTheme.typography.titleMedium)
            Text(
                text = actual.fuenteTitulo,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
            Button(onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(actual.fuenteUrl))
                contexto.startActivity(intent)
            }) {
                Icon(Icons.Filled.OpenInNew, contentDescription = null)
                Text(text = "  Ver texto oficial completo")
            }

            Text(
                text = "Esta app resume la normativa con fines informativos. Ante cualquier trámite o duda legal, consultá siempre el texto oficial vigente y a un profesional matriculado.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
    }
}
