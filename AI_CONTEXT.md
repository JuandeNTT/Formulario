# Contexto del Proyecto - Formulario Android

Este documento contiene información esencial para que cualquier IA pueda continuar el desarrollo del proyecto manteniendo la coherencia arquitectural y de diseño.

---

## 1. Prompt Inicial del Proyecto

**Objetivo:** Crear un formulario en Android con Jetpack Compose que cumpla las siguientes especificaciones:

```
Hay que realizar esto en este proyecto de jetpack compose. Necesito que crees una 
interfaz amigable con el usuario, que utilices material 3 para temas de diseño y 
cosas que creas que puedan estar bien. De igual forma, utiliza el modelo MVVM. 
Un fichero Dimens para todo el tema de tamaños y no quiero que hardcodees el texto. 
Respeta el Java en el que estamos para que no dé muchos fallos.

Campos: título, descripción, categoría, prioridad (1–5), email

Validaciones: título 5–60 chars, descripción 20–500, email válido, prioridad 1–5

Botón deshabilitado si inválido; estado "Enviando…" y sin doble envío
```

---

## 2. Requisitos Funcionales

### Campos del Formulario
1. **Título** (TextField)
   - Validación: 5-60 caracteres
   - Single line

2. **Descripción** (TextField multiline)
   - Validación: 20-500 caracteres
   - 5 líneas visibles

3. **Categoría** (Dropdown)
   - Opciones: Trabajo, Personal, Urgente, Otros

4. **Prioridad** (Slider)
   - Rango: 1-5
   - Indicador visual

5. **Email** (TextField con validación)
   - Validación: formato email válido
   - Keyboard type EMAIL_ADDRESS

### Comportamiento de la UI
- **Botón Submit**: Deshabilitado hasta que todos los campos sean válidos
- **Estado "Enviando..."**: Mostrar durante el envío, deshabilitar interacción
- **Prevención de doble envío**: El botón se deshabilita durante el proceso
- **Mensajes de validación**: Feedback en tiempo real bajo cada campo
- **Mensaje de éxito**: Confirmación visual tras envío exitoso

---

## 3. Requisitos Técnicos

### Tecnologías y Patrones
- **Framework**: Jetpack Compose
- **Design System**: Material 3 (Material Design 3)
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Compatibilidad**: Java 11 (respetando limitaciones del SDK)
- **Gestión de estado**: ViewModel + StateFlow/MutableState
- **Internacionalización**: Todos los textos en `strings.xml`
- **Dimensiones**: Centralizadas en `Dimens.kt`

### Gestión de Credenciales
- **Archivo `.env`**: Para credenciales de Supabase (NO se sube a Git)
- **BuildConfig**: Lee `.env` y expone variables en tiempo de compilación
- **Seguridad**: `.env` incluido en `.gitignore`

---

## 4. Arquitectura Implementada

### Estructura de Carpetas

```
app/src/main/java/com/example/formulario/
├── model/
│   └── FormData.kt                    # Data class del formulario
├── viewmodel/
│   ├── FormViewModel.kt               # ViewModel principal
│   └── RequestsViewModel.kt           # ViewModel para listado (WIP)
├── ui/
│   ├── components/
│   │   ├── FormTextField.kt           # TextField reutilizable
│   │   ├── EmailTextField.kt          # TextField específico para email
│   │   ├── CategoryDropdown.kt        # Dropdown de categorías
│   │   ├── PrioritySlider.kt          # Slider con indicador visual
│   │   ├── SubmitButton.kt            # Botón con estados
│   │   └── SuccessMessage.kt          # Mensaje de confirmación
│   ├── screens/
│   │   ├── FormScreen.kt              # Pantalla principal del formulario
│   │   └── RequestsScreen.kt          # Pantalla de listado (WIP)
│   └── theme/
│       ├── Color.kt                   # Paleta de colores Material 3
│       ├── Theme.kt                   # Configuración del tema
│       ├── Type.kt                    # Tipografía
│       └── Dimens.kt                  # Dimensiones centralizadas
├── data/                               # (WIP - Integración Supabase)
│   ├── remote/
│   │   └── SupabaseClient.kt          # Cliente singleton de Supabase
│   ├── model/
│   │   ├── NetworkResult.kt           # Sealed class para estados HTTP
│   │   └── FormEntity.kt              # Modelo de respuesta de Supabase
│   └── repository/
│       └── FormRepository.kt          # Patrón Repository
├── navigation/                         # (WIP)
│   └── Navigation.kt                  # Configuración NavHost
└── MainActivity.kt                     # Activity principal con Compose
```

### Decisiones de Diseño

#### 1. **Componentización**
- Cada elemento de UI es un componente reutilizable
- Parámetros configurables con valores por defecto
- Modifiers externos para flexibilidad

#### 2. **Gestión de Estado**
- ViewModel mantiene el estado con `MutableStateFlow`
- UI observa cambios con `collectAsState()`
- Validación reactiva en tiempo real

#### 3. **Validaciones**
- Lógica centralizada en el ViewModel
- Funciones de validación puras y testeables
- Feedback inmediato al usuario

#### 4. **Material 3**
- Uso de componentes Material 3 nativos
- Esquema de colores dinámico
- Elevaciones y shapes según guías de diseño

---

## 5. Integración con Supabase (WIP)

### Backend
- **Plataforma**: Supabase (PostgreSQL + REST API)
- **Tabla**: `form_requests`
- **Campos**:
  - `id` (UUID, PK, auto-generado)
  - `title` (TEXT)
  - `description` (TEXT)
  - `category` (TEXT)
  - `priority` (INTEGER, 1-5)
  - `email` (TEXT)
  - `created_at` (TIMESTAMP)

