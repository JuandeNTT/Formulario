package com.example.formulario.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Convierte una fecha UTC en formato ISO 8601 (ej: "2026-04-28T18:13:00.000Z")
 * a la hora local del dispositivo con formato "dd/MM/yyyy HH:mm"
 *
 * @param fechaUtc Fecha en formato ISO 8601 UTC (termina en Z)
 * @return Fecha formateada en hora local, o la fecha original si hay error
 */
@RequiresApi(Build.VERSION_CODES.O)
fun convertirAHoraLocal(fechaUtc: String): String {
    return try {
        Log.d("DateUtils", "Fecha original: $fechaUtc")
        
        // Parse la fecha como Instant (siempre UTC)
        val instant = Instant.parse(fechaUtc)
        Log.d("DateUtils", "Instant parseado: $instant")
        
        // Usar explícitamente la zona horaria de Madrid (España)
        val zonaHoraria = ZoneId.of("Europe/Madrid")
        Log.d("DateUtils", "Zona horaria utilizada: $zonaHoraria")
        
        // Convertir a ZonedDateTime en la zona horaria local
        val fechaLocal = instant.atZone(zonaHoraria)
        Log.d("DateUtils", "Fecha local: $fechaLocal")
        
        // Formatear al formato deseado: dd/MM/yyyy HH:mm
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val resultado = fechaLocal.format(formatter)
        Log.d("DateUtils", "Fecha formateada: $resultado")
        
        resultado
    } catch (e: Exception) {
        Log.e("DateUtils", "Error al convertir fecha: ${e.message}", e)
        // Si hay error, devolver la fecha original
        fechaUtc
    }
}
