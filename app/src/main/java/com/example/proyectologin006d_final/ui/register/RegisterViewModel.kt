package com.example.proyectologin006d_final.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.proyectologin006d_final.data.model.Usuario
import com.example.proyectologin006d_final.data.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun onRunChange(value: String) {
        uiState = uiState.copy(run = value, error = null)
    }

    fun onNombreChange(value: String) {
        uiState = uiState.copy(nombre = value, error = null)
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

    fun onTelefonoChange(value: String) {
        uiState = uiState.copy(telefono = value, error = null)
    }

    fun onComunaChange(value: String) {
        uiState = uiState.copy(comuna = value, error = null)
    }

    fun onPaisChange(value: String) {
        uiState = uiState.copy(pais = value, error = null)
    }

    fun onCodigoPromocionalChange(value: String) {
        uiState = uiState.copy(codigoPromocional = value, error = null)
    }

    fun submit(onSuccess: (String, Boolean) -> Unit) {
        // Validaciones
        if (uiState.run.isBlank()) {
            uiState = uiState.copy(error = "El RUT es obligatorio")
            return
        }

        if (uiState.nombre.isBlank()) {
            uiState = uiState.copy(error = "El nombre es obligatorio")
            return
        }

        if (uiState.email.isBlank()) {
            uiState = uiState.copy(error = "El correo es obligatorio")
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

        val usuario = Usuario(
            correo = uiState.email.trim(),
            run = uiState.run.trim(),
            nombre = uiState.nombre.trim(),
            password = uiState.password,
            fecha = uiState.fechaNacimiento.trim(),
            telefono = uiState.telefono.trim(),
            comuna = uiState.comuna.trim(),
            pais = uiState.pais.trim(),
            codigoUsado = if (uiState.codigoPromocional.isBlank()) "" else uiState.codigoPromocional.trim()
        )

        viewModelScope.launch {
            val result = repo.register(usuario)
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
                    onSuccess(result.usuario.correo, result.usuario.isAdmin)
                }
                is com.example.proyectologin006d_final.data.repository.RegisterResult.Error -> {
                    uiState = uiState.copy(error = result.mensaje)
                }
            }
        }
    }
}

