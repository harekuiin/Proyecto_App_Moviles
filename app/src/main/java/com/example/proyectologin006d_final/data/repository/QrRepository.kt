package com.example.proyectologin006d_final.data.repository

import com.example.proyectologin006d_final.data.model.QrResult

class QrRepository {
    fun processQrContent(content: String): QrResult {
        // Aquí podrías guardar o procesar el QR en una BD o API
        return QrResult(content)
    }
    
    fun validarCodigoDescuento(codigo: String): Boolean {
        // Validar si el código es válido (ej: FELICES50)
        return codigo.uppercase() == "FELICES50"
    }
}

