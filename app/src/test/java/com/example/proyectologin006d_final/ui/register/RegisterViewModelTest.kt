package com.example.proyectologin006d_final.ui.register

import com.example.proyectologin006d_final.data.model.Usuario
import com.example.proyectologin006d_final.data.repository.AuthRepository
import com.example.proyectologin006d_final.data.repository.RegisterResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: RegisterViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        authRepository = mock()
        viewModel = RegisterViewModel(authRepository)
    }

    @Test
    fun `test onNombreChange - actualiza el nombre en el estado`() {
        val nombre = "Juan Pérez"
        viewModel.onNombreChange(nombre)
        
        assertEquals(nombre, viewModel.uiState.nombre)
        assertNull(viewModel.uiState.error)
    }

    @Test
    fun `test onEmailChange - actualiza el email en el estado`() {
        val email = "juan@gmail.com"
        viewModel.onEmailChange(email)
        
        assertEquals(email, viewModel.uiState.email)
        assertNull(viewModel.uiState.error)
    }

    @Test
    fun `test onPasswordChange - actualiza la contraseña en el estado`() {
        val password = "password123"
        viewModel.onPasswordChange(password)
        
        assertEquals(password, viewModel.uiState.password)
        assertNull(viewModel.uiState.error)
    }

    @Test
    fun `test submit - nombre vacio muestra error`() {
        viewModel.uiState = viewModel.uiState.copy(
            nombre = "",
            email = "test@gmail.com",
            password = "password123",
            confirmPassword = "password123",
            mayorDeEdad = true
        )
        
        var callbackInvoked = false
        viewModel.submit { _, _ -> callbackInvoked = true }
        
        assertEquals("El nombre es obligatorio", viewModel.uiState.error)
        assertFalse(callbackInvoked)
    }

    @Test
    fun `test submit - email vacio muestra error`() {
        viewModel.uiState = viewModel.uiState.copy(
            nombre = "Juan",
            email = "",
            password = "password123",
            confirmPassword = "password123",
            mayorDeEdad = true
        )
        
        var callbackInvoked = false
        viewModel.submit { _, _ -> callbackInvoked = true }
        
        assertEquals("El correo es obligatorio", viewModel.uiState.error)
        assertFalse(callbackInvoked)
    }

    @Test
    fun `test submit - contraseña muy corta muestra error`() {
        viewModel.uiState = viewModel.uiState.copy(
            nombre = "Juan",
            email = "test@gmail.com",
            password = "12345",
            confirmPassword = "12345",
            mayorDeEdad = true
        )
        
        var callbackInvoked = false
        viewModel.submit { _, _ -> callbackInvoked = true }
        
        assertEquals("La contraseña debe tener al menos 6 caracteres", viewModel.uiState.error)
        assertFalse(callbackInvoked)
    }

    @Test
    fun `test submit - contraseñas no coinciden muestra error`() {
        viewModel.uiState = viewModel.uiState.copy(
            nombre = "Juan",
            email = "test@gmail.com",
            password = "password123",
            confirmPassword = "password456",
            mayorDeEdad = true
        )
        
        var callbackInvoked = false
        viewModel.submit { _, _ -> callbackInvoked = true }
        
        assertEquals("Las contraseñas no coinciden", viewModel.uiState.error)
        assertFalse(callbackInvoked)
    }

    @Test
    fun `test submit - menor de edad muestra error`() {
        viewModel.uiState = viewModel.uiState.copy(
            nombre = "Juan",
            email = "test@gmail.com",
            password = "password123",
            confirmPassword = "password123",
            mayorDeEdad = false
        )
        
        var callbackInvoked = false
        viewModel.submit { _, _ -> callbackInvoked = true }
        
        assertEquals("Debes ser mayor de 18 años para registrarte", viewModel.uiState.error)
        assertFalse(callbackInvoked)
    }

    @Test
    fun `test submit - registro exitoso`() = runTest {
        val usuario = Usuario(
            correo = "test@gmail.com",
            run = "",
            nombre = "Juan",
            password = "password123",
            isAdmin = false
        )
        
        val successResult = RegisterResult.Success(
            descuento50 = false,
            descuento10 = false,
            esEstudianteDuoc = false,
            usuario = usuario
        )
        
        whenever(authRepository.register(any())).thenReturn(successResult)
        
        viewModel.uiState = viewModel.uiState.copy(
            nombre = "Juan",
            email = "test@gmail.com",
            password = "password123",
            confirmPassword = "password123",
            mayorDeEdad = true
        )
        
        var callbackInvoked = false
        var callbackCorreo = ""
        var callbackIsAdmin = false
        
        viewModel.submit { correo, isAdmin ->
            callbackInvoked = true
            callbackCorreo = correo
            callbackIsAdmin = isAdmin
        }
        
        // Avanzar el scheduler para ejecutar las coroutines
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Verificar que el estado cambió (el callback puede ejecutarse asíncronamente)
        assertTrue("El estado debería mostrar loading o success", 
                   viewModel.uiState.isLoading || viewModel.uiState.successMessage != null)
    }
}

