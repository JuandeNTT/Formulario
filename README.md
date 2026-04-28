# Formulario Android - Jetpack Compose

Aplicación Android de formulario desarrollada con Jetpack Compose siguiendo la arquitectura **Clean Architecture** con las capas **Data-Domain-UI** y Material Design 3.

## 📋 Características

### Campos del Formulario
- **Título**: Campo de texto de 5-60 caracteres
- **Descripción**: Campo de texto multilínea de 20-500 caracteres
- **Categoría**: Selector desplegable con 5 opciones
- **Prioridad**: Slider de 1-5
- **Email**: Campo de texto con validación de formato

### Validaciones
- ✅ Validación en tiempo real con mensajes de error
- ✅ Contador de caracteres para título y descripción
- ✅ Validación de email con expresión regular
- ✅ Validación de rango para prioridad (1-5)
- ✅ Botón de envío deshabilitado cuando el formulario es inválido

### Funcionalidades
- ✅ Envío de formulario a Supabase
- ✅ Visualización de todas las solicitudes
- ✅ Estado "Enviando..." con indicador de progreso
- ✅ Prevención de doble envío durante el proceso
- ✅ Mensaje de éxito tras envío exitoso
- ✅ Manejo de errores con diálogos informativos
- ✅ Navegación entre pantallas

## 🏗️ Arquitectura Clean - Data-Domain-UI

### Estructura del Proyecto

```
app/src/main/java/com/example/formulario/
├── data/                           # Capa de Datos
│   ├── model/
│   │   ├── FormEntity.kt          # Entidad para base de datos
│   │   └── NetworkResult.kt       # Wrapper para resultados de red
│   ├── remote/
│   │   └── SupabaseClient.kt      # Cliente de Supabase
│   └── repository/
│       └── FormRepositoryImpl.kt  # Implementación del repositorio
│
├── domain/                         # Capa de Dominio (Lógica de Negocio)
│   ├── model/
│   │   ├── FormData.kt            # Modelo de dominio para el formulario
│   │   └── FormRequest.kt         # Modelo de dominio para solicitudes
│   ├── repository/
│   │   └── IFormRepository.kt     # Interfaz del repositorio
│   └── usecase/
│       ├── ValidateFormUseCase.kt # Caso de uso: Validación
│       ├── SubmitFormUseCase.kt   # Caso de uso: Envío
│       └── GetAllRequestsUseCase.kt # Caso de uso: Obtener solicitudes
│
├── ui/                             # Capa de Presentación
│   ├── viewmodel/
│   │   ├── FormViewModel.kt       # ViewModel del formulario
│   │   └── RequestsViewModel.kt   # ViewModel de solicitudes
│   ├── screens/
│   │   ├── FormScreen.kt          # Pantalla del formulario
│   │   └── RequestsScreen.kt      # Pantalla de solicitudes
│   ├── components/                # Componentes reutilizables
│   │   ├── FormTextField.kt       # Campo de texto con contador
│   │   ├── CategoryDropdown.kt    # Dropdown de categorías
│   │   ├── PrioritySlider.kt      # Slider de prioridad
│   │   ├── EmailTextField.kt      # Campo de email especializado
│   │   ├── SubmitButton.kt        # Botón con estados
│   │   ├── SuccessMessage.kt      # Mensaje de éxito
│   │   ├── ErrorDialog.kt         # Diálogo de error
│   │   └── RequestItem.kt         # Item de solicitud
│   └── theme/
│       ├── Dimens.kt              # Dimensiones centralizadas
│       ├── Color.kt               # Colores del tema
│       ├── Theme.kt               # Configuración de Material 3
│       └── Type.kt                # Tipografía
│
├── navigation/
│   └── Navigation.kt              # Navegación entre pantallas
├── utils/
│   └── DateUtils.kt               # Utilidades para fechas
└── MainActivity.kt                # Actividad principal
```

## 🎯 Capas de la Arquitectura

### 1. Capa de Datos (Data Layer)

**Responsabilidad**: Gestión de fuentes de datos (API, base de datos local, etc.)

#### Componentes:
- **FormEntity**: Modelo de datos que representa la estructura en la base de datos
- **NetworkResult**: Clase sellada para encapsular resultados de operaciones de red (Success, Error, Loading)
- **SupabaseClient**: Cliente singleton para interactuar con Supabase
- **FormRepositoryImpl**: Implementación concreta del repositorio que gestiona el acceso a datos

