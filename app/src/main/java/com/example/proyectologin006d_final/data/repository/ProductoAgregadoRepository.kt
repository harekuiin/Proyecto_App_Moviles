package com.example.proyectologin006d_final.data.repository

import com.example.proyectologin006d_final.data.dao.ProductoAgregadoDao
import com.example.proyectologin006d_final.data.model.ProductoAgregado
import kotlinx.coroutines.flow.Flow

class ProductoAgregadoRepository(private val productoAgregadoDao: ProductoAgregadoDao) {

    suspend fun insertarProductoAgregado(productoAgregado: ProductoAgregado) {
        productoAgregadoDao.insertarProductoAgregado(productoAgregado)
    }

    suspend fun actualizarProductoAgregado(productoAgregado: ProductoAgregado) {
        productoAgregadoDao.actualizarProductoAgregado(productoAgregado)
    }

    suspend fun eliminarProductoAgregado(productoAgregado: ProductoAgregado) {
        productoAgregadoDao.eliminarProductoAgregado(productoAgregado)
    }

    fun obtenerProductosAgregadosPorUsuario(correo: String): Flow<List<ProductoAgregado>> {
        return productoAgregadoDao.obtenerProductosAgregadosPorUsuario(correo)
    }

    fun obtenerTodosLosProductosAgregados(): Flow<List<ProductoAgregado>> {
        return productoAgregadoDao.obtenerTodosLosProductosAgregados()
    }

    suspend fun existeProductoAgregado(idProducto: String, correo: String): ProductoAgregado? {
        return productoAgregadoDao.existeProductoAgregado(idProducto, correo)
    }

    suspend fun obtenerProductoAgregadoPorId(id: Long): ProductoAgregado? {
        return productoAgregadoDao.obtenerProductoAgregadoPorId(id)
    }

    suspend fun eliminarProductoAgregadoPorId(id: Long) {
        productoAgregadoDao.eliminarProductoAgregadoPorId(id)
    }

    suspend fun actualizarCantidad(id: Long, cantidad: Int) {
        productoAgregadoDao.actualizarCantidad(id, cantidad)
    }

    suspend fun vaciarCarrito(correo: String) {
        productoAgregadoDao.vaciarCarrito(correo)
    }
}

