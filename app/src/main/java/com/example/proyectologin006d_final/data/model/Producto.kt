package com.example.proyectologin006d_final.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = false)
    val codigo: String,
    val categoria: String,
    val nombre: String,
    val descripcion: String,
    val precio: Int, // Precio en CLP
    val foto: String = "" // Ruta o nombre del recurso de imagen
)