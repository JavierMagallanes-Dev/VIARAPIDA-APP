package com.viarapida.pasajes.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.viarapida.pasajes.data.model.Destino
import com.viarapida.pasajes.data.model.Horario
import kotlinx.coroutines.tasks.await

class DestinoRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // Obtener todos los destinos activos
    suspend fun getDestinos(): Result<List<Destino>> {
        return try {
            val snapshot = firestore.collection("destinos")
                .whereEqualTo("activo", true)
                .get()
                .await()

            val destinos = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Destino::class.java)
            }

            Result.success(destinos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Buscar destinos por origen y destino
    suspend fun buscarDestinos(origen: String, destino: String): Result<List<Destino>> {
        return try {
            val snapshot = firestore.collection("destinos")
                .whereEqualTo("origen", origen)
                .whereEqualTo("destino", destino)
                .whereEqualTo("activo", true)
                .get()
                .await()

            val destinos = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Destino::class.java)
            }

            Result.success(destinos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener horarios disponibles para un destino
    suspend fun getHorariosDisponibles(destinoId: String): Result<List<Horario>> {
        return try {
            val snapshot = firestore.collection("horarios")
                .whereEqualTo("destinoId", destinoId)
                .whereEqualTo("estado", "activo")
                .whereGreaterThan("asientosDisponibles", 0)
                .get()
                .await()

            val horarios = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Horario::class.java)
            }

            Result.success(horarios)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener ciudades de origen únicas
    suspend fun getOrigenes(): Result<List<String>> {
        return try {
            val snapshot = firestore.collection("destinos")
                .whereEqualTo("activo", true)
                .get()
                .await()

            val origenes = snapshot.documents
                .mapNotNull { it.getString("origen") }
                .distinct()
                .sorted()

            Result.success(origenes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener ciudades de destino únicas
    suspend fun getDestinos(origen: String): Result<List<String>> {
        return try {
            val snapshot = firestore.collection("destinos")
                .whereEqualTo("origen", origen)
                .whereEqualTo("activo", true)
                .get()
                .await()

            val destinos = snapshot.documents
                .mapNotNull { it.getString("destino") }
                .distinct()
                .sorted()

            Result.success(destinos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Agregar destinos de prueba (solo para desarrollo)
    suspend fun agregarDestinosPrueba(): Result<Boolean> {
        return try {
            val destinos = listOf(
                Destino(
                    id = "1",
                    origen = "Ayacucho",
                    destino = "Lima",
                    precio = 45.0,
                    duracionHoras = 9,
                    distanciaKm = 564.0,
                    imagenUrl = "",
                    activo = true
                ),
                Destino(
                    id = "2",
                    origen = "Ayacucho",
                    destino = "Huancayo",
                    precio = 30.0,
                    duracionHoras = 5,
                    distanciaKm = 255.0,
                    imagenUrl = "",
                    activo = true
                ),
                Destino(
                    id = "3",
                    origen = "Ayacucho",
                    destino = "Cusco",
                    precio = 55.0,
                    duracionHoras = 10,
                    distanciaKm = 598.0,
                    imagenUrl = "",
                    activo = true
                ),
                Destino(
                    id = "4",
                    origen = "Ayacucho",
                    destino = "Andahuaylas",
                    precio = 25.0,
                    duracionHoras = 4,
                    distanciaKm = 200.0,
                    imagenUrl = "",
                    activo = true
                )
            )

            destinos.forEach { destino ->
                firestore.collection("destinos")
                    .document(destino.id)
                    .set(destino.toMap())
                    .await()
            }

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}