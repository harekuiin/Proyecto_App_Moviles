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
    version = 9, // VERSIÓN INCREMENTADA PARA FORZAR LA RECREACIÓN DE LA BASE DE DATOS
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

    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                // Obtenemos la instancia de la BD de forma segura dentro de la corutina
                val database = getDatabase(context)
                val usuarioDao = database.usuarioDao()
                
                // Solo insertamos el usuario admin. Los productos se manejan en ProductoViewModel 
                // para asegurar consistencia con las imágenes de assets
                usuarioDao.insertarUsuario(
                    Usuario(
                        correo = "admin@profesor.duoc.cl",
                        password = "admin",
                        isAdmin = true,
                        run = "1-9",
                        nombre = "Administrador"
                    )
                )
            }
        }
    }
}
