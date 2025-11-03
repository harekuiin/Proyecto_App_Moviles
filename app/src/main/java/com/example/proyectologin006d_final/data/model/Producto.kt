package com.example.proyectologin006d_final.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = false)
    val id: String, // ID del producto (ej: TC001)
    val categoria: String,
    val nombre: String,
    val descripcion: String,
    val precio: Int, // Precio en CLP
    val imagen_principal: String = "", // Ruta de imagen principal
    val imagen1: String = "",
    val imagen2: String = "",
    val imagen3: String = "",
    val stock: Int = 10 // Stock disponible
)