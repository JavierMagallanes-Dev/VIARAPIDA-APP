package com.viarapida.pasajes.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destino(
    val id: String = "",
    val origen: String = "",
    val destino: String = "",
    val precio: Double = 0.0,
    val duracionHoras: Int = 0,
    val distanciaKm: Double = 0.0,
    val imagenUrl: String = "",
    val activo: Boolean = true
) : Parcelable {

    constructor() : this(
        id = "",
        origen = "",
        destino = "",
        precio = 0.0,
        duracionHoras = 0,
        distanciaKm = 0.0,
        imagenUrl = "",
        activo = true
    )

    fun toMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "origen" to origen,
            "destino" to destino,
            "precio" to precio,
            "duracionHoras" to duracionHoras,
            "distanciaKm" to distanciaKm,
            "imagenUrl" to imagenUrl,
            "activo" to activo
        )
    }

    // Función para obtener ruta completa
    fun rutaCompleta(): String = "$origen → $destino"

    // Función para formatear precio
    fun precioFormateado(): String = "S/ %.2f".format(precio)

    // Función para formatear duración
    fun duracionFormateada(): String {
        return when {
            duracionHoras < 1 -> "${(duracionHoras * 60).toInt()} min"
            duracionHoras == 1 -> "1 hora"
            else -> "$duracionHoras horas"
        }
    }
}
