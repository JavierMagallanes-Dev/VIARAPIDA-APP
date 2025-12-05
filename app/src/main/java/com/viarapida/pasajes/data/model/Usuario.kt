package com.viarapida.pasajes.data.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Usuario(
    val id: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val email: String = "",
    val telefono: String = "",
    val dni: String = "",
    val fotoPerfil: String = "",
    val fechaRegistro: Timestamp = Timestamp.now()
) : Parcelable {

    // Constructor sin argumentos requerido por Firebase
    constructor() : this(
        id = "",
        nombre = "",
        apellido = "",
        email = "",
        telefono = "",
        dni = "",
        fotoPerfil = "",
        fechaRegistro = Timestamp.now()
    )

    // Función para convertir a Map para Firebase
    fun toMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "nombre" to nombre,
            "apellido" to apellido,
            "email" to email,
            "telefono" to telefono,
            "dni" to dni,
            "fotoPerfil" to fotoPerfil,
            "fechaRegistro" to fechaRegistro
        )
    }

    // Función para obtener nombre completo
    fun nombreCompleto(): String = "$nombre $apellido"
}