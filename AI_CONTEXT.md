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

## 2. Arquitectura Implementada: Clean Architecture (Data-Domain-UI)

### Principios de Clean Architecture
1. **Separación de responsabilidades** por capas
2. **Independencia de frameworks** en la capa de dominio
3. **Testabilidad** mediante casos de uso e interfaces
4. **Inversión de dependencias** (las capas externas dependen de las internas)
5. **Inyección de dependencias** con Hilt en todas las capas

### Estructura Completa del Proyecto

```
app/src/main/java/com/example/formulario/
├── data/                                    # CAPA DE DATOS
│   ├── model/
│   │   ├── FormEntity.kt                   # Entidad para Supabase
│   │   └── NetworkResult.kt                # Sealed class para resultados
│   ├── remote/
│   │   └── SupabaseClient.kt               # Cliente singleton de Supabase
│   └── repository/
│       └── FormRepositoryImpl.kt           # Implementación del repositorio
│
├── domain/                                  # CAPA DE DOMINIO (LÓGICA DE NEGOCIO)
│   ├── model/
│   │   ├── FormData.kt                     # Modelo de dominio para formulario
│   │   └── FormRequest.kt                  # Modelo de dominio para solicitudes
│   ├── repository/
│   │   └── IFormRepository.kt              # Interfaz del repositorio
│   └── usecase/
│       ├── ValidateFormUseCase.kt          # Caso de uso: Validación
│       ├── SubmitFormUseCase.kt            # Caso de uso: Envío
│       └── GetAllRequestsUseCase.kt        # Caso de uso: Obtener solicitudes
│
├── ui/                                      # CAPA DE PRESENTACIÓN
│   ├── viewmodel/
│   │   ├── FormViewModel.kt                # ViewModel del formulario
│   │   └── RequestsViewModel.kt            # ViewModel de solicitudes
│   ├── screens/
│   │   ├── FormScreen.kt                   # Pantalla del formulario
│   │   └── RequestsScreen.kt               # Pantalla de listado
│   ├── components/
│   │   ├── FormTextField.kt                # TextField reutilizable
│   │   ├── EmailTextField.kt               # TextField específico para email
│   │   ├── CategoryDropdown.kt             # Dropdown de categorías
│   │   ├── PrioritySlider.kt               # Slider con indicador visual
│   │   ├── SubmitButton.kt                 # Botón con estados
│   │   ├── SuccessMessage.kt               # Mensaje de confirmación
│   │   ├── ErrorDialog.kt                  # Diálogo de error
│   │   └── RequestItem.kt                  # Item de solicitud
│   └── theme/
│       ├── Color.kt                        # Paleta de colores Material 3
│       ├── Theme.kt                        # Configuración del tema
│       ├── Type.kt                         # Tipografía
│       └── Dimens.kt                       # Dimensiones centralizadas
│
├── di/                                      # INYECCIÓN DE DEPENDENCIAS (HILT)
│   ├── AppModule.kt                        # Módulo principal
│   └── RepositoryModule.kt                 # Módulo de repositorios
│
├── navigation/
│   └── Navigation.kt                       # Configuración NavHost
├── utils/
│   └── DateUtils.kt                        # Utilidades para fechas
├── FormularioApplication.kt                # Application class con @HiltAndroidApp
└── MainActivity.kt                         # Actividad principal con @AndroidEntryPoint
```

### Descripción de las Capas

#### **1. CAPA DE DATOS (Data Layer)**
**Responsabilidad**: Gestión de fuentes de datos externas (API, base de datos, caché).

**Componentes clave**:
- `FormEntity`: Modelo de datos para la estructura en Supabase
- `NetworkResult`: Sealed class (Success, Error, Loading) para resultados de red
- `SupabaseClient`: Cliente singleton para interactuar con la API
- `FormRepositoryImpl`: Implementación concreta del repositorio

**Inyección de dependencias**:
```kotlin
@Singleton
class FormRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : IFormRepository
```

#### **2. CAPA DE DOMINIO (Domain Layer)**
**Responsabilidad**: Lógica de negocio pura, independiente de frameworks.

