package com.example.proyectologin006d_final.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.proyectologin006d_final.data.model.QrResult
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QrViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: QrViewModel
    private lateinit var qrResultObserver: Observer<QrResult?>

    @Before
    fun setup() {
        viewModel = QrViewModel()
    }

    @Test
    fun `test onQrDetected - actualiza qrResult correctamente`() {
        val contenido = "https://example.com/qr"
        
        viewModel.onQrDetected(contenido)
        
        val result = viewModel.qrResult.value
        assertNotNull(result)
        assertEquals(contenido, result?.content)
    }

    @Test
    fun `test clearResult - limpia el resultado`() {
        viewModel.onQrDetected("test content")
        assertNotNull(viewModel.qrResult.value)
        
        viewModel.clearResult()
        
        assertNull(viewModel.qrResult.value)
    }

    @Test
    fun `test validarCodigoDescuento - codigo valido`() {
        assertTrue(viewModel.validarCodigoDescuento("FELICES50"))
    }

    @Test
    fun `test validarCodigoDescuento - codigo invalido`() {
        assertFalse(viewModel.validarCodigoDescuento("INVALIDO"))
    }

    @Test
    fun `test validarCodigoDescuento - codigo en minusculas`() {
        assertTrue(viewModel.validarCodigoDescuento("felices50"))
    }
}

