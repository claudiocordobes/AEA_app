package ar.com.aeacordoba.seguridadelectrica.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ar.com.aeacordoba.seguridadelectrica.ui.NormativaViewModel
import ar.com.aeacordoba.seguridadelectrica.ui.components.NormaCard

@Composable
fun FavoritosScreen(
    viewModel: NormativaViewModel,
    onNormaClick: (String) -> Unit
) {
    val favoritas by viewModel.favoritas.collectAsState()

    if (favoritas.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
            Text(
                text = "Todavía no marcaste normas como favoritas.\nTocá el corazón en cualquier norma para guardarla acá.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(favoritas, key = { it.id }) { norma ->
                NormaCard(
                    norma = norma,
                    onClick = { onNormaClick(norma.id) },
                    onFavoritoClick = { viewModel.alternarFavorito(norma) }
                )
            }
        }
    }
}
