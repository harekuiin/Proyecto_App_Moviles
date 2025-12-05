package com.example.proyectologin006d_final.ui.register

import com.example.proyectologin006d_final.data.repository.AuthRepository
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    @MockK
    private lateinit var authRepository: AuthRepository

    @InjectMockKs
    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
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
        viewModel.onNombreChange("")
        viewModel.onEmailChange("test@gmail.com")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("password123")
        viewModel.onMayorDeEdadChange(true)
        
        var callbackInvoked = false
        viewModel.submit { _, _ -> callbackInvoked = true }
        
        assertEquals("El nombre es obligatorio", viewModel.uiState.error)
        assertFalse(callbackInvoked)
    }

    @Test
    fun `test submit - email vacio muestra error`() {
        viewModel.onNombreChange("Juan")
        viewModel.onEmailChange("")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("password123")
        viewModel.onMayorDeEdadChange(true)
        
        var callbackInvoked = false
        viewModel.submit { _, _ -> callbackInvoked = true }
        
        assertEquals("El correo es obligatorio", viewModel.uiState.error)
        assertFalse(callbackInvoked)
    }

    @Test
    fun `test submit - contraseña muy corta muestra error`() {
        viewModel.onNombreChange("Juan")
        viewModel.onEmailChange("test@gmail.com")
        viewModel.onPasswordChange("12345")
        viewModel.onConfirmPasswordChange("12345")
        viewModel.onMayorDeEdadChange(true)
        
        var callbackInvoked = false
        viewModel.submit { _, _ -> callbackInvoked = true }
        
        assertEquals("La contraseña debe tener al menos 6 caracteres", viewModel.uiState.error)
        assertFalse(callbackInvoked)
    }

    @Test
    fun `test submit - contraseñas no coinciden muestra error`() {
        viewModel.onNombreChange("Juan")
        viewModel.onEmailChange("test@gmail.com")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("password456")
        viewModel.onMayorDeEdadChange(true)
        
        var callbackInvoked = false
        viewModel.submit { _, _ -> callbackInvoked = true }
        
        assertEquals("Las contraseñas no coinciden", viewModel.uiState.error)
        assertFalse(callbackInvoked)
    }

    @Test
    fun `test submit - menor de edad muestra error`() {
        viewModel.onNombreChange("Juan")
        viewModel.onEmailChange("test@gmail.com")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("password123")
        viewModel.onMayorDeEdadChange(false)
        
        var callbackInvoked = false
        viewModel.submit { _, _ -> callbackInvoked = true }
        
        assertEquals("Debes ser mayor de 18 años para registrarte", viewModel.uiState.error)
        assertFalse(callbackInvoked)
    }

}
