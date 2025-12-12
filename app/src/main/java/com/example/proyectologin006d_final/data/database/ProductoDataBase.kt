package com.example.proyectologin006d_final.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.proyectologin006d_final.data.dao.ProductoAgregadoDao
import com.example.proyectologin006d_final.data.dao.ProductoDao
import com.example.proyectologin006d_final.data.dao.UsuarioDao
import com.example.proyectologin006d_final.data.model.Producto
import com.example.proyectologin006d_final.data.model.ProductoAgregado
import com.example.proyectologin006d_final.data.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Producto::class, Usuario::class, ProductoAgregado::class],
    version = 8, // VERSIÓN INCREMENTADA PARA FORZAR LA RECREACIÓN DE LA BASE DE DATOS
    exportSchema = false
)
abstract class ProductoDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoAgregadoDao(): ProductoAgregadoDao

    companion object {
        @Volatile
        private var INSTANCE: ProductoDatabase? = null

        fun getDatabase(context: Context): ProductoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductoDatabase::class.java,
                    "producto_database"
                )
                .fallbackToDestructiveMigration() // Borra la BD si la versión cambia
                .addCallback(DatabaseCallback(context)) // Añade el callback corregido
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // ESTA ES LA IMPLEMENTACIÓN CORRECTA Y ROBUSTA DEL CALLBACK
    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                // Obtenemos la instancia de la BD de forma segura dentro de la corutina
                val database = getDatabase(context)
                val usuarioDao = database.usuarioDao()
                val productoDao = database.productoDao()

                // Este código ahora sí se ejecutará, poblando la base de datos
                usuarioDao.insertarUsuario(
                    Usuario(
                        correo = "admin@profesor.duoc.cl",
                        password = "admin",
                        isAdmin = true,
                        run = "1-9",
                        nombre = "Administrador"
                    )
                )
                productoDao.insertarProductos(
                    listOf(
                        Producto(id = "100", nombre = "Torta de Chocolate", categoria = "Tortas", precio = 25000, descripcion = "Deliciosa torta de chocolate con manjar.", imagen_principal = "torta_chocolate"),
                        Producto(id = "101", nombre = "Pie de Limón", categoria = "Pies", precio = 15000, descripcion = "Refrescante pie de limón con merengue.", imagen_principal = "pie_limon"),
                        Producto(id = "102", nombre = "Kuchen de Manzana", categoria = "Kuchenes", precio = 12000, descripcion = "Tradicional kuchen de manzana con canela.", imagen_principal = "kuchen_manzana"),
                        Producto(id = "103", nombre = "Cheesecake de Frutos Rojos", categoria = "Cheesecakes", precio = 20000, descripcion = "Cremoso cheesecake con salsa de frutos rojos.", imagen_principal = "cheesecake_frutos_rojos"),
                        Producto(id = "104", nombre = "Galletas Surtidas", categoria = "Galletas", precio = 8000, descripcion = "Caja de galletas de mantequilla y chocolate.", imagen_principal = "galletas_surtidas"),
                        Producto(id = "105", nombre = "Brazo de Reina", categoria = "Brazos de Reina", precio = 10000, descripcion = "Clásico brazo de reina con manjar.", imagen_principal = "brazo_reina")
                    )
                )
            }
        }
    }
}
