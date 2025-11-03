package com.example.proyectologin006d_final.ui.register

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.proyectologin006d_final.data.repository.AuthRepository

class RegisterViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun onUsernameChange(value: String) {
        uiState = uiState.copy(username = value, error = null)
    }

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value, error = null)
    }

    fun onFechaNacimientoChange(value: String) {
        uiState = uiState.copy(fechaNacimiento = value, error = null)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, error = null)
    }

    fun onConfirmPasswordChange(value: String) {
        uiState = uiState.copy(confirmPassword = value, error = null)
    }

    fun onCodigoPromocionalChange(value: String) {
        uiState = uiState.copy(codigoPromocional = value, error = null)
    }

    fun submit(onSuccess: (String) -> Unit) {
        // Validaciones
        if (uiState.username.isBlank()) {
            uiState = uiState.copy(error = "El usuario es obligatorio")
            return
        }

        if (uiState.email.isBlank()) {
            uiState = uiState.copy(error = "El correo es obligatorio")
            return
        }

        if (uiState.fechaNacimiento.isBlank()) {
            uiState = uiState.copy(error = "La fecha de nacimiento es obligatoria")
            return
        }

        if (uiState.password.isBlank()) {
            uiState = uiState.copy(error = "La contraseña es obligatoria")
            return
        }

        if (uiState.password != uiState.confirmPassword) {
            uiState = uiState.copy(error = "Las contraseñas no coinciden")
            return
        }

        uiState = uiState.copy(isLoading = true, error = null, successMessage = null)

        val result = repo.register(
            username = uiState.username.trim(),
            password = uiState.password,
            email = uiState.email.trim(),
            fechaNacimiento = uiState.fechaNacimiento.trim(),
            codigoPromocional = if (uiState.codigoPromocional.isBlank()) null else uiState.codigoPromocional.trim()
        )

        uiState = uiState.copy(isLoading = false)

        when (result) {
            is com.example.proyectologin006d_final.data.repository.RegisterResult.Success -> {
                val mensajes = mutableListOf<String>()
                if (result.descuento50) mensajes.add("50% de descuento por ser mayor de 50 años")
                if (result.descuento10) mensajes.add("10% de descuento de por vida con código FELICES50")
                if (result.esEstudianteDuoc) mensajes.add("Tortas gratis en tu cumpleaños por ser estudiante Duoc")

                val mensaje = if (mensajes.isNotEmpty()) {
                    "¡Registro exitoso! Beneficios: ${mensajes.joinToString(", ")}"
                } else {
                    "¡Registro exitoso!"
                }

                uiState = uiState.copy(successMessage = mensaje)
                onSuccess(uiState.username.trim())
            }
            is com.example.proyectologin006d_final.data.repository.RegisterResult.Error -> {
                uiState = uiState.copy(error = result.mensaje)
            }
        }
    }
}

