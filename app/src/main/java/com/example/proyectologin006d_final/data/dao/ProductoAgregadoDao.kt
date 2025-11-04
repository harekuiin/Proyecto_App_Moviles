package com.example.proyectologin006d_final.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.proyectologin006d_final.data.model.ProductoAgregado
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoAgregadoDao {
    @Insert
    suspend fun insertarProductoAgregado(productoAgregado: ProductoAgregado)

    @Query("SELECT * FROM productos_agregados WHERE correoUsuario = :correo")
    fun obtenerProductosAgregadosPorUsuario(correo: String): Flow<List<ProductoAgregado>>

    @Query("SELECT * FROM productos_agregados")
    fun obtenerTodosLosProductosAgregados(): Flow<List<ProductoAgregado>>

    @Query("SELECT * FROM productos_agregados WHERE idProducto = :idProducto AND correoUsuario = :correo")
    suspend fun existeProductoAgregado(idProducto: String, correo: String): ProductoAgregado?
}

