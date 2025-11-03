package com.example.proyectologin006d_final.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey
    val correo: String,
    val run: String,
    val nombre: String,
    val password: String,
    val fecha: String = "", // Fecha de nacimiento
    val telefono: String = "",
    val comuna: String = "",
    val pais: String = "Chile",
    val descuento: Int = 0,
    val tortaGratis: Boolean = false,
    val codigoUsado: String = "",
    val isAdmin: Boolean = false // Determina si es admin basado en @profesor.duoc.cl
)