**Modelos de dominio**:
- `FormData`: Representa los datos del formulario
- `FormRequest`: Representa una solicitud guardada

**Interfaz de repositorio**:
- `IFormRepository`: Contrato que define operaciones de datos

**Casos de uso**:
- `ValidateFormUseCase`: Valida campos según reglas de negocio
- `SubmitFormUseCase`: Envía formulario a través del repositorio
- `GetAllRequestsUseCase`: Obtiene todas las solicitudes

**Inyección de dependencias**:
```kotlin
class SubmitFormUseCase @Inject constructor(
    private val repository: IFormRepository
)
```

#### **3. CAPA DE PRESENTACIÓN (UI Layer)**
**Responsabilidad**: Interfaz de usuario y gestión de estado.

**ViewModels inyectados con Hilt**:
```kotlin
@HiltViewModel
class FormViewModel @Inject constructor(
    private val submitFormUseCase: SubmitFormUseCase,
    private val validateFormUseCase: ValidateFormUseCase
) : ViewModel()
```

**Uso en Composables**:
```kotlin
@Composable
fun FormScreen(
    navController: NavHostController,
    viewModel: FormViewModel = hiltViewModel()
)
```

#### **4. INYECCIÓN DE DEPENDENCIAS (Hilt)**

**AppModule.kt**:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient = SupabaseClient
    
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
```

**RepositoryModule.kt**:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindFormRepository(
        formRepositoryImpl: FormRepositoryImpl
    ): IFormRepository
}
```

---

## 3. Flujo de Datos Completo

```
UI (FormScreen)
    ↓ Usuario hace clic en "Enviar"
    
ViewModel (FormViewModel - @HiltViewModel)
    ↓ viewModel.submitForm()
    ↓ Invoca use case inyectado
    
Use Case (SubmitFormUseCase - @Inject)
    ↓ invoke(formData)
    ↓ Usa repositorio inyectado
    
Repository Interface (IFormRepository)
    ↓ Define contrato
    
Repository Implementation (FormRepositoryImpl - @Inject)
    ↓ submitForm(formData)
    ↓ Usa cliente inyectado
    
Data Source (SupabaseClient - @Singleton)
    ↓ Realiza petición HTTP
    ↓ Retorna NetworkResult
    
← Respuesta fluye de vuelta por todas las capas
    
UI actualiza estado:
    - Success → SuccessMessage
    - Error → ErrorDialog
    - Loading → Indicador de progreso
```

---

## 4. Reglas de Validación

### Título
- **Mínimo**: 5 caracteres
- **Máximo**: 60 caracteres
- **Mensaje**: "El título debe tener al menos 5 caracteres"

### Descripción
- **Mínimo**: 20 caracteres
- **Máximo**: 500 caracteres
- **Mensaje**: "La descripción debe tener al menos 20 caracteres"

### Email
- **Patrón**: Regex para validar formato email
- **Mensaje**: "Ingrese un email válido"

### Categoría
- **Opciones**: Trabajo, Personal, Urgente, Otros, Consulta
- **Requerido**: Debe seleccionar una opción
- **Mensaje**: "Debe seleccionar una categoría"

### Prioridad
- **Rango**: 1-5 (validado automáticamente por el slider)
- Sin mensaje de error (el slider previene valores inválidos)

---

## 5. Integración con Supabase

### Configuración del Backend
- **Tabla**: `form_requests`
- **Campos**:
  - `id` (UUID, PK, auto-generado)
  - `title` (TEXT)
  - `description` (TEXT)
  - `category` (TEXT)
  - `priority` (INTEGER)
  - `email` (TEXT)
  - `created_at` (TIMESTAMP)

### Seguridad
- **RLS (Row Level Security)**: Habilitado
- **Políticas**:
  - INSERT: Permitido para todos (`true`)
  - SELECT: Permitido para todos (`true`)

### Credenciales
**Archivo**: `local.properties` (en la raíz del proyecto, NO versionado)
```properties
supabase.url=tu_url_de_supabase
supabase.anon.key=tu_api_key_de_supabase
```

