package com.example.proyectologin006d_final.data.repository

import com.example.proyectologin006d_final.data.dao.ProductoDao
import com.example.proyectologin006d_final.data.model.Producto
import kotlinx.coroutines.flow.Flow

class ProductoRepository (private val productoDao: ProductoDao){

    suspend fun insertarProducto(producto: Producto){
        productoDao.insertarProducto(producto)
    }

    suspend fun insertarProductos(productos: List<Producto>){
        productoDao.insertarProductos(productos)
    }

    fun obtenerProductos(): Flow<List<Producto>> {
        return productoDao.obtenerProductos()
    }

    suspend fun obtenerProductoPorCodigo(codigo: String): Producto? {
        return productoDao.obtenerProductoPorCodigo(codigo)
    }

    fun obtenerProductosPorCategoria(categoria: String): Flow<List<Producto>> {
        return productoDao.obtenerProductosPorCategoria(categoria)
    }
}