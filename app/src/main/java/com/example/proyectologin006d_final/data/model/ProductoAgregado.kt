package com.example.proyectologin006d_final.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "productos_agregados")
data class ProductoAgregado(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val idProducto: String, // ID del producto (ej: TC001)
    val correoUsuario: String, // Correo del usuario que agregó el producto
    val fechaAgregado: Long = System.currentTimeMillis() // Timestamp de cuando se agregó
)

