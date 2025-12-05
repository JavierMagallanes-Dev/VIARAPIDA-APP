package com.viarapida.pasajes.data.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class Horario(
    val id: String = "",
    val destinoId: String = "",
    val fecha: Timestamp = Timestamp.now(),
    val horaSalida: String = "",
    val horaLlegada: String = "",
    val asientosDisponibles: Int = 45,
    val asientosTotales: Int = 45,
    val tipoBus: String = "Estándar", // Estándar, VIP, Semi-Cama
    val estado: String = "activo" // activo, cancelado, finalizado
) : Parcelable {

    constructor() : this(
        id = "",
        destinoId = "",
        fecha = Timestamp.now(),
        horaSalida = "",
        horaLlegada = "",
        asientosDisponibles = 45,
        asientosTotales = 45,
        tipoBus = "Estándar",
        estado = "activo"
    )

    fun toMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "destinoId" to destinoId,
            "fecha" to fecha,
            "horaSalida" to horaSalida,
            "horaLlegada" to horaLlegada,
            "asientosDisponibles" to asientosDisponibles,
            "asientosTotales" to asientosTotales,
            "tipoBus" to tipoBus,
            "estado" to estado
        )
    }

    // Función para obtener fecha formateada
    fun fechaFormateada(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("es", "PE"))
        return sdf.format(fecha.toDate())
    }

    // Función para obtener día de la semana
    fun diaSemana(): String {
        val sdf = SimpleDateFormat("EEEE", Locale("es", "PE"))
        return sdf.format(fecha.toDate()).replaceFirstChar { it.uppercase() }
    }

    // Calcular porcentaje de ocupación
    fun porcentajeOcupacion(): Int {
        val ocupados = asientosTotales - asientosDisponibles
        return ((ocupados.toFloat() / asientosTotales) * 100).toInt()
    }

    // Verificar disponibilidad
    fun tieneDisponibilidad(): Boolean = asientosDisponibles > 0 && estado == "activo"
}