package com.example.proyectologin006d_final.data.repository

import com.example.proyectologin006d_final.data.model.Credential
import java.util.Calendar


class AuthRepository(
    private val validCredential: Credential = Credential.Admin
) {
    private val registeredUsers = mutableMapOf<String, String>() // username -> password
    
    fun login(username: String, password: String): Boolean {
        // Verificar si es admin o usuario registrado
        return (username == validCredential.username && password == validCredential.password) ||
               (registeredUsers[username] == password)
    }
    
    fun register(
        username: String,
        password: String,
        email: String,
        fechaNacimiento: String,
        codigoPromocional: String?
    ): RegisterResult {
        if (registeredUsers.containsKey(username)) {
            return RegisterResult.Error("El usuario ya existe")
        }
        
        if (password.length < 6) {
            return RegisterResult.Error("La contraseña debe tener al menos 6 caracteres")
        }
        
        registeredUsers[username] = password
        
        // Calcular descuentos
        val descuento50 = calcularDescuento50(fechaNacimiento)
        val descuento10 = codigoPromocional == "FELICES50"
        val esEstudianteDuoc = email.endsWith("@duoc.cl") || email.endsWith("@duocuc.cl")
        
        return RegisterResult.Success(
            descuento50 = descuento50,
            descuento10 = descuento10,
            esEstudianteDuoc = esEstudianteDuoc
        )
    }
    
    private fun calcularDescuento50(fechaNacimiento: String): Boolean {
        // Formato esperado: DD/MM/YYYY
        try {
            val partes = fechaNacimiento.split("/")
            if (partes.size != 3) return false
            
            val anio = partes[2].toInt()
            val anioActual = Calendar.getInstance().get(Calendar.YEAR)
            val edad = anioActual - anio
            return edad >= 50
        } catch (e: Exception) {
            return false
        }
    }
}

sealed class RegisterResult {
    data class Success(
        val descuento50: Boolean,
        val descuento10: Boolean,
        val esEstudianteDuoc: Boolean
    ) : RegisterResult()
    data class Error(val mensaje: String) : RegisterResult()
}
