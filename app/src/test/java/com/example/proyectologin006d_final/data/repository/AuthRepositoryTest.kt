package com.example.proyectologin006d_final.data.repository

import com.example.proyectologin006d_final.data.model.Usuario
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import io.mockk.MockKAnnotations
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthRepositoryTest {

    @MockK
    private lateinit var usuarioRepository: UsuarioRepository

    @InjectMockKs
    private lateinit var authRepository: AuthRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test isAllowedDomain - correo gmail valido`() {
        assertTrue(authRepository.isAllowedDomain("usuario@gmail.com"))
    }

    @Test
    fun `test isAllowedDomain - correo duoc valido`() {
        assertTrue(authRepository.isAllowedDomain("estudiante@duoc.cl"))
    }

    @Test
    fun `test isAllowedDomain - correo profesor valido`() {
        assertTrue(authRepository.isAllowedDomain("profesor@profesor.duoc.cl"))
    }

    @Test
    fun `test isAllowedDomain - correo invalido`() {
        assertFalse(authRepository.isAllowedDomain("usuario@yahoo.com"))
    }

    @Test
    fun `test isAllowedDomain - correo vacio`() {
        assertFalse(authRepository.isAllowedDomain(""))
    }

    @Test
    fun `test isProfessorDomain - correo profesor`() {
        assertTrue(authRepository.isProfessorDomain("profesor@profesor.duoc.cl"))
    }

    @Test
    fun `test isProfessorDomain - correo no profesor`() {
        assertFalse(authRepository.isProfessorDomain("usuario@gmail.com"))
    }

    @Test
    fun `test login - usuario no encontrado`() = runBlocking {
        // Given
        val correo = "usuario@gmail.com"
        coEvery { usuarioRepository.obtenerUsuarioPorCorreo(correo) } returns null

        // When
        val result = authRepository.login(correo, "password123")

        // Then
        assertTrue(result is LoginResult.Error)
        assertEquals("Usuario no encontrado", (result as LoginResult.Error).mensaje)
    }

    @Test
    fun `test login - contraseña incorrecta`() = runBlocking {
        // Given
        val correo = "usuario@gmail.com"
        val usuario = Usuario(
            correo = correo,
            run = "",
            nombre = "Usuario Test",
            password = "password123",
            isAdmin = false
        )
        coEvery { usuarioRepository.obtenerUsuarioPorCorreo(correo) } returns usuario

        // When
        val result = authRepository.login(correo, "passwordIncorrecta")

        // Then
        assertTrue(result is LoginResult.Error)
        assertEquals("Contraseña incorrecta", (result as LoginResult.Error).mensaje)
    }

    @Test
    fun `test login - login exitoso`() = runBlocking {
        // Given
        val correo = "usuario@gmail.com"
        val password = "password123"
        val usuario = Usuario(
            correo = correo,
            run = "",
            nombre = "Usuario Test",
            password = password,
            isAdmin = false
        )
        coEvery { usuarioRepository.obtenerUsuarioPorCorreo(correo) } returns usuario

        // When
        val result = authRepository.login(correo, password)

        // Then
        assertTrue(result is LoginResult.Success)
        val successResult = result as LoginResult.Success
        assertEquals(correo, successResult.usuario.correo)
        assertEquals("Usuario Test", successResult.usuario.nombre)
    }

    @Test
    fun `test login - profesor es admin automaticamente`() = runBlocking {
        // Given
        val correo = "profesor@profesor.duoc.cl"
        val password = "password123"
        val usuario = Usuario(
            correo = correo,
            run = "",
            nombre = "Profesor Test",
            password = password,
            isAdmin = false
        )
        coEvery { usuarioRepository.obtenerUsuarioPorCorreo(correo) } returns usuario

        // When
        val result = authRepository.login(correo, password)

        // Then
        assertTrue(result is LoginResult.Success)
        val successResult = result as LoginResult.Success
        assertTrue(successResult.usuario.isAdmin)
    }

    @Test
    fun `test register - correo ya registrado`() = runBlocking {
        // Given
        val correo = "usuario@gmail.com"
        val usuarioExistente = Usuario(
            correo = correo,
            run = "",
            nombre = "Usuario Existente",
            password = "password123",
            isAdmin = false
        )
        coEvery { usuarioRepository.obtenerUsuarioPorCorreo(correo) } returns usuarioExistente

        val nuevoUsuario = Usuario(
            correo = correo,
            run = "",
            nombre = "Nuevo Usuario",
            password = "password456",
            isAdmin = false
        )

        // When
        val result = authRepository.register(nuevoUsuario)

        // Then
        assertTrue(result is RegisterResult.Error)
        assertEquals("El correo ya está registrado", (result as RegisterResult.Error).mensaje)
    }

    @Test
    fun `test register - contraseña muy corta`() = runBlocking {
        // Given
        val correo = "nuevo@gmail.com"
        coEvery { usuarioRepository.obtenerUsuarioPorCorreo(correo) } returns null

        val nuevoUsuario = Usuario(
            correo = correo,
            run = "",
            nombre = "Nuevo Usuario",
            password = "12345", // Menos de 6 caracteres
            isAdmin = false
        )

        // When
        val result = authRepository.register(nuevoUsuario)

        // Then
        assertTrue(result is RegisterResult.Error)
        assertEquals("La contraseña debe tener al menos 6 caracteres", (result as RegisterResult.Error).mensaje)
    }

    @Test
    fun `test register - registro exitoso estudiante duoc`() = runBlocking {
        // Given
        val correo = "estudiante@duoc.cl"
        coEvery { usuarioRepository.obtenerUsuarioPorCorreo(correo) } returns null
        coEvery { usuarioRepository.insertarUsuario(any()) } just runs

        val nuevoUsuario = Usuario(
            correo = correo,
            run = "",
            nombre = "Estudiante Duoc",
            password = "password123",
            isAdmin = false
        )

        // When
        val result = authRepository.register(nuevoUsuario)

        // Then
        assertTrue(result is RegisterResult.Success)
        val successResult = result as RegisterResult.Success
        assertTrue(successResult.esEstudianteDuoc)
        assertTrue(successResult.usuario.tortaGratis)
        coVerify { usuarioRepository.insertarUsuario(any()) }
    }
}
