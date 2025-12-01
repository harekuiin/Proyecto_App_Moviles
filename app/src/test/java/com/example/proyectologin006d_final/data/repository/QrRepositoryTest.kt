package com.example.proyectologin006d_final.data.repository

import com.example.proyectologin006d_final.data.model.QrResult
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class QrRepositoryTest {

    private lateinit var qrRepository: QrRepository

    @Before
    fun setup() {
        qrRepository = QrRepository()
    }

    @Test
    fun `test validarCodigoDescuento - codigo FELICES50 valido`() {
        assertTrue(qrRepository.validarCodigoDescuento("FELICES50"))
    }

    @Test
    fun `test validarCodigoDescuento - codigo FELICES50 en minusculas`() {
        assertTrue(qrRepository.validarCodigoDescuento("felices50"))
    }

    @Test
    fun `test validarCodigoDescuento - codigo FELICES50 en mayusculas y minusculas`() {
        assertTrue(qrRepository.validarCodigoDescuento("FeLiCeS50"))
    }

    @Test
    fun `test validarCodigoDescuento - codigo invalido`() {
        assertFalse(qrRepository.validarCodigoDescuento("DESCUENTO20"))
    }

    @Test
    fun `test validarCodigoDescuento - codigo vacio`() {
        assertFalse(qrRepository.validarCodigoDescuento(""))
    }

    @Test
    fun `test processQrContent - procesa contenido correctamente`() {
        val contenido = "https://example.com/qr"
        val result = qrRepository.processQrContent(contenido)
        
        assertNotNull(result)
        assertEquals(contenido, result.content)
    }
}

