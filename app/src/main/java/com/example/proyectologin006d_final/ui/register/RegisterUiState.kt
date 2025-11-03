package com.example.proyectologin006d_final.ui.register

data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val fechaNacimiento: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val codigoPromocional: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

