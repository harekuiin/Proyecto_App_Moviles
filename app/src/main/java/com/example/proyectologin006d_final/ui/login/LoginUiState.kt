package com.example.proyectologin006d_final.ui.login

data class LoginUiState(
    val correo: String = "",
    val password: String = "",
    val comentario: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
