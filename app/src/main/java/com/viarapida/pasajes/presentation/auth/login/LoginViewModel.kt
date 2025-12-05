package com.viarapida.pasajes.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viarapida.pasajes.data.model.Usuario
import com.viarapida.pasajes.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val usuario: Usuario? = null
)

class LoginViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState(isLoading = true)

            val result = authRepository.loginUser(email, password)

            result.onSuccess { usuario ->
                _loginState.value = LoginState(
                    isLoading = false,
                    isSuccess = true,
                    usuario = usuario
                )
            }.onFailure { exception ->
                _loginState.value = LoginState(
                    isLoading = false,
                    isSuccess = false,
                    error = getErrorMessage(exception)
                )
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState()
    }

    private fun getErrorMessage(exception: Throwable): String {
        return when {
            exception.message?.contains("password") == true -> "Contraseña incorrecta"
            exception.message?.contains("user") == true -> "Usuario no encontrado"
            exception.message?.contains("network") == true -> "Error de conexión"
            else -> "Error al iniciar sesión: ${exception.message}"
        }
    }
}