**Lectura en build.gradle.kts**:
```kotlin
android {
    defaultConfig {
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        
        buildConfigField("String", "SUPABASE_URL", "\"${properties.getProperty("supabase.url")}\"")
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"${properties.getProperty("supabase.anon.key")}\"")
    }
}
```

---

## 6. Material Design 3 - Sistema de Diseño

### Colores Principales
```kotlin
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Colores semánticos personalizados
val SuccessGreen = Color(0xFF4CAF50)
val ErrorRed = Color(0xFFF44336)
```

### Dimensiones Centralizadas (Dimens.kt)
```kotlin
object Dimens {
    val paddingSmall = 8.dp
    val paddingMedium = 16.dp
    val paddingLarge = 24.dp
    val paddingExtraLarge = 32.dp
    
    val spacingSmall = 8.dp
    val spacingMedium = 16.dp
    val spacingLarge = 24.dp
    
    val cardElevation = 4.dp
    val cornerRadius = 8.dp
    
    val buttonHeight = 56.dp
    val iconSize = 24.dp
    
    val textFieldMinHeight = 56.dp
    val descriptionFieldHeight = 150.dp
}
```

### Tipografía
- Material 3 Typography por defecto
- Fuente del sistema Android

---

## 7. Componentes UI Reutilizables

### FormTextField
**Propósito**: Campo de texto con validación y contador de caracteres

**Parámetros**:
- `value`, `onValueChange`, `label`
- `errorMessage`, `isError`
- `currentLength`, `maxLength`
- `singleLine`, `maxLines`

**Características**:
- Contador visual de caracteres
- Mensaje de error dinámico
- Colores semánticos (error/normal)

### EmailTextField
**Propósito**: Campo especializado para email

**Características**:
- Keyboard type: EMAIL_ADDRESS
- Validación de formato
- Leading icon de email

### CategoryDropdown
**Propósito**: Selector desplegable Material 3

**Características**:
- ExposedDropdownMenuBox
- Lista de opciones predefinidas
- Validación de selección

### PrioritySlider
**Propósito**: Slider 1-5 con indicador visual

**Características**:
- Pasos discretos (1, 2, 3, 4, 5)
- Indicador de valor actual
- Label descriptivo de prioridad

### SubmitButton
**Propósito**: Botón con estados (enabled/disabled/loading)

**Características**:
- CircularProgressIndicator durante carga
- Deshabilitado cuando `isLoading` o `!enabled`
- Altura fija desde Dimens

### SuccessMessage
**Propósito**: Mensaje de confirmación animado

**Características**:
- Animación de entrada/salida
- Auto-ocultamiento después de 3 segundos
- Icono de check y fondo verde

### ErrorDialog
**Propósito**: Diálogo modal para mostrar errores

**Características**:
- AlertDialog Material 3
- Icono de error
- Botón para cerrar

### RequestItem
**Propósito**: Tarjeta para mostrar una solicitud en el listado

**Características**:
- Card con elevación
- Colores de prioridad (rojo, naranja, verde)
- Fecha formateada con DateUtils

---

## 8. Navegación

### Rutas
```kotlin
sealed class Screen(val route: String) {
    object Form : Screen("form")
    object Requests : Screen("requests")
}
```

### NavHost
```kotlin
NavHost(
    navController = navController,
    startDestination = Screen.Form.route
) {
    composable(Screen.Form.route) {
        FormScreen(navController, hiltViewModel())
    }
    composable(Screen.Requests.route) {
        RequestsScreen(navController, hiltViewModel())
    }
}
```

---

## 9. Dependencias Clave (build.gradle.kts)

```kotlin
// Compose
implementation(platform("androidx.compose:compose-bom:2024.10.01"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.ui:ui-tooling-preview")
implementation("androidx.activity:activity-compose:1.9.3")
implementation("androidx.navigation:navigation-compose:2.8.4")

// Hilt - Inyección de dependencias
implementation("com.google.dagger:hilt-android:2.51.1")
kapt("com.google.dagger:hilt-compiler:2.51.1")
implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

// Supabase
implementation(platform("io.github.jan-tennert.supabase:bom:3.0.3"))
implementation("io.github.jan-tennert.supabase:postgrest-kt")
implementation("io.ktor:ktor-client-android:3.0.2")

// Lifecycle
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
```