```kotlin
// Ejemplo de uso
class FormRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : IFormRepository {
    override suspend fun submitForm(formData: FormData): NetworkResult<Unit>
    override suspend fun getAllRequests(): NetworkResult<List<FormRequest>>
}
```

### 2. Capa de Dominio (Domain Layer)

**Responsabilidad**: Lógica de negocio pura, independiente de frameworks

#### Componentes:

**Modelos de Dominio**:
- `FormData`: Representa los datos del formulario en el dominio
- `FormRequest`: Representa una solicitud guardada

**Interfaz de Repositorio**:
- `IFormRepository`: Contrato que define las operaciones de datos (patrón Repository)

**Casos de Uso (Use Cases)**:
- `ValidateFormUseCase`: Valida todos los campos del formulario
- `SubmitFormUseCase`: Envía el formulario a través del repositorio
- `GetAllRequestsUseCase`: Obtiene todas las solicitudes guardadas

```kotlin
// Ejemplo de caso de uso
class SubmitFormUseCase(
    private val repository: IFormRepository
) {
    suspend operator fun invoke(formData: FormData): NetworkResult<Unit> {
        return repository.submitForm(formData)
    }
}
```

**Ventajas de los Use Cases**:
- Encapsulan lógica de negocio específica
- Fáciles de testear unitariamente
- Reutilizables en diferentes ViewModels
- Facilitan el mantenimiento y escalabilidad

### 3. Capa de Presentación (UI Layer)

**Responsabilidad**: Interfaz de usuario y gestión de estado

#### Componentes:
- **ViewModels**: Gestionan el estado de la UI y coordinan los casos de uso
- **Screens**: Pantallas principales de la aplicación
- **Components**: Componentes reutilizables de UI
- **Theme**: Configuración del tema Material 3

```kotlin
// Ejemplo de ViewModel usando casos de uso
class FormViewModel(
    private val validateFormUseCase: ValidateFormUseCase,
    private val submitFormUseCase: SubmitFormUseCase
) : ViewModel() {
    // Estado y lógica de presentación
}
```

## 🔄 Flujo de Datos

```
UI (Screen) 
    ↓ Acción del usuario
ViewModel 
    ↓ Invoca
Use Case 
    ↓ Usa
Repository Interface 
    ↓ Implementa
Repository Implementation 
    ↓ Accede
Data Source (Supabase)
```

### Ejemplo de Flujo: Envío de Formulario

1. **UI**: Usuario presiona el botón "Enviar"
2. **ViewModel**: `FormViewModel.submitForm()` se invoca
3. **Use Case**: `SubmitFormUseCase` valida y procesa los datos
4. **Repository**: `FormRepositoryImpl` envía los datos a Supabase
5. **Data Source**: `SupabaseClient` realiza la petición HTTP
6. **Respuesta**: El resultado fluye de vuelta por las capas
7. **UI**: Se actualiza mostrando éxito o error

## 🎨 Diseño

### Material Design 3
- Componentes modernos de Material 3
- Tema dinámico compatible
- Elevación y esquinas redondeadas en tarjetas
- Colores semánticos (error, primary, success)
- Arquitectura modular con componentes reutilizables

### Dimensiones Centralizadas (Dimens.kt)
```kotlin
object Dimens {
    val paddingSmall = 8.dp
    val paddingMedium = 16.dp
    val paddingLarge = 24.dp
    val spacingMedium = 16.dp
    val cardElevation = 4.dp
    val buttonHeight = 56.dp
    // ... más dimensiones
}
```

## 🚀 Configuración del Proyecto

### Requisitos
- Android Studio Hedgehog o superior
- Kotlin 1.9+
- compileSdk 34
- minSdk 24
- targetSdk 34
- Cuenta de Supabase configurada

### Dependencias Principales
```kotlin
// Jetpack Compose BOM
implementation(platform("androidx.compose:compose-bom:2023.08.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.ui:ui-tooling-preview")

// Lifecycle & ViewModel
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

// Navigation Compose
implementation("androidx.navigation:navigation-compose:2.7.3")

// Supabase
implementation("io.github.jan-tennert.supabase:postgrest-kt:2.0.0")
implementation("io.github.jan-tennert.supabase:realtime-kt:2.0.0")
implementation("io.ktor:ktor-client-android:2.3.5")
```

### Configuración de Supabase

1. Crear archivo `.env` en la raíz del proyecto:
```env
SUPABASE_URL=tu_url_de_supabase
SUPABASE_KEY=tu_api_key_de_supabase
```

2. El archivo ya está incluido en `.gitignore`

