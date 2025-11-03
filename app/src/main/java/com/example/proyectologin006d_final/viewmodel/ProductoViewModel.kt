package com.example.proyectologin006d_final.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectologin006d_final.data.database.ProductoDatabase
import com.example.proyectologin006d_final.data.model.Producto
import com.example.proyectologin006d_final.data.repository.ProductoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductoViewModel(application: Application) : AndroidViewModel(application) {
    private val database = ProductoDatabase.getDatabase(application)
    private val repository = ProductoRepository(database.productoDao())

    val productos: StateFlow<List<Producto>> = repository.obtenerProductos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Inicializar productos si la base de datos está vacía
        viewModelScope.launch {
            val productosActuales = repository.obtenerProductos().first()
            if (productosActuales.isEmpty()) {
                inicializarProductos()
            }
        }
    }

    private suspend fun inicializarProductos() {
        val productosIniciales = listOf(
            Producto("TC001", "Tortas Cuadradas", "Torta Cuadrada de Chocolate", "Deliciosa torta de chocolate con capas de ganache y un toque de avellanas. Personalizable con mensajes especiales.", 45000, ""),
            Producto("TC002", "Tortas Cuadradas", "Torta Cuadrada de Frutas", "Una mezcla de frutas frescas y crema chantilly sobre un suave bizcocho de vainilla, ideal para celebraciones.", 50000, ""),
            Producto("TT001", "Tortas Circulares", "Torta Circular de Vainilla", "Bizcocho de vainilla clásico relleno con crema pastelera y cubierto con un glaseado dulce, perfecto para cualquier ocasión.", 40000, ""),
            Producto("TT002", "Tortas Circulares", "Torta Circular de Manjar", "Torta tradicional chilena con manjar y nueces, un deleite para los amantes de los sabores dulces y clásicos.", 42000, ""),
            Producto("PI001", "Postres Individuales", "Mousse de Chocolate", "Postre individual cremoso y suave, hecho con chocolate de alta calidad, ideal para los amantes del chocolate.", 5000, ""),
            Producto("PI002", "Postres Individuales", "Tiramisú Clásico", "Un postre italiano individual con capas de café, mascarpone y cacao, perfecto para finalizar cualquier comida.", 5500, ""),
            Producto("PSA001", "Productos Sin Azúcar", "Torta Sin Azúcar de Naranja", "Torta ligera y deliciosa, endulzada naturalmente, ideal para quienes buscan opciones más saludables.", 48000, ""),
            Producto("PSA002", "Productos Sin Azúcar", "Cheesecake Sin Azúcar", "Suave y cremoso, este cheesecake es una opción perfecta para disfrutar sin culpa.", 47000, ""),
            Producto("PT001", "Pastelería Tradicional", "Empanada de Manzana", "Pastelería tradicional rellena de manzanas especiadas, perfecta para un dulce desayuno o merienda.", 3000, ""),
            Producto("PT002", "Pastelería Tradicional", "Tarta de Santiago", "Tradicional tarta española hecha con almendras, azúcar, y huevos, una delicia para los amantes de los postres clásicos.", 6000, ""),
            Producto("PG001", "Productos Sin Gluten", "Brownie Sin Gluten", "Rico y denso, este brownie es perfecto para quienes necesitan evitar el gluten sin sacrificar el sabor.", 4000, ""),
            Producto("PG002", "Productos Sin Gluten", "Pan Sin Gluten", "Suave y esponjoso, ideal para sándwiches o para acompañar cualquier comida.", 3500, ""),
            Producto("PV001", "Productos Vegana", "Torta Vegana de Chocolate", "Torta de chocolate húmeda y deliciosa, hecha sin productos de origen animal, perfecta para veganos.", 50000, ""),
            Producto("PV002", "Productos Vegana", "Galletas Veganas de Avena", "Crujientes y sabrosas, estas galletas son una excelente opción para un snack saludable y vegano.", 4500, ""),
            Producto("TE001", "Tortas Especiales", "Torta Especial de Cumpleaños", "Diseñada especialmente para celebraciones, personalizable con decoraciones y mensajes únicos.", 55000, ""),
            Producto("TE002", "Tortas Especiales", "Torta Especial de Boda", "Elegante y deliciosa, esta torta está diseñada para ser el centro de atención en cualquier boda.", 60000, "")
        )
        repository.insertarProductos(productosIniciales)
    }

    suspend fun guardarProducto(producto: Producto) {
        repository.insertarProducto(producto)
    }
}
