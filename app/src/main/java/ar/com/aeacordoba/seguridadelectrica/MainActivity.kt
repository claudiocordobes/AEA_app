package ar.com.aeacordoba.seguridadelectrica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ar.com.aeacordoba.seguridadelectrica.ui.NormativaViewModel
import ar.com.aeacordoba.seguridadelectrica.ui.navigation.Ruta
import ar.com.aeacordoba.seguridadelectrica.ui.screens.BuscarScreen
import ar.com.aeacordoba.seguridadelectrica.ui.screens.DetalleScreen
import ar.com.aeacordoba.seguridadelectrica.ui.screens.FavoritosScreen
import ar.com.aeacordoba.seguridadelectrica.ui.theme.AEASeguridadElectricaTheme

class MainActivity : ComponentActivity() {

    private val viewModel: NormativaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AEASeguridadElectricaTheme {
                AppPrincipal(viewModel)
            }
        }
    }
}

private data class ItemNav(val ruta: String, val etiqueta: String, val icono: androidx.compose.ui.graphics.vector.ImageVector)

@androidx.compose.runtime.Composable
private fun AppPrincipal(viewModel: NormativaViewModel) {
    val navController = rememberNavController()
    val items = listOf(
        ItemNav(Ruta.Buscar.ruta, "Buscar", Icons.Filled.Search),
        ItemNav(Ruta.Favoritos.ruta, "Favoritos", Icons.Filled.Favorite)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val destinoActual = backStackEntry?.destination
                items.forEach { item ->
                    NavigationBarItem(
                        selected = destinoActual?.hierarchy?.any { it.route == item.ruta } == true,
                        onClick = {
                            navController.navigate(item.ruta) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icono, contentDescription = item.etiqueta) },
                        label = { Text(item.etiqueta) }
                    )
                }
            }
        }
    ) { paddingInterno ->
        NavHost(
            navController = navController,
            startDestination = Ruta.Buscar.ruta,
            modifier = Modifier.padding(paddingInterno)
        ) {
            composable(Ruta.Buscar.ruta) {
                BuscarScreen(
                    viewModel = viewModel,
                    onNormaClick = { id -> navController.navigate(Ruta.Detalle.crear(id)) }
                )
            }
            composable(Ruta.Favoritos.ruta) {
                FavoritosScreen(
                    viewModel = viewModel,
                    onNormaClick = { id -> navController.navigate(Ruta.Detalle.crear(id)) }
                )
            }
            composable("detalle/{id}") { entrada ->
                val id = entrada.arguments?.getString("id") ?: return@composable
                DetalleScreen(
                    id = id,
                    viewModel = viewModel,
                    onVolver = { navController.popBackStack() }
                )
            }
        }
    }
}