---

## 10. Strings Externalizados (strings.xml)

```xml
<resources>
    <string name="app_name">Formulario</string>
    
    <!-- Form Labels -->
    <string name="label_title">Título</string>
    <string name="label_description">Descripción</string>
    <string name="label_category">Categoría</string>
    <string name="label_priority">Prioridad</string>
    <string name="label_email">Email</string>
    
    <!-- Categories -->
    <string name="category_work">Trabajo</string>
    <string name="category_personal">Personal</string>
    <string name="category_urgent">Urgente</string>
    <string name="category_others">Otros</string>
    <string name="category_query">Consulta</string>
    
    <!-- Validation Messages -->
    <string name="error_title_length">El título debe tener al menos 5 caracteres</string>
    <string name="error_description_length">La descripción debe tener al menos 20 caracteres</string>
    <string name="error_email_invalid">Ingrese un email válido</string>
    <string name="error_category_required">Debe seleccionar una categoría</string>
    
    <!-- Actions -->
    <string name="action_submit">Enviar</string>
    <string name="action_sending">Enviando…</string>
    <string name="action_view_requests">Ver Solicitudes</string>
    <string name="action_back_to_form">Volver al Formulario</string>
    
    <!-- Success & Error -->
    <string name="success_form_submitted">Formulario enviado exitosamente</string>
    <string name="error_submit_failed">Error al enviar el formulario</string>
    
    <!-- Screens -->
    <string name="screen_title_form">Nuevo Formulario</string>
    <string name="screen_title_requests">Solicitudes</string>
</resources>
```

---

## 11. Buenas Prácticas Implementadas

### 1. Clean Architecture
- ✅ Separación clara de responsabilidades
- ✅ Independencia de frameworks en dominio
- ✅ Testabilidad mediante interfaces y casos de uso
- ✅ Inversión de dependencias (DIP)

### 2. SOLID Principles
- ✅ **Single Responsibility**: Cada clase tiene una única responsabilidad
- ✅ **Open/Closed**: Extensible mediante interfaces
- ✅ **Liskov Substitution**: Las implementaciones son intercambiables
- ✅ **Interface Segregation**: Interfaces específicas y concisas
- ✅ **Dependency Inversion**: Dependencias invertidas con Hilt

### 3. Jetpack Compose Best Practices
- ✅ Componentes reutilizables y atómicos
- ✅ Estado hoisted (estado elevado a ViewModels)
- ✅ Single source of truth
- ✅ Unidirectional data flow

### 4. Material Design 3
- ✅ Componentes Material 3
- ✅ Color scheme coherente
- ✅ Elevaciones y sombras apropiadas
- ✅ Diseño responsive

### 5. Seguridad
- ✅ Credenciales fuera del control de versiones
- ✅ RLS habilitado en Supabase
- ✅ Validación tanto en cliente como servidor

---

## 12. Testing (Futuro)

### Estructura sugerida para tests:
```
app/src/test/java/com/example/formulario/
├── domain/
│   └── usecase/
│       ├── ValidateFormUseCaseTest.kt
│       ├── SubmitFormUseCaseTest.kt
│       └── GetAllRequestsUseCaseTest.kt
└── data/
    └── repository/
        └── FormRepositoryImplTest.kt
```

### Ventajas de la arquitectura para testing:
- **Domain Layer**: 100% testeable sin mocks de Android
- **Use Cases**: Tests unitarios puros
- **Repository**: Tests con mocks del cliente Supabase
- **ViewModels**: Tests con repositorios mockeados

---

## 13. Próximos Pasos Sugeridos

