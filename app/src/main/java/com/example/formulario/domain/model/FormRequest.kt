package com.example.formulario.domain.model

data class FormRequest(
    val id: String? = null,
    val title: String,
    val description: String,
    val category: String,
    val priority: Int,
    val email: String,
    val createdAt: String? = null
)
