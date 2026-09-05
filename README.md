# AEA Seguridad Eléctrica Córdoba

App Android (Kotlin + Jetpack Compose) para consultar, buscar y guardar como favorita
la normativa de seguridad eléctrica vigente en la Provincia de Córdoba, Argentina:

- **Ley N° 10281** – Régimen de Seguridad Eléctrica de la Provincia de Córdoba.
- **Decreto N° 1022/2015** – Reglamentario de la Ley 10281.
- **Resoluciones Generales del ERSeP** – Registro de Instaladores Electricistas Habilitados.
- **AEA 90364** – Reglamentación técnica de la Asociación Electrotécnica Argentina, base
  técnica que toma ERSeP para reglamentar la Ley.
- **Certificado de Instalación Eléctrica Apta (CIEA)** – trámite exigido por la Ley.

## Cómo funciona

- Los datos base están en `app/src/main/assets/normativa.json` y se cargan una sola vez
  en una base de datos local (Room/SQLite) al abrir la app por primera vez.
- Pantalla **Buscar**: búsqueda por texto libre (título, resumen, puntos clave, categoría)
  y filtro por categoría (chips).
- Pantalla **Detalle**: resumen, puntos clave, vigencia y botón para abrir el **texto
  oficial completo** en el navegador (fuente citada por cada norma).
- Pantalla **Favoritos**: normas marcadas con el ícono de corazón, persistidas en el
  dispositivo (sin necesidad de conexión a internet).
- Funciona 100% offline luego de la primera apertura.

## ⚠️ Importante sobre el contenido legal

Los resúmenes y "puntos clave" de cada norma fueron redactados a partir de fuentes
oficiales públicas (Legislatura de Córdoba, ERSeP, AEA) citadas en cada ficha, pero
**son un resumen informativo, no el texto legal completo ni un reemplazo de él**.
Para cualquier trámite, certificación o consulta con validez legal, verificá siempre
el texto oficial vigente (el botón "Ver texto oficial completo" en cada ficha enlaza
a la fuente) y consultá a un Instalador Electricista Habilitado o profesional
matriculado ante el ERSeP.

Fuentes utilizadas para el contenido semilla:
- Ley 10281 (Legislatura de Córdoba): https://relevandopeligros.org/front/downloads/biblioteca/ley10281/Ley%20de%20Seguridad%20El%C3%A9ctrica.pdf
- ERSeP – Seguridad Eléctrica: https://ersep.cba.gov.ar/seguridad-electrica/
- AEA – Reglamentaciones 90364: https://aea.org.ar/wp-content/uploads/2017/10/90364-1-1.pdf

## Cómo agregar o corregir normativa

Editá `app/src/main/assets/normativa.json`. Cada entrada tiene:

```json
{
  "id": "identificador-unico",
  "categoria": "Nombre de la categoría (se usa como filtro)",
  "titulo": "Título de la norma",
  "resumen": "Resumen breve",
  "articulos": ["Punto clave 1", "Punto clave 2"],
  "vigencia": "Fecha de sanción / vigencia",
  "fuenteTitulo": "Nombre de la fuente oficial",
  "fuenteUrl": "https://enlace-al-texto-oficial"
}
```

> Nota: la app sólo siembra la base de datos si está vacía. Si ya instalaste la app y
> modificás el JSON, desinstalá y volvé a instalar (o borrá los datos de la app) para
> ver los cambios.

## Compilar el proyecto

Requiere Android Studio (Koala o superior) o el Android SDK + JDK 17 instalados.

```bash
./gradlew assembleDebug
```

El APK generado queda en `app/build/outputs/apk/debug/app-debug.apk`.

### Stack técnico

- Kotlin, Jetpack Compose (Material 3), Navigation Compose
- Room (SQLite) para persistencia local y favoritos
- Gson para parsear el JSON semilla
- `minSdk` 26 (Android 8.0+), `compileSdk`/`targetSdk` 34