### Funcionalidades
1. ✅ Formulario con validaciones
2. ✅ Listado de solicitudes
3. ✅ Navegación entre pantallas
4. 🔲 Detalle de solicitud individual
5. 🔲 Edición de solicitudes
6. 🔲 Eliminación de solicitudes
7. 🔲 Filtros y búsqueda

### Mejoras Técnicas
1. 🔲 Tests unitarios
2. 🔲 Tests de integración
3. 🔲 Tests de UI con Compose
4. 🔲 Manejo de errores más granular
5. 🔲 Caché local con Room
6. 🔲 Paginación en el listado
7. 🔲 Pull to refresh
8. 🔲 Modo offline

### UX/UI
1. 🔲 Animaciones de transición
2. 🔲 Skeleton loaders
3. 🔲 Empty states mejorados
4. 🔲 Soporte para modo oscuro personalizado
5. 🔲 Accesibilidad (TalkBack)

---

## 14. Comandos Útiles

### Compilar proyecto
```bash
./gradlew assembleDebug
```

### Limpiar y compilar
```bash
./gradlew clean build
```

### Ver dependencias
```bash
./gradlew app:dependencies
```

---

## 15. Troubleshooting Común

### Error: "BuildConfig does not exist"
**Solución**: Asegúrate de tener el archivo `local.properties` con las credenciales de Supabase y sincroniza el proyecto.

### Error de Hilt: "Component not generated"
**Solución**: Verifica que todas las anotaciones estén correctamente aplicadas:
- `@HiltAndroidApp` en FormularioApplication
- `@AndroidEntryPoint` en MainActivity
- `@HiltViewModel` en ViewModels
- `@Inject` en constructores

### Error de Compose: "Unresolved reference"
**Solución**: Sincroniza Gradle y asegúrate de que todas las dependencias de Compose estén importadas correctamente.

---

## 16. Historial de Cambios Arquitecturales

### v1.0 - Arquitectura Inicial (MVVM básico)
- ViewModels directamente con Repository
- Modelo único para UI y datos

### v2.0 - Clean Architecture (Data-Domain-UI) ✅ ACTUAL
- **Separación en 3 capas**:
  - Data: FormEntity, SupabaseClient, FormRepositoryImpl
  - Domain: FormData, FormRequest, IFormRepository, UseCases
  - UI: ViewModels, Screens, Components
- **Inyección de dependencias con Hilt**
- **Casos de uso** para lógica de negocio
- **Inversión de dependencias** mediante interfaces

### Beneficios de la migración:
1. ✅ Código más testeable
2. ✅ Lógica de negocio independiente de frameworks
3. ✅ Mayor mantenibilidad
4. ✅ Escalabilidad mejorada
5. ✅ Separación clara de responsabilidades

---

## 17. Notas Importantes para Desarrolladores

### Al agregar nueva funcionalidad:
1. **Pregúntate**: ¿En qué capa va esto?
   - ¿Es lógica de negocio? → Domain (Use Case)
   - ¿Es acceso a datos? → Data (Repository)
   - ¿Es UI? → UI (Screen/Component)

2. **Sigue el flujo**:
   ```
   UI → ViewModel → UseCase → Repository → DataSource
   ```

3. **Inyecta dependencias con Hilt**:
   - Usa `@Inject` en constructores
   - Usa `@HiltViewModel` en ViewModels
   - Define providers en módulos si es necesario

4. **Mantén las capas independientes**:
   - Domain NO debe importar nada de Data o UI
   - Data puede importar Domain (interfaces)
   - UI puede importar Domain (modelos y use cases)

### Convenciones de código:
- **Nombres de archivos**: PascalCase
- **Nombres de funciones**: camelCase
- **Constantes**: UPPER_SNAKE_CASE
- **Composables**: Empiezan con mayúscula
- **Use Cases**: Terminar con "UseCase"
- **Interfaces de repositorio**: Empezar con "I" (IFormRepository)

---

## FIN DEL DOCUMENTO

**Última actualización**: 28 de abril de 2026  
**Versión de arquitectura**: 2.0 (Clean Architecture)  
**Estado del proyecto**: ✅ Funcional y listo para desarrollo adicional
