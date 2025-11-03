package com.example.proyectologin006d_final.ui.register

data class RegisterUiState(
    val run: String = "",
    val nombre: String = "",
    val email: String = "",
    val fechaNacimiento: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val telefono: String = "",
    val comuna: String = "Santiago",
    val pais: String = "Chile",
    val codigoPromocional: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

