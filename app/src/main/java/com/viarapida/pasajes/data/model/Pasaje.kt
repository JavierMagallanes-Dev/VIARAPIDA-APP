package com.viarapida.pasajes.data.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class Pasaje(
    val id: String = "",
    val usuarioId: String = "",
    val horarioId: String = "",
    val numeroAsiento: Int = 0,
    val codigoQR: String = "",
    val precioTotal: Double = 0.0,
    val metodoPago: String = "", // efectivo, tarjeta, yape, plin
    val estado: String = "pendiente", // pendiente, pagado, usado, cancelado
    val fechaCompra: Timestamp = Timestamp.now(),
    val fechaViaje: Timestamp = Timestamp.now(),

    // Información adicional para mostrar (no se guarda en Firebase)
    var usuarioNombre: String = "",
    var origenDestino: String = "",
    var horaSalida: String = ""
) : Parcelable {

    constructor() : this(
        id = "",
        usuarioId = "",
        horarioId = "",
        numeroAsiento = 0,
        codigoQR = "",
        precioTotal = 0.0,
        metodoPago = "",
        estado = "pendiente",
        fechaCompra = Timestamp.now(),
        fechaViaje = Timestamp.now()
    )

    fun toMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "usuarioId" to usuarioId,
            "horarioId" to horarioId,
            "numeroAsiento" to numeroAsiento,
            "codigoQR" to codigoQR,
            "precioTotal" to precioTotal,
            "metodoPago" to metodoPago,
            "estado" to estado,
            "fechaCompra" to fechaCompra,
            "fechaViaje" to fechaViaje
        )
    }

    // Función para formatear fecha de compra
    fun fechaCompraFormateada(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "PE"))
        return sdf.format(fechaCompra.toDate())
    }

    // Función para formatear fecha de viaje
    fun fechaViajeFormateada(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("es", "PE"))
        return sdf.format(fechaViaje.toDate())
    }

    // Función para formatear precio
    fun precioFormateado(): String = "S/ %.2f".format(precioTotal)

    // Obtener color según estado
    fun getEstadoColor(): Long {
        return when (estado) {
            "pagado" -> 0xFF4CAF50 // Verde
            "usado" -> 0xFF9E9E9E // Gris
            "cancelado" -> 0xFFF44336 // Rojo
            else -> 0xFFFF9800 // Naranja (pendiente)
        }
    }

    // Obtener texto del estado
    fun getEstadoTexto(): String {
        return when (estado) {
            "pagado" -> "Confirmado"
            "usado" -> "Usado"
            "cancelado" -> "Cancelado"
            else -> "Pendiente"
        }
    }
}