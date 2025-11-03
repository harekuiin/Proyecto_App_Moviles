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

    fun onNombreChange(value: String) {
        uiState = uiState.copy(nombre = value, error = null)
    }

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value, error = null)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, error = null)
    }

    fun onConfirmPasswordChange(value: String) {
        uiState = uiState.copy(confirmPassword = value, error = null)
    }

    fun onMayorDeEdadChange(value: Boolean) {
        uiState = uiState.copy(mayorDeEdad = value, error = null)
    }

    fun submit(onSuccess: (String, Boolean) -> Unit) {
        // Validaciones
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

        if (uiState.password.length < 6) {
            uiState = uiState.copy(error = "La contraseña debe tener al menos 6 caracteres")
            return
        }

        if (uiState.password != uiState.confirmPassword) {
            uiState = uiState.copy(error = "Las contraseñas no coinciden")
            return
        }

        if (!uiState.mayorDeEdad) {
            uiState = uiState.copy(error = "Debes ser mayor de 18 años para registrarte")
            return
        }

        uiState = uiState.copy(isLoading = true, error = null, successMessage = null)

        // Crear usuario con valores por defecto para campos no requeridos
        val usuario = Usuario(
            correo = uiState.email.trim(),
            run = "", // No requerido
            nombre = uiState.nombre.trim(),
            password = uiState.password,
            fecha = "", // No requerido
            telefono = "", // No requerido
            comuna = "", // No requerido
            pais = "Chile", // Valor por defecto
            codigoUsado = "" // No requerido
        )

        viewModelScope.launch {
            val result = repo.register(usuario)
            uiState = uiState.copy(isLoading = false)

            when (result) {
                is com.example.proyectologin006d_final.data.repository.RegisterResult.Success -> {
                    // Mostrar mensaje de éxito
                    val mensaje = if (result.esEstudianteDuoc) {
                        "¡Registro exitoso! Beneficios: Tortas gratis en tu cumpleaños por ser estudiante Duoc"
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

