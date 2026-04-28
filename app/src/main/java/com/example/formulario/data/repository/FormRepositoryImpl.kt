package com.example.formulario.data.repository

import com.example.formulario.data.model.FormEntity
import com.example.formulario.data.remote.SupabaseClient
import com.example.formulario.domain.model.FormData
import com.example.formulario.domain.model.FormRequest
import com.example.formulario.domain.repository.IFormRepository
import com.example.formulario.domain.repository.Result
import io.github.jan.supabase.postgrest.from
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.net.UnknownHostException

class FormRepositoryImpl : IFormRepository {
    private val supabase = SupabaseClient.client

    override suspend fun submitForm(formData: FormData): Result<FormRequest> {
        return withContext(Dispatchers.IO) {
            try {
                withTimeout(8000) {
                    val entity = FormEntity(
                        title = formData.title,
                        description = formData.description,
                        category = formData.category,
                        priority = formData.priority,
                        email = formData.email
                    )

                    val response = supabase.from("form_requests")
                        .insert(entity) {
                            select()
                        }
                        .decodeSingle<FormEntity>()

                    Result.Success(response.toDomain())
                }
            } catch (e: Exception) {
                val message = when (e) {
                    is TimeoutCancellationException ->
                        "Se ha excedido el tiempo límite de carga. Asegúrese de estar conectado a Internet e intente de nuevo."
                    is UnknownHostException -> 
                        "No se pudo conectar al servidor. Verifique su conexión a Internet."
                    is ConnectTimeoutException, is SocketTimeoutException, is HttpRequestTimeoutException ->
                        "Se ha excedido el tiempo límite de carga. Asegúrese de estar conectado a Internet e intente de nuevo."
                    else -> 
                        "Error al enviar el formulario. Asegúrese de estar conectado a Internet e intente de nuevo."
                }
                
                Result.Error(
                    message = message,
                    exception = e
                )
            }
        }
    }

    override suspend fun getAllRequests(): Result<List<FormRequest>> {
        return withContext(Dispatchers.IO) {
            try {
                withTimeout(15000) {
                    val response = supabase.from("form_requests")
                        .select()
                        .decodeList<FormEntity>()

                    Result.Success(response.map { it.toDomain() })
                }
            } catch (e: Exception) {
                val message = when (e) {
                    is TimeoutCancellationException ->
                        "Se ha excedido el tiempo límite de carga. Asegúrese de estar conectado a Internet e intente de nuevo."
                    is UnknownHostException -> 
                        "No se pudo conectar al servidor. Verifique su conexión a Internet."
                    is ConnectTimeoutException, is SocketTimeoutException, is HttpRequestTimeoutException ->
                        "Se ha excedido el tiempo límite de carga. Asegúrese de estar conectado a Internet e intente de nuevo."
                    else -> 
                        "Error al cargar las solicitudes. Asegúrese de estar conectado a Internet e intente de nuevo."
                }
                
                Result.Error(
                    message = message,
                    exception = e
                )
            }
        }
    }

    private fun FormEntity.toDomain(): FormRequest {
        return FormRequest(
            id = id,
            title = title,
            description = description,
            category = category,
            priority = priority,
            email = email,
            createdAt = createdAt
        )
    }
}
