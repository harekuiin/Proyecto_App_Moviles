package com.example.proyectologin006d_final.data.repository

import com.example.proyectologin006d_final.data.model.Usuario
import java.util.Calendar

class AuthRepository(
    private val usuarioRepository: UsuarioRepository
) {
    companion object {
        // Dominios permitidos
        const val DOMAIN_GMAIL = "@gmail.com"
        const val DOMAIN_DUOC = "@duoc.cl"
        const val DOMAIN_PROFESOR = "@profesor.duoc.cl"
        
        val ALLOWED_DOMAINS = listOf(DOMAIN_GMAIL, DOMAIN_DUOC, DOMAIN_PROFESOR)
    }
    
    fun isAllowedDomain(email: String): Boolean {
        if (email.isBlank()) return false
        val lowerEmail = email.lowercase()
        return ALLOWED_DOMAINS.any { lowerEmail.endsWith(it) }
    }
    
    fun isProfessorDomain(email: String): Boolean {
        if (email.isBlank()) return false
        return email.lowercase().endsWith(DOMAIN_PROFESOR)
    }
    
    suspend fun login(correo: String, password: String, comentario: String = ""): LoginResult {
        if (!isAllowedDomain(correo)) {
            return LoginResult.Error("El correo debe ser @gmail.com, @duoc.cl o @profesor.duoc.cl")
        }
        
        val usuario = usuarioRepository.obtenerUsuarioPorCorreo(correo)
        if (usuario == null) {
            return LoginResult.Error("Usuario no encontrado")
        }
        
        if (usuario.password != password) {
            return LoginResult.Error("Contraseña incorrecta")
        }
        
        // Determinar si es admin basado en el dominio
        val isAdmin = isProfessorDomain(correo) || usuario.isAdmin
        
        // Guardar el comentario en el usuario
        val usuarioActualizado = usuario.copy(isAdmin = isAdmin, comentario = comentario)
        usuarioRepository.actualizarUsuario(usuarioActualizado)
        
        return LoginResult.Success(usuarioActualizado)
    }
    
    suspend fun register(usuario: Usuario): RegisterResult {
        if (!isAllowedDomain(usuario.correo)) {
            return RegisterResult.Error("El correo debe ser @gmail.com, @duoc.cl o @profesor.duoc.cl")
        }
        
        // Verificar si el usuario ya existe
        val usuarioExistente = usuarioRepository.obtenerUsuarioPorCorreo(usuario.correo)
        if (usuarioExistente != null) {
            return RegisterResult.Error("El correo ya está registrado")
        }
        
        if (usuario.password.length < 6) {
            return RegisterResult.Error("La contraseña debe tener al menos 6 caracteres")
        }
        
        // Determinar si es admin basado en el dominio
        val isAdmin = isProfessorDomain(usuario.correo)
        val usuarioConAdmin = usuario.copy(isAdmin = isAdmin)
        
        // Calcular descuentos
        val descuento50 = calcularDescuento50(usuario.fecha)
        val descuento10 = usuario.codigoUsado == "FELICES50"
        val esEstudianteDuoc = usuario.correo.lowercase().endsWith("@duoc.cl") || 
                               usuario.correo.lowercase().endsWith("@duocuc.cl")
        
        val descuentoFinal = when {
            descuento50 -> 50
            descuento10 -> 10
            else -> 0
        }
        
        val usuarioFinal = usuarioConAdmin.copy(
            descuento = descuentoFinal,
            tortaGratis = esEstudianteDuoc
        )
        
        usuarioRepository.insertarUsuario(usuarioFinal)
        
        return RegisterResult.Success(
            descuento50 = descuento50,
            descuento10 = descuento10,
            esEstudianteDuoc = esEstudianteDuoc,
            usuario = usuarioFinal
        )
    }
    
    private fun calcularDescuento50(fechaNacimiento: String): Boolean {
        if (fechaNacimiento.isBlank()) return false
        try {
            // Formato esperado: YYYY-MM-DD (formato ISO) o DD/MM/YYYY
            val partes = if (fechaNacimiento.contains("/")) {
                fechaNacimiento.split("/")
            } else {
                fechaNacimiento.split("-")
            }
            
            if (partes.size < 3) return false
            
            // Determinar si el formato es YYYY-MM-DD o DD/MM/YYYY
            val anio = when {
                partes[0].length == 4 -> partes[0].toInt() // YYYY-MM-DD
                partes[2].length == 4 -> partes[2].toInt() // DD/MM/YYYY
                else -> return false
            }
            
            val anioActual = Calendar.getInstance().get(Calendar.YEAR)
            val edad = anioActual - anio
            return edad >= 50
        } catch (e: Exception) {
            return false
        }
    }
}

sealed class LoginResult {
    data class Success(val usuario: Usuario) : LoginResult()
    data class Error(val mensaje: String) : LoginResult()
}

sealed class RegisterResult {
    data class Success(
        val descuento50: Boolean,
        val descuento10: Boolean,
        val esEstudianteDuoc: Boolean,
        val usuario: Usuario
    ) : RegisterResult()
    data class Error(val mensaje: String) : RegisterResult()
}
