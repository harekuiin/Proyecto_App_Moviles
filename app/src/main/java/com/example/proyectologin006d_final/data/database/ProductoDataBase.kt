package com.example.proyectologin006d_final.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proyectologin006d_final.data.dao.ProductoDao
import com.example.proyectologin006d_final.data.model.Producto

@Database(
    entities = [Producto::class],
    version=2,
    exportSchema = false // Agregar para evitar el warning
)
abstract class ProductoDatabase: RoomDatabase(){
    abstract fun productoDao(): ProductoDao

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