### Seguridad
- **RLS (Row Level Security)**: Habilitado
- **Políticas**:
  - INSERT: Permitido para todos
  - SELECT: Permitido para todos

### Cliente Android
- **Librería**: Supabase Kotlin Client + Ktor
- **Patrón**: Repository Pattern
- **Manejo de errores**: NetworkResult sealed class
- **Credenciales**: Archivo `.env` (no versionado)

---

## 6. Componentes Reutilizables Creados

### FormTextField
```kotlin
@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String?,
    isError: Boolean,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    maxLines: Int = 1
)
```
**Uso**: Campos de texto genéricos con validación integrada.

### EmailTextField
```kotlin
@Composable
fun EmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String?,
    isError: Boolean,
    modifier: Modifier = Modifier
)
```
**Uso**: Campo específico para email con keyboard type y validación.

### CategoryDropdown
```kotlin
@Composable
fun CategoryDropdown(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    categories: List<String>,
    modifier: Modifier = Modifier
)
```
**Uso**: Selector desplegable Material 3.

### PrioritySlider
```kotlin
@Composable
fun PrioritySlider(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
)
```
**Uso**: Slider 1-5 con indicador visual de prioridad.

### SubmitButton
```kotlin
@Composable
fun SubmitButton(
    onClick: () -> Unit,
    enabled: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier
)
```
**Uso**: Botón con estados (normal, disabled, loading).

### SuccessMessage
```kotlin
@Composable
fun SuccessMessage(
    message: String,
    onDismiss: () -> Unit
)
```
**Uso**: Card de confirmación con animación y auto-dismiss.

---

## 7. Strings Definidos (strings.xml)

Todos los textos están externalizados:
- `app_name`: Nombre de la app
- `form_title`: Título del formulario
- `label_title`, `label_description`, `label_category`, etc.: Labels
- `error_title_short`, `error_title_long`, etc.: Mensajes de validación
- `hint_*`: Placeholders
- `button_submit`, `button_submitting`: Textos del botón
- `success_message`: Mensaje de éxito
- `category_*`: Opciones de categoría

---

## 8. Dimensiones Centralizadas (Dimens.kt)

```kotlin
object Dimens {
    val paddingSmall = 8.dp
    val paddingMedium = 16.dp
    val paddingLarge = 24.dp
    val buttonHeight = 56.dp
    val cornerRadius = 12.dp
    val iconSize = 24.dp
    val spacerSmall = 8.dp
    val spacerMedium = 16.dp
    val spacerLarge = 24.dp
}
```

---

## 9. Prompts Útiles para Continuar el Proyecto

### Para añadir nueva funcionalidad:
```
Necesito añadir [funcionalidad] al proyecto Formulario. 
Respeta la arquitectura MVVM existente, usa Material 3, 
no hardcodees textos (usa strings.xml), y mantén las 
dimensiones en Dimens.kt. El proyecto usa Java 11.
```

### Para integrar con Supabase:
```
Completa la integración con Supabase en el proyecto Formulario. 
Las credenciales están en el archivo .env. Implementa el patrón 
Repository, maneja errores con NetworkResult sealed class, 
y actualiza FormViewModel para enviar datos a la tabla form_requests.
```

### Para añadir navegación:
```
Implementa Navigation Compose en el proyecto Formulario. 
Crea una pantalla de listado (RequestsScreen) que muestre 
los registros de Supabase. Mantén la arquitectura MVVM y 
Material 3 existentes.
```

### Para añadir tests:
```
Crea tests unitarios para FormViewModel en el proyecto Formulario. 
Testea las validaciones de campos (título 5-60 chars, descripción 
20-500, email válido, prioridad 1-5) y el flujo de submit.
```

### Para mejorar accesibilidad:
```
Mejora la accesibilidad del formulario en el proyecto Formulario. 
Añade contentDescription, semantics, y soporte para TalkBack 
en todos los componentes.
```

---

## 10. Configuración del Entorno

### Dependencias Clave (build.gradle.kts)
```kotlin
// Compose BOM
implementation(platform("androidx.compose:compose-bom:2024.04.01"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.ui:ui-tooling-preview")

// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

// Navigation (WIP)
implementation("androidx.navigation:navigation-compose:2.7.7")

// Supabase (WIP)
implementation("io.github.jan-tennert.supabase:postgrest-kt:2.0.0")
implementation("io.ktor:ktor-client-android:2.3.7")
```

### Configuración de BuildConfig
El archivo `app/build.gradle.kts` está configurado para leer el archivo `.env` 
y exponer las credenciales en `BuildConfig` de forma segura.

---

## 11. Contacto y Notas

**Versión del documento**: 1.0  
**Última actualización**: 27/04/2026  
**Compatibilidad**: Android SDK 24+ (Android 7.0+)  
**Lenguaje**: Kotlin con compatibilidad Java 11

---

## 12. Tips para IAs que Continúen Este Proyecto

1. **Siempre lee los archivos existentes** antes de crear nuevos componentes
2. **Respeta la estructura de carpetas** establecida
3. **No hardcodees texto**: usa `stringResource(R.string.xxx)`
4. **No hardcodees dimensiones**: usa `Dimens.xxx`
5. **Mantén la coherencia visual**: usa colores del theme (MaterialTheme.colorScheme)
6. **Sigue MVVM estricto**: no lógica de negocio en Composables
7. **Componentiza**: si un elemento se repite, crear componente reutilizable
8. **Valida en ViewModel**: no validaciones en UI
9. **Usa Material 3**: aprovechar componentes nativos
10. **Manejo de errores**: siempre mostrar feedback al usuario

---