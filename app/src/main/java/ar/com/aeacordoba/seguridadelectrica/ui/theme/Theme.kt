package ar.com.aeacordoba.seguridadelectrica.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AzulPrimario = Color(0xFF0D47A1)
private val AmbarAcento = Color(0xFFFFC107)

private val EsquemaClaro = lightColorScheme(
    primary = AzulPrimario,
    secondary = AmbarAcento
)

private val EsquemaOscuro = darkColorScheme(
    primary = Color(0xFF90CAF9),
    secondary = AmbarAcento
)

@Composable
fun AEASeguridadElectricaTheme(
    esOscuro: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val esquema = if (esOscuro) EsquemaOscuro else EsquemaClaro
    MaterialTheme(colorScheme = esquema, content = content)
}
