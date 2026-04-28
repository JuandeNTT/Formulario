package com.example.formulario.domain.usecase

import com.example.formulario.domain.model.FormData
import com.example.formulario.domain.model.FormRequest
import com.example.formulario.domain.repository.IFormRepository
import com.example.formulario.domain.repository.Result
import javax.inject.Inject

class SubmitFormUseCase @Inject constructor(
    private val repository: IFormRepository
) {
    suspend operator fun invoke(formData: FormData): Result<FormRequest> {
        return repository.submitForm(formData)
    }
}
