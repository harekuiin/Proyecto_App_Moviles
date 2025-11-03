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
            Producto("TC001", "Tortas Cuadradas", "Torta Cuadrada de Chocolate", "Deliciosa torta de chocolate con capas de ganache y un toque de avellanas. Personalizable con mensajes especiales.", 45000, "/img/torta_cuadrada_chocolate.jpg", "/img/tcc_mini_1.webp", "/img/tcc_mini_2.webp", "/img/tcc_mini_3.jpg", 10),
            Producto("TC002", "Tortas Cuadradas", "Torta Cuadrada de Frutas ", "Una mezcla de frutas frescas y crema chantilly sobre un suave bizcocho de vainilla, ideal para celebraciones.", 50000, "/img/torta_cuadrada_frutas.png", "/img/tcf_mini_1.png", "/img/tcf_mini_2.jpg", "/img/tcf_mini_3.jpg", 10),
            Producto("TT001", "Tortas Circulares", "Torta Circular de Vainilla", "Bizcocho de vainilla clásico relleno con crema pastelera y cubierto con un glaseado dulce, perfecto para cualquier ocasión.", 40000, "/img/torta_circular_vainilla.jpg", "/img/tcv_mini_1.jpg", "/img/tcv_mini_2.webp", "/img/tcv_mini_3.avif", 10),
            Producto("TT002", "Tortas Circulares", "Torta Circular de Manjar", " Torta tradicional chilena con manjar y nueces, un deleite para los amantes de los sabores dulces y clásicos.", 42000, "/img/torta_circular_manjar.png", "/img/tcm_mini_1.jfif", "/img/tcm_mini_2.jpg", "/img/tcm_mini_3.jfif", 10),
            Producto("PI001", "Postres Individuales", "Mousse de Chocolate", "Postre individual cremoso y suave, hecho con chocolate de alta calidad, ideal para los amantes del chocolate.", 5000, "/img/mousse_chocolate.png", "/img/m_mini_1.jpg", "/img/m_mini_2.jpg", "/img/m_mini_3.avif", 10),
            Producto("PI002", "Postres Individuales", "Tiramisú Clásico", "Un postre italiano individual con capas de café, mascarpone y cacao, perfecto para finalizar cualquier comida.", 5500, "/img/tiramisu.png", "/img/t_mini_1.jpg", "/img/t_mini_2.jpg", "/img/t_mini_3.jpg", 10),
            Producto("PSA001", "Productos Sin Azúcar", "Torta Sin Azúcar De Naranja", "Torta ligera y deliciosa, endulzada naturalmente, ideal para quienes buscan opciones más saludables.", 48000, "/img/torta_sin_azucar_naranja.webp", "/img/tn_mini_1.jpg", "/img/tn_mini_2.jpg", "/img/tn_mini_3.jpg", 10),
            Producto("PSA002", "Productos Sin Azúcar", "Cheesecake Sin Azúcar", "Suave y cremoso, este cheesecake es una opción perfecta para disfrutar sin culpa.", 47000, "/img/cheesecake_sin_azucar.jpg", "/img/c_mini_1.webp", "/img/c_mini_2.jfif", "/img/c_mini_3.jpg", 10),
            Producto("PT001", "Pastelería Tradicional ", "Empanada de Manzana", "Pastelería tradicional rellena de manzanas especiadas, perfecta para un dulce desayuno o merienda.", 3000, "/img/empanada.png", "/img/e_mini_1.jpg", "/img/e_mini_2.webp", "/img/e_mini_3.jpg", 10),
            Producto("PT002", "Pastelería Tradicional ", "Tarta de Santiago", "Tradicional tarta española hecha con almendras, azúcar, y huevos, una delicia para los amantes de los postres clásicos.", 6000, "/img/tarta_santiago.webp", "/img/ts_mini_1.jpg", "/img/ts_mini_2.jpg", "/img/ts_mini_3.jpg", 10),
            Producto("PG001", "Productos Sin Gluten", "Brownie Sin Gluten", "Rico y denso, este brownie es perfecto para quienes necesitan evitar el gluten sin sacrificar el sabor.", 4000, "/img/brownie.webp", "/img/b_mini_1.webp", "/img/b_mini_2.png", "/img/b_mini_3.avif", 10),
            Producto("PG002", "Productos Sin Gluten", "Pan Sin Gluten", "Suave y esponjoso, ideal para sándwiches o para acompañar cualquier comida.", 3500, "/img/pan.png", "/img/p_mini_1.jpg", "/img/p_mini_2.jpg", "/img/p_mini_3.jpg", 10),
            Producto("PV001", "Productos Veganos", "Torta Vegana de Chocolate", "Torta de chocolate húmeda y deliciosa, hecha sin productos de origen animal, perfecta para veganos.", 50000, "/img/torta_vegana_chocolate.png", "/img/tv_mini_1.jpg", "/img/tv_mini_2.webp", "/img/tv_mini_3.jpg", 10),
            Producto("PV002", "Productos Veganos", "Galletas Veganas de Avena", "Crujientes y sabrosas, estas galletas son una excelente opción para un snack saludable y vegano.", 4500, "/img/galleta_vegana_avena.jpg", "/img/gv_mini_1.jpg", "/img/gv_mini_2.jpg", "/img/gv_mini_3.jpg", 10),
            Producto("TE001", "Tortas Especiales", "Torta Especial de Cumpleaños", "Diseñada especialmente para celebraciones, personalizable con decoraciones y mensajes únicos.", 55000, "/img/torta_cumpleanos.png", "/img/tec_mini_1.webp", "/img/tec_mini_2.jfif", "/img/tec_mini_3.webp", 10),
            Producto("TE002", "Tortas Especiales", "Torta Especial de Bodas", "Elegante y deliciosa, esta torta está diseñada para ser el centro de atención en cualquier boda.", 60000, "/img/torta_boda.webp", "/img/teb_mini_1.webp", "/img/teb_mini_2.webp", "/img/teb_mini_3.jpg", 10)
        )
        repository.insertarProductos(productosIniciales)
    }

    suspend fun guardarProducto(producto: Producto) {
        repository.insertarProducto(producto)
    }
}
