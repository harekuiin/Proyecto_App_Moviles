package com.example.proyectologin006d_final.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.proyectologin006d_final.data.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao{

    @Insert
    suspend fun insertarProducto(producto: Producto)

    @Insert
    suspend fun insertarProductos(productos: List<Producto>)

    @Query("SELECT * FROM productos")
    fun obtenerProductos(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE codigo = :codigo")
    suspend fun obtenerProductoPorCodigo(codigo: String): Producto?

    @Query("SELECT * FROM productos WHERE categoria = :categoria")
    fun obtenerProductosPorCategoria(categoria: String): Flow<List<Producto>>
}