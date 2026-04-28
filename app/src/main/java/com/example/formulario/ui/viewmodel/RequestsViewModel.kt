package com.example.formulario.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formulario.data.repository.FormRepositoryImpl
import com.example.formulario.domain.model.FormRequest
import com.example.formulario.domain.repository.Result
import com.example.formulario.domain.usecase.GetAllRequestsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RequestsUiState(
    val isLoading: Boolean = false,
    val requests: List<FormRequest> = emptyList(),
    val errorMessage: String? = null
)

class RequestsViewModel(
    private val repository: FormRepositoryImpl = FormRepositoryImpl()
) : ViewModel() {
    
    private val getAllRequestsUseCase = GetAllRequestsUseCase(repository)
    
    private val _uiState = MutableStateFlow(RequestsUiState())
    val uiState: StateFlow<RequestsUiState> = _uiState.asStateFlow()
    
    init {
        loadRequests()
    }
    
    fun loadRequests() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )
            
            when (val result = getAllRequestsUseCase()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        requests = result.data.sortedByDescending { it.createdAt }
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        requests = emptyList(),
                        errorMessage = result.message
                    )
                }
                is Result.Loading -> {
                    // Already loading
                }
            }
        }
    }
    
    fun dismissError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
