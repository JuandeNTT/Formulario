package com.example.formulario.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormEntity(
    @SerialName("id")
    val id: String? = null,
    
    @SerialName("title")
    val title: String,
    
    @SerialName("description")
    val description: String,
    
    @SerialName("category")
    val category: String,
    
    @SerialName("priority")
    val priority: Int,
    
    @SerialName("email")
    val email: String,
    
    @SerialName("created_at")
    val createdAt: String? = null
)
