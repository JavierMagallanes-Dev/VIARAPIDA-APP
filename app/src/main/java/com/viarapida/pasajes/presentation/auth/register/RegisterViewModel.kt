package com.viarapida.pasajes.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viarapida.pasajes.data.model.Usuario
import com.viarapida.pasajes.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegisterState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val usuario: Usuario? = null
)

class RegisterViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(
        nombre: String,
        apellido: String,
        dni: String,
        telefono: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _registerState.value = RegisterState(isLoading = true)

            val result = authRepository.registerUser(
                nombre = nombre,
                apellido = apellido,
                dni = dni,
                telefono = telefono,
                email = email,
                password = password
            )

            result.onSuccess { usuario ->
                _registerState.value = RegisterState(
                    isLoading = false,
                    isSuccess = true,
                    usuario = usuario
                )
            }.onFailure { exception ->
                _registerState.value = RegisterState(
                    isLoading = false,
                    isSuccess = false,
                    error = getErrorMessage(exception)
                )
            }
        }
    }

    fun resetState() {
        _registerState.value = RegisterState()
    }

    private fun getErrorMessage(exception: Throwable): String {
        return when {
            exception.message?.contains("already in use") == true -> "El correo ya está registrado"
            exception.message?.contains("weak-password") == true -> "La contraseña es muy débil"
            exception.message?.contains("invalid-email") == true -> "Correo electrónico inválido"
            exception.message?.contains("network") == true -> "Error de conexión"
            else -> "Error al registrar: ${exception.message}"
        }
    }
}