## 🔧 Instalación y Ejecución

1. Clonar el repositorio
2. Configurar las credenciales de Supabase en `.env`
3. Abrir el proyecto en Android Studio
4. Sincronizar Gradle
5. Ejecutar en un dispositivo o emulador con API 24+

```bash
# Compilar el proyecto
./gradlew build

# Instalar en dispositivo conectado
./gradlew installDebug
```

## 📱 Uso

### Pantalla de Formulario
1. **Título**: Ingrese entre 5 y 60 caracteres
2. **Descripción**: Ingrese entre 20 y 500 caracteres
3. **Categoría**: Seleccione una opción del desplegable
4. **Prioridad**: Ajuste el slider entre 1 y 5
5. **Email**: Ingrese un email válido
6. **Enviar**: El botón se habilitará cuando todos los campos sean válidos
7. **Ver Solicitudes**: Navegue a la pantalla de solicitudes guardadas

### Pantalla de Solicitudes
- Visualice todas las solicitudes enviadas
- Cada tarjeta muestra título, descripción, categoría, prioridad, email y fecha
- Los colores de prioridad ayudan a identificar la urgencia

## ✅ Validaciones Implementadas

| Campo | Regla | Mensaje de Error |
|-------|-------|------------------|
| Título | 5-60 caracteres | "El título debe tener al menos 5 caracteres" |
| Descripción | 20-500 caracteres | "La descripción debe tener al menos 20 caracteres" |
| Categoría | Requerido | "Debe seleccionar una categoría" |
| Prioridad | 1-5 | Validación automática con slider |
| Email | Formato válido | "Ingrese un email válido" |

## 🎯 Ventajas de esta Arquitectura

### Separación de Responsabilidades
- Cada capa tiene una responsabilidad única y bien definida
- Cambios en una capa no afectan a las demás

### Testabilidad
- Los casos de uso pueden testearse independientemente
- Los ViewModels pueden testearse sin dependencias de Android
- Los repositorios pueden mockearse fácilmente

### Escalabilidad
- Fácil agregar nuevas funcionalidades
- Nuevos casos de uso se integran sin modificar código existente
- Componentes reutilizables en toda la aplicación

### Mantenibilidad
- Código organizado y fácil de entender
- Búsqueda rápida de funcionalidades
- Documentación implícita por la estructura

### Independencia de Frameworks
- La capa de dominio no depende de Android ni Compose
- Fácil migración a otras tecnologías si es necesario
- Lógica de negocio portable

## 🧪 Testing

### Unit Tests - Domain Layer
```kotlin
class ValidateFormUseCaseTest {
    @Test
    fun `validate title with valid input returns success`() {
        val useCase = ValidateFormUseCase()
        val result = useCase.validateTitle("Valid Title")
        assertTrue(result.isValid)
    }
}
```

### Unit Tests - ViewModel
```kotlin
class FormViewModelTest {
    @Test
    fun `submitForm calls submitFormUseCase`() = runTest {
        val mockUseCase = mockk<SubmitFormUseCase>()
        val viewModel = FormViewModel(mockUseCase)
        viewModel.submitForm()
        verify { mockUseCase.invoke(any()) }
    }
}
```

## 📝 Decisiones de Diseño

### ¿Por qué Clean Architecture?
- **Separación de preocupaciones**: Cada capa tiene responsabilidades claras
- **Testabilidad**: Fácil crear tests unitarios para cada componente
- **Escalabilidad**: Agregar nuevas funcionalidades sin afectar código existente
- **Mantenibilidad**: Código organizado y fácil de navegar

### ¿Por qué Use Cases?
- Encapsulan lógica de negocio específica
- Representan acciones del usuario de forma clara
- Facilitan la reutilización de lógica
- Permiten testing independiente

### ¿Por qué Repository Pattern?
- Abstrae el origen de los datos
- Facilita el cambio de fuente de datos (API, BD local, caché)
- Permite mockear datos en tests
- Centraliza la lógica de acceso a datos

### ¿Por qué Jetpack Compose?
- UI declarativa y reactiva
- Menos código boilerplate
- Preview en tiempo real
- Integración nativa con ViewModel

### ¿Por qué Material 3?
- Diseño moderno y consistente
- Componentes accesibles
- Soporte para temas dinámicos
- Guías de diseño de Google

## � Seguridad

- Las credenciales de Supabase están en archivo `.env` (no versionado)
- Uso de HTTPS para todas las comunicaciones
- Validación de datos en cliente y servidor

## 📈 Pos
