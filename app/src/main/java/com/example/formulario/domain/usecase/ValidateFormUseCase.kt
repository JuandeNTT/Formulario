package com.example.formulario.domain.usecase

import android.util.Patterns
import com.example.formulario.domain.model.FormData

data class ValidationResult(
    val isValid: Boolean,
    val titleError: String? = null,
    val descriptionError: String? = null,
    val categoryError: String? = null,
    val emailError: String? = null
)

class ValidateFormUseCase {
    
    companion object {
        const val TITLE_MIN_LENGTH = 5
        const val TITLE_MAX_LENGTH = 60
        const val DESCRIPTION_MIN_LENGTH = 20
        const val DESCRIPTION_MAX_LENGTH = 500
        const val EMAIL_MAX_LENGTH = 100
        const val PRIORITY_MIN = 1
        const val PRIORITY_MAX = 5
    }
    
    operator fun invoke(formData: FormData): ValidationResult {
        val titleError = validateTitle(formData.title)
        val descriptionError = validateDescription(formData.description)
        val categoryError = validateCategory(formData.category)
        val emailError = validateEmail(formData.email)
        
        val isValid = titleError == null && 
                     descriptionError == null && 
                     categoryError == null && 
                     emailError == null &&
                     formData.priority in PRIORITY_MIN..PRIORITY_MAX
        
        return ValidationResult(
            isValid = isValid,
            titleError = titleError,
            descriptionError = descriptionError,
            categoryError = categoryError,
            emailError = emailError
        )
    }
    
    fun validateTitle(title: String): String? {
        return when {
            title.isEmpty() -> null
            title.length < TITLE_MIN_LENGTH -> "El título debe tener al menos $TITLE_MIN_LENGTH caracteres"
            title.length > TITLE_MAX_LENGTH -> "El título no puede superar $TITLE_MAX_LENGTH caracteres"
            else -> null
        }
    }
    
    fun validateDescription(description: String): String? {
        return when {
            description.isEmpty() -> null
            description.length < DESCRIPTION_MIN_LENGTH -> "La descripción debe tener al menos $DESCRIPTION_MIN_LENGTH caracteres"
            description.length > DESCRIPTION_MAX_LENGTH -> "La descripción no puede superar $DESCRIPTION_MAX_LENGTH caracteres"
            else -> null
        }
    }
    
    fun validateCategory(category: String): String? {
        return if (category.isEmpty()) "Debe seleccionar una categoría" else null
    }
    
    fun validateEmail(email: String): String? {
        return when {
            email.isEmpty() -> null
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Ingrese un email válido"
            else -> null
        }
    }
}
