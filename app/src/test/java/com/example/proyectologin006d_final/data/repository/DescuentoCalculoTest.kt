package com.example.proyectologin006d_final.data.repository

import com.example.proyectologin006d_final.data.model.Usuario
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

/**
 * Pruebas unitarias para el cálculo de descuentos basados en edad
 */
class DescuentoCalculoTest {

    private lateinit var usuarioRepository: UsuarioRepository
    private lateinit var authRepository: AuthRepository

    @Before
    fun setup() {
        usuarioRepository = mock()
        authRepository = AuthRepository(usuarioRepository)
    }

    @Test
    fun `test register - usuario mayor de 50 años recibe descuento 50`() = runBlocking {
        // Given: Usuario nacido en 1970 (más de 50 años)
        val correo = "usuario@gmail.com"
        val fechaNacimiento = "1970-05-15" // Formato YYYY-MM-DD
        
        whenever(usuarioRepository.obtenerUsuarioPorCorreo(correo)).thenReturn(null)
        whenever(usuarioRepository.insertarUsuario(any())).thenAnswer { }

        val usuario = Usuario(
            correo = correo,
            run = "",
            nombre = "Usuario Mayor",
            password = "password123",
            fecha = fechaNacimiento,
            isAdmin = false
        )

        // When
        val result = authRepository.register(usuario)

        // Then
        assertTrue(result is RegisterResult.Success)
        val successResult = result as RegisterResult.Success
        assertTrue("Debe tener descuento del 50%", successResult.descuento50)
        assertEquals(50, successResult.usuario.descuento)
    }

    @Test
    fun `test register - usuario menor de 50 años no recibe descuento 50`() = runBlocking {
        // Given: Usuario nacido en 2000 (menos de 50 años)
        val correo = "usuario@gmail.com"
        val fechaNacimiento = "2000-05-15"
        
        whenever(usuarioRepository.obtenerUsuarioPorCorreo(correo)).thenReturn(null)
        whenever(usuarioRepository.insertarUsuario(any())).thenAnswer { }

        val usuario = Usuario(
            correo = correo,
            run = "",
            nombre = "Usuario Joven",
            password = "password123",
            fecha = fechaNacimiento,
            isAdmin = false
        )

        // When
        val result = authRepository.register(usuario)

        // Then
        assertTrue(result is RegisterResult.Success)
        val successResult = result as RegisterResult.Success
        assertFalse("No debe tener descuento del 50%", successResult.descuento50)
        assertEquals(0, successResult.usuario.descuento)
    }

    @Test
    fun `test register - fecha formato DD-MM-YYYY funciona correctamente`() = runBlocking {
        // Given: Usuario con fecha en formato DD/MM/YYYY
        val correo = "usuario@gmail.com"
        val fechaNacimiento = "15/05/1970" // Formato DD/MM/YYYY
        
        whenever(usuarioRepository.obtenerUsuarioPorCorreo(correo)).thenReturn(null)
        whenever(usuarioRepository.insertarUsuario(any())).thenAnswer { }

        val usuario = Usuario(
            correo = correo,
            run = "",
            nombre = "Usuario Test",
            password = "password123",
            fecha = fechaNacimiento,
            isAdmin = false
        )

        // When
        val result = authRepository.register(usuario)

        // Then
        assertTrue(result is RegisterResult.Success)
        val successResult = result as RegisterResult.Success
        assertTrue("Debe calcular correctamente el descuento con formato DD/MM/YYYY", 
                   successResult.descuento50)
    }

    @Test
    fun `test register - fecha vacia no aplica descuento`() = runBlocking {
        // Given: Usuario sin fecha de nacimiento
        val correo = "usuario@gmail.com"
        
        whenever(usuarioRepository.obtenerUsuarioPorCorreo(correo)).thenReturn(null)
        whenever(usuarioRepository.insertarUsuario(any())).thenAnswer { }

        val usuario = Usuario(
            correo = correo,
            run = "",
            nombre = "Usuario Test",
            password = "password123",
            fecha = "", // Fecha vacía
            isAdmin = false
        )

        // When
        val result = authRepository.register(usuario)

        // Then
        assertTrue(result is RegisterResult.Success)
        val successResult = result as RegisterResult.Success
        assertFalse("No debe tener descuento si la fecha está vacía", successResult.descuento50)
        assertEquals(0, successResult.usuario.descuento)
    }

    @Test
    fun `test register - codigo FELICES50 aplica descuento 10`() = runBlocking {
        // Given: Usuario con código de descuento
        val correo = "usuario@gmail.com"
        
        whenever(usuarioRepository.obtenerUsuarioPorCorreo(correo)).thenReturn(null)
        whenever(usuarioRepository.insertarUsuario(any())).thenAnswer { }

        val usuario = Usuario(
            correo = correo,
            run = "",
            nombre = "Usuario Test",
            password = "password123",
            fecha = "2000-01-01",
            codigoUsado = "FELICES50",
            isAdmin = false
        )

        // When
        val result = authRepository.register(usuario)

        // Then
        assertTrue(result is RegisterResult.Success)
        val successResult = result as RegisterResult.Success
        assertTrue("Debe tener descuento del 10% por código", successResult.descuento10)
        assertEquals(10, successResult.usuario.descuento)
    }
}

