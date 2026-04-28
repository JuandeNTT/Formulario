package com.example.formulario.domain.repository

import com.example.formulario.domain.model.FormData
import com.example.formulario.domain.model.FormRequest

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val exception: Exception? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

interface IFormRepository {
    suspend fun submitForm(formData: FormData): Result<FormRequest>
    suspend fun getAllRequests(): Result<List<FormRequest>>
}
