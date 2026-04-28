package com.example.formulario.data.repository

import com.example.formulario.data.model.FormEntity
import com.example.formulario.data.model.NetworkResult
import com.example.formulario.data.remote.SupabaseClient
import com.example.formulario.model.FormData
import io.github.jan.supabase.postgrest.from
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.net.UnknownHostException

class FormRepository {
    private val supabase = SupabaseClient.client

    suspend fun submitForm(formData: FormData): NetworkResult<FormEntity> {
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

                    NetworkResult.Success(response)
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
                
                NetworkResult.Error(
                    message = message,
                    exception = e
                )
            }
        }
    }

    suspend fun getAllRequests(): NetworkResult<List<FormEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                withTimeout(15000) {
                    val response = supabase.from("form_requests")
                        .select()
                        .decodeList<FormEntity>()

                    NetworkResult.Success(response)
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
                
                NetworkResult.Error(
                    message = message,
                    exception = e
                )
            }
        }
    }
}
