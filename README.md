# Formulario Android - Jetpack Compose

Aplicación Android de formulario desarrollada con Jetpack Compose siguiendo el patrón arquitectural MVVM y Material Design 3.

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

### Estados del Formulario
- ✅ Estado "Enviando..." con indicador de progreso
- ✅ Prevención de doble envío durante el proceso
- ✅ Mensaje de éxito tras envío exitoso
- ✅ Reset automático del formulario después del envío

## 🏗️ Arquitectura

### Patrón MVVM (Model-View-ViewModel)

```
app/src/main/java/com/example/formulario/
├── model/
│   └── FormData.kt              # Modelo de datos del formulario
├── viewmodel/
│   └── FormViewModel.kt         # Lógica de negocio y gestión de estado
├── ui/
│   ├── components/              # Componentes reutilizables
│   │   ├── FormTextField.kt     # Campo de texto con contador
│   │   ├── CategoryDropdown.kt  # Dropdown de categorías
│   │   ├── PrioritySlider.kt    # Slider de prioridad
│   │   ├── EmailTextField.kt    # Campo de email especializado
│   │   ├── SubmitButton.kt      # Botón con estados
│   │   └── SuccessMessage.kt    # Mensaje de éxito
│   ├── screens/
│   │   └── FormScreen.kt        # Pantalla principal del formulario
│   └── theme/
│       ├── Dimens.kt            # Dimensiones centralizadas
│       ├── Color.kt             # Colores del tema
│       ├── Theme.kt             # Configuración de Material 3
│       └── Type.kt              # Tipografía
└── MainActivity.kt              # Actividad principal
```

### Componentes Clave

#### 1. FormData (Model)
```kotlin
data class FormData(
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val priority: Int = 1,
    val email: String = ""
)
```

#### 2. FormViewModel (ViewModel)
- Gestión del estado con `StateFlow`
- Validaciones en tiempo real
- Lógica de envío del formulario
- Prevención de doble envío

#### 3. FormScreen (View)
- UI declarativa con Jetpack Compose
- Material Design 3
- Responsive y scrollable
- Feedback visual para errores y estados

#### 4. Componentes Reutilizables

**FormTextField**: Campo de texto genérico con contador de caracteres y validación
**CategoryDropdown**: Selector desplegable con Material 3
**PrioritySlider**: Slider visual con indicador numérico destacado
**EmailTextField**: Campo especializado para email con icono
**SubmitButton**: Botón con estados de carga y prevención de doble envío
**SuccessMessage**: Tarjeta de confirmación con diseño Material 3

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

### Strings Externalizados
Todos los textos están en `res/values/strings.xml` para facilitar la internacionalización y mantenimiento.

## 🚀 Configuración del Proyecto

### Requisitos
- Android Studio Hedgehog o superior
- Kotlin 1.9+
- compileSdk 34
- minSdk 24
- targetSdk 34

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

// Activity Compose
implementation("androidx.activity:activity-compose:1.7.2")
```

## 🔧 Instalación y Ejecución

1. Clonar el repositorio
2. Abrir el proyecto en Android Studio
3. Sincronizar Gradle
4. Ejecutar en un dispositivo o emulador con API 24+

```bash
# Compilar el proyecto
./gradlew build

# Instalar en dispositivo conectado
./gradlew installDebug
```

## 📱 Uso

1. **Título**: Ingrese entre 5 y 60 caracteres
2. **Descripción**: Ingrese entre 20 y 500 caracteres
3. **Categoría**: Seleccione una opción del desplegable
4. **Prioridad**: Ajuste el slider entre 1 y 5
5. **Email**: Ingrese un email válido
6. **Enviar**: El botón se habilitará cuando todos los campos sean válidos

## ✅ Validaciones Implementadas

| Campo | Regla | Mensaje de Error |
|-------|-------|------------------|
| Título | 5-60 caracteres | "El título debe tener al menos 5 caracteres" |
| Descripción | 20-500 caracteres | "La descripción debe tener al menos 20 caracteres" |
| Categoría | Requerido | "Debe seleccionar una categoría" |
| Prioridad | 1-5 | Validación automática con slider |
| Email | Formato válido | "Ingrese un email válido" |

## 🎯 Decisiones de Diseño

### Por qué MVVM
- Separación clara de responsabilidades
- Facilita testing unitario del ViewModel
- Reactividad con StateFlow
- Compatible con ciclo de vida de Android

### Por qué Material 3
- Diseño moderno y consistente
- Componentes accesibles
- Soporte para temas dinámicos
- Guías de diseño de Google

### Por qué Jetpack Compose
- UI declarativa y reactiva
- Menos código boilerplate
- Preview en tiempo real
- Integración nativa con ViewModel

### Por qué Componentes Reutilizables
- Código más mantenible y escalable
- Facilita testing individual
- Reutilización en otros formularios
- Separación de responsabilidades en UI

## 🧪 Testing

### ViewModel Testing
El ViewModel puede ser testeado unitariamente:
```kotlin
@Test
fun `validate title with valid input`() {
    viewModel.onTitleChange("Valid Title")
    assertNull(viewModel.uiState.value.titleError)
}
```

## 📝 Notas Técnicas

### Gestión de Estado
- Uso de `StateFlow` para estado reactivo
- `collectAsState()` en Compose para observar cambios
- Estado inmutable con `copy()`

### Prevención de Doble Envío
```kotlin
fun submitForm() {
    if (!isFormValid() || _uiState.value.isSubmitting) {
        return
    }
    // ... lógica de envío
}
```

### Validación Regex Email
```kotlin
Patterns.EMAIL_ADDRESS
```

### Componentes Modulares
Cada componente UI está aislado en su propio archivo, facilitando:
- Mantenimiento independiente
- Reutilización en otros proyectos
- Testing individual
- Documentación específica

## 👨‍💻 Desarrollo

Desarrollado con Jetpack Compose y siguiendo las mejores prácticas de Android moderno.
