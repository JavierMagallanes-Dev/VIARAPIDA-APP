package com.viarapida.pasajes.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.viarapida.pasajes.data.model.Usuario
import com.google.firebase.Timestamp
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Obtener usuario actual
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    // Verificar si está logueado
    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    // Registro de usuario
    suspend fun registerUser(
        nombre: String,
        apellido: String,
        dni: String,
        telefono: String,
        email: String,
        password: String
    ): Result<Usuario> {
        return try {
            // Crear usuario en Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                // Crear objeto Usuario
                val usuario = Usuario(
                    id = firebaseUser.uid,
                    nombre = nombre,
                    apellido = apellido,
                    email = email,
                    telefono = telefono,
                    dni = dni,
                    fotoPerfil = "",
                    fechaRegistro = Timestamp.now()
                )

                // Guardar en Firestore
                firestore.collection("usuarios")
                    .document(firebaseUser.uid)
                    .set(usuario.toMap())
                    .await()

                Result.success(usuario)
            } else {
                Result.failure(Exception("Error al crear usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Login de usuario
    suspend fun loginUser(email: String, password: String): Result<Usuario> {
        return try {
            // Autenticar con Firebase Auth
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                // Obtener datos del usuario desde Firestore
                val documentSnapshot = firestore.collection("usuarios")
                    .document(firebaseUser.uid)
                    .get()
                    .await()

                if (documentSnapshot.exists()) {
                    val usuario = documentSnapshot.toObject(Usuario::class.java)
                    if (usuario != null) {
                        Result.success(usuario)
                    } else {
                        Result.failure(Exception("Error al obtener datos del usuario"))
                    }
                } else {
                    Result.failure(Exception("Usuario no encontrado en la base de datos"))
                }
            } else {
                Result.failure(Exception("Error al iniciar sesión"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Cerrar sesión
    fun logout() {
        auth.signOut()
    }

    // Obtener datos del usuario actual
    suspend fun getCurrentUserData(): Result<Usuario> {
        return try {
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                val documentSnapshot = firestore.collection("usuarios")
                    .document(firebaseUser.uid)
                    .get()
                    .await()

                if (documentSnapshot.exists()) {
                    val usuario = documentSnapshot.toObject(Usuario::class.java)
                    if (usuario != null) {
                        Result.success(usuario)
                    } else {
                        Result.failure(Exception("Error al obtener datos"))
                    }
                } else {
                    Result.failure(Exception("Usuario no encontrado"))
                }
            } else {
                Result.failure(Exception("No hay usuario autenticado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Actualizar datos del usuario
    suspend fun updateUserData(usuario: Usuario): Result<Boolean> {
        return try {
            firestore.collection("usuarios")
                .document(usuario.id)
                .set(usuario.toMap())
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Restablecer contraseña
    suspend fun resetPassword(email: String): Result<Boolean> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}