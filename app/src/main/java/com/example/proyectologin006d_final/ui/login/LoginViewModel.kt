package com.example.proyectologin006d_final.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.proyectologin006d_final.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onCorreoChange(value: String) {
        uiState = uiState.copy(correo = value, error = null)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, error = null)
    }

    fun onComentarioChange(value: String) {
        uiState = uiState.copy(comentario = value)
    }

    fun submit(onSuccess: (String, Boolean) -> Unit) {
        uiState = uiState.copy(isLoading = true, error = null)
        
        viewModelScope.launch {
            val result = repo.login(uiState.correo.trim(), uiState.password, uiState.comentario)
            uiState = uiState.copy(isLoading = false)
            
            when (result) {
                is com.example.proyectologin006d_final.data.repository.LoginResult.Success -> {
                    onSuccess(result.usuario.correo, result.usuario.isAdmin)
                }
                is com.example.proyectologin006d_final.data.repository.LoginResult.Error -> {
                    uiState = uiState.copy(error = result.mensaje)
                }
            }
        }
    }
}
