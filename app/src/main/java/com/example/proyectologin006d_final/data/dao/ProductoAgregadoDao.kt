package com.example.proyectologin006d_final.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.proyectologin006d_final.data.model.ProductoAgregado
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoAgregadoDao {
    @Insert
    suspend fun insertarProductoAgregado(productoAgregado: ProductoAgregado)

    @Update
    suspend fun actualizarProductoAgregado(productoAgregado: ProductoAgregado)

    @Delete
    suspend fun eliminarProductoAgregado(productoAgregado: ProductoAgregado)

    @Query("SELECT * FROM productos_agregados WHERE correoUsuario = :correo")
    fun obtenerProductosAgregadosPorUsuario(correo: String): Flow<List<ProductoAgregado>>

    @Query("SELECT * FROM productos_agregados")
    fun obtenerTodosLosProductosAgregados(): Flow<List<ProductoAgregado>>

    @Query("SELECT * FROM productos_agregados WHERE idProducto = :idProducto AND correoUsuario = :correo")
    suspend fun existeProductoAgregado(idProducto: String, correo: String): ProductoAgregado?

    @Query("SELECT * FROM productos_agregados WHERE id = :id")
    suspend fun obtenerProductoAgregadoPorId(id: Long): ProductoAgregado?

    @Query("DELETE FROM productos_agregados WHERE id = :id")
    suspend fun eliminarProductoAgregadoPorId(id: Long)

    @Query("UPDATE productos_agregados SET cantidad = :cantidad WHERE id = :id")
    suspend fun actualizarCantidad(id: Long, cantidad: Int)

    @Query("DELETE FROM productos_agregados WHERE correoUsuario = :correo")
    suspend fun vaciarCarrito(correo: String)
}

