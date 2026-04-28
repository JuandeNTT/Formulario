package com.example.formulario.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formulario.data.model.FormEntity
import com.example.formulario.data.model.NetworkResult
import com.example.formulario.data.repository.FormRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RequestsUiState(
    val requests: List<FormEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class RequestsViewModel(
    private val repository: FormRepository = FormRepository()
) : ViewModel() {
    
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
            
            when (val result = repository.getAllRequests()) {
                is NetworkResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        requests = result.data.sortedByDescending { it.createdAt },
                        errorMessage = null
                    )
                }
                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        requests = emptyList(),
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
}
