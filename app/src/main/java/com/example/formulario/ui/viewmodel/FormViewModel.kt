package com.example.formulario.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formulario.domain.model.FormData
import com.example.formulario.domain.repository.Result
import com.example.formulario.domain.usecase.SubmitFormUseCase
import com.example.formulario.domain.usecase.ValidateFormUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
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

@HiltViewModel
class FormViewModel @Inject constructor(
    private val submitFormUseCase: SubmitFormUseCase,
    private val validateFormUseCase: ValidateFormUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FormUiState())
    val uiState: StateFlow<FormUiState> = _uiState.asStateFlow()
    
    companion object {
        const val TITLE_MIN_LENGTH = ValidateFormUseCase.TITLE_MIN_LENGTH
        const val TITLE_MAX_LENGTH = ValidateFormUseCase.TITLE_MAX_LENGTH
        const val DESCRIPTION_MIN_LENGTH = ValidateFormUseCase.DESCRIPTION_MIN_LENGTH
        const val DESCRIPTION_MAX_LENGTH = ValidateFormUseCase.DESCRIPTION_MAX_LENGTH
        const val EMAIL_MAX_LENGTH = ValidateFormUseCase.EMAIL_MAX_LENGTH
        const val PRIORITY_MIN = ValidateFormUseCase.PRIORITY_MIN
        const val PRIORITY_MAX = ValidateFormUseCase.PRIORITY_MAX
    }
    
    fun onTitleChange(title: String) {
        if (title.length <= TITLE_MAX_LENGTH) {
            _uiState.value = _uiState.value.copy(
                formData = _uiState.value.formData.copy(title = title),
                titleCharCount = title.length,
                titleError = validateFormUseCase.validateTitle(title),
                submitSuccess = false
            )
        }
    }
    
    fun onDescriptionChange(description: String) {
        if (description.length <= DESCRIPTION_MAX_LENGTH) {
            _uiState.value = _uiState.value.copy(
                formData = _uiState.value.formData.copy(description = description),
                descriptionCharCount = description.length,
                descriptionError = validateFormUseCase.validateDescription(description),
                submitSuccess = false
            )
        }
    }
    
    fun onCategoryChange(category: String) {
        _uiState.value = _uiState.value.copy(
            formData = _uiState.value.formData.copy(category = category),
            categoryError = validateFormUseCase.validateCategory(category),
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
                emailError = validateFormUseCase.validateEmail(email),
                submitSuccess = false
            )
        }
    }
    
    fun isFormValid(): Boolean {
        val validation = validateFormUseCase(_uiState.value.formData)
        return validation.isValid && !_uiState.value.isSubmitting
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
            
            when (val result = submitFormUseCase(_uiState.value.formData)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        submitSuccess = true
                    )
                    resetForm()
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        errorMessage = result.message
                    )
                }
                is Result.Loading -> {
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
