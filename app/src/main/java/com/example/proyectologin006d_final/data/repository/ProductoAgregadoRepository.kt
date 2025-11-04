package com.example.proyectologin006d_final.data.repository

import com.example.proyectologin006d_final.data.dao.ProductoAgregadoDao
import com.example.proyectologin006d_final.data.model.ProductoAgregado
import kotlinx.coroutines.flow.Flow

class ProductoAgregadoRepository(private val productoAgregadoDao: ProductoAgregadoDao) {

    suspend fun insertarProductoAgregado(productoAgregado: ProductoAgregado) {
        productoAgregadoDao.insertarProductoAgregado(productoAgregado)
    }

    fun obtenerProductosAgregadosPorUsuario(correo: String): Flow<List<ProductoAgregado>> {
        return productoAgregadoDao.obtenerProductosAgregadosPorUsuario(correo)
    }

    suspend fun existeProductoAgregado(idProducto: String, correo: String): Boolean {
        return productoAgregadoDao.existeProductoAgregado(idProducto, correo) != null
    }
}

