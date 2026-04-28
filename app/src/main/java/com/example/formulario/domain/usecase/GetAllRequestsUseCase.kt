package com.example.formulario.domain.usecase

import com.example.formulario.domain.model.FormRequest
import com.example.formulario.domain.repository.IFormRepository
import com.example.formulario.domain.repository.Result

class GetAllRequestsUseCase(
    private val repository: IFormRepository
) {
    suspend operator fun invoke(): Result<List<FormRequest>> {
        return repository.getAllRequests()
    }
}
