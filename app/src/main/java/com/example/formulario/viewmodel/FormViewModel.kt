package com.example.formulario.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formulario.data.model.NetworkResult
import com.example.formulario.data.repository.FormRepository
import com.example.formulario.model.FormData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FormUiState(
    val formData: FormData = FormData(),
    val titleError: String? = null,
    val descriptionError: String? = null,
    val categoryError: String? = null,
    val emailError: String? = null,
    val isSubmitting: Boolean = false,
    val submitSuccess: Boolean = false,
    val titleCharCount: Int = 0,
    val descriptionCharCount: Int = 0,
    val emailCharCount: Int = 0,
    val errorMessage: String? = null
)

class FormViewModel(
    private val repository: FormRepository = FormRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FormUiState())
    val uiState: StateFlow<FormUiState> = _uiState.asStateFlow()
    
    companion object {
        const val TITLE_MIN_LENGTH = 5
        const val TITLE_MAX_LENGTH = 60
        const val DESCRIPTION_MIN_LENGTH = 20
        const val DESCRIPTION_MAX_LENGTH = 500
        const val EMAIL_MAX_LENGTH = 100
        const val PRIORITY_MIN = 1
        const val PRIORITY_MAX = 5
    }
    
    fun onTitleChange(title: String) {
        if (title.length <= TITLE_MAX_LENGTH) {
            _uiState.value = _uiState.value.copy(
                formData = _uiState.value.formData.copy(title = title),
                titleCharCount = title.length,
                titleError = validateTitle(title),
                submitSuccess = false
            )
        }
    }
    
    fun onDescriptionChange(description: String) {
        if (description.length <= DESCRIPTION_MAX_LENGTH) {
            _uiState.value = _uiState.value.copy(
                formData = _uiState.value.formData.copy(description = description),
                descriptionCharCount = description.length,
                descriptionError = validateDescription(description),
                submitSuccess = false
            )
        }
    }
    
    fun onCategoryChange(category: String) {
        _uiState.value = _uiState.value.copy(
            formData = _uiState.value.formData.copy(category = category),
            categoryError = validateCategory(category),
            submitSuccess = false
        )
    }
    
    fun onPriorityChange(priority: Float) {
        _uiState.value = _uiState.value.copy(
            formData = _uiState.value.formData.copy(priority = priority.toInt()),
            submitSuccess = false
        )
    }
    
    fun onEmailChange(email: String) {
        if (email.length <= EMAIL_MAX_LENGTH) {
            _uiState.value = _uiState.value.copy(
                formData = _uiState.value.formData.copy(email = email),
                emailCharCount = email.length,
                emailError = validateEmail(email),
                submitSuccess = false
            )
        }
    }
    
    private fun validateTitle(title: String): String? {
        return when {
            title.isEmpty() -> null
            title.length < TITLE_MIN_LENGTH -> "El título debe tener al menos $TITLE_MIN_LENGTH caracteres"
            title.length > TITLE_MAX_LENGTH -> "El título no puede superar $TITLE_MAX_LENGTH caracteres"
            else -> null
        }
    }
    
    private fun validateDescription(description: String): String? {
        return when {
            description.isEmpty() -> null
            description.length < DESCRIPTION_MIN_LENGTH -> "La descripción debe tener al menos $DESCRIPTION_MIN_LENGTH caracteres"
            description.length > DESCRIPTION_MAX_LENGTH -> "La descripción no puede superar $DESCRIPTION_MAX_LENGTH caracteres"
            else -> null
        }
    }
    
    private fun validateCategory(category: String): String? {
        return if (category.isEmpty()) "Debe seleccionar una categoría" else null
    }
    
    private fun validateEmail(email: String): String? {
        return when {
            email.isEmpty() -> null
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Ingrese un email válido"
            else -> null
        }
    }
    
    fun isFormValid(): Boolean {
        val state = _uiState.value
        val formData = state.formData
        
        return formData.title.length in TITLE_MIN_LENGTH..TITLE_MAX_LENGTH &&
                formData.description.length in DESCRIPTION_MIN_LENGTH..DESCRIPTION_MAX_LENGTH &&
                formData.category.isNotEmpty() &&
                formData.priority in PRIORITY_MIN..PRIORITY_MAX &&
                Patterns.EMAIL_ADDRESS.matcher(formData.email).matches() &&
                !state.isSubmitting
    }
    
    fun submitForm() {
        if (!isFormValid() || _uiState.value.isSubmitting) {
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSubmitting = true,
                errorMessage = null
            )
            
            when (val result = repository.submitForm(_uiState.value.formData)) {
                is NetworkResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        submitSuccess = true
                    )
                    resetForm()
                }
                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        errorMessage = result.message
                    )
                }
                is NetworkResult.Loading -> {
                    // No action needed
                }
            }
        }
    }
    
    fun dismissErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    private fun resetForm() {
        _uiState.value = FormUiState(submitSuccess = true)
    }
}
