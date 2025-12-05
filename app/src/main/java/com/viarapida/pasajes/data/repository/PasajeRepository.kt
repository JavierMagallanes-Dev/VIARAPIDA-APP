package com.viarapida.pasajes.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.viarapida.pasajes.data.model.Pasaje
import kotlinx.coroutines.tasks.await

class PasajeRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Crear un nuevo pasaje
    suspend fun crearPasaje(pasaje: Pasaje): Result<String> {
        return try {
            val pasajeId = firestore.collection("pasajes").document().id
            val pasajeConId = pasaje.copy(id = pasajeId)

            firestore.collection("pasajes")
                .document(pasajeId)
                .set(pasajeConId.toMap())
                .await()

            Result.success(pasajeId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener pasajes del usuario actual
    suspend fun getPasajesUsuario(): Result<List<Pasaje>> {
        return try {
            val userId = auth.currentUser?.uid ?: return Result.failure(Exception("Usuario no autenticado"))

            val snapshot = firestore.collection("pasajes")
                .whereEqualTo("usuarioId", userId)
                .orderBy("fechaViaje", Query.Direction.DESCENDING)
                .get()
                .await()

            val pasajes = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Pasaje::class.java)
            }

            Result.success(pasajes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener pasajes próximos
    suspend fun getPasajesProximos(): Result<List<Pasaje>> {
        return try {
            val userId = auth.currentUser?.uid ?: return Result.failure(Exception("Usuario no autenticado"))

            val snapshot = firestore.collection("pasajes")
                .whereEqualTo("usuarioId", userId)
                .whereIn("estado", listOf("pendiente", "pagado"))
                .orderBy("fechaViaje", Query.Direction.ASCENDING)
                .get()
                .await()

            val pasajes = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Pasaje::class.java)
            }

            Result.success(pasajes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener historial de pasajes
    suspend fun getPasajesHistorial(): Result<List<Pasaje>> {
        return try {
            val userId = auth.currentUser?.uid ?: return Result.failure(Exception("Usuario no autenticado"))

            val snapshot = firestore.collection("pasajes")
                .whereEqualTo("usuarioId", userId)
                .whereIn("estado", listOf("usado", "cancelado"))
                .orderBy("fechaViaje", Query.Direction.DESCENDING)
                .get()
                .await()

            val pasajes = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Pasaje::class.java)
            }

            Result.success(pasajes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener un pasaje por ID
    suspend fun getPasajeById(pasajeId: String): Result<Pasaje> {
        return try {
            val documentSnapshot = firestore.collection("pasajes")
                .document(pasajeId)
                .get()
                .await()

            val pasaje = documentSnapshot.toObject(Pasaje::class.java)
            if (pasaje != null) {
                Result.success(pasaje)
            } else {
                Result.failure(Exception("Pasaje no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Cancelar un pasaje
    suspend fun cancelarPasaje(pasajeId: String): Result<Boolean> {
        return try {
            firestore.collection("pasajes")
                .document(pasajeId)
                .update("estado", "cancelado")
                .await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Actualizar estado del pasaje
    suspend fun actualizarEstadoPasaje(pasajeId: String, nuevoEstado: String): Result<Boolean> {
        return try {
            firestore.collection("pasajes")
                .document(pasajeId)
                .update("estado", nuevoEstado)
                .await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}