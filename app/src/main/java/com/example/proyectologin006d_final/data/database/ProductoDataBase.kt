package com.example.proyectologin006d_final.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proyectologin006d_final.data.dao.ProductoAgregadoDao
import com.example.proyectologin006d_final.data.dao.ProductoDao
import com.example.proyectologin006d_final.data.dao.UsuarioDao
import com.example.proyectologin006d_final.data.model.Producto
import com.example.proyectologin006d_final.data.model.ProductoAgregado
import com.example.proyectologin006d_final.data.model.Usuario

@Database(
    entities = [Producto::class, Usuario::class, ProductoAgregado::class],
    version = 5, // Incrementado para agregar campo comentario en ProductoAgregado
    exportSchema = false // Agregar para evitar el warning
)
abstract class ProductoDatabase: RoomDatabase(){
    abstract fun productoDao(): ProductoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoAgregadoDao(): ProductoAgregadoDao

    companion object{
        @Volatile
        private var INSTANCE: ProductoDatabase?=null
        fun getDatabase(context: Context): ProductoDatabase{
            return INSTANCE?: synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    ProductoDatabase::class.java,
                    "producto_database"
                )
                .fallbackToDestructiveMigration() // Para desarrollo: permite recrear la BD si cambia el esquema
                .build() // fin Room
                INSTANCE=instance
                instance

            }//fin return
        }// fin getDatabase

    }// fin companion


}// fin abstract