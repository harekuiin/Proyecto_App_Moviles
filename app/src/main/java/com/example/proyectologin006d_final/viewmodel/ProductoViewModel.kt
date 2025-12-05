package com.example.proyectologin006d_final.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectologin006d_final.data.database.ProductoDatabase
import com.example.proyectologin006d_final.data.model.Producto
import com.example.proyectologin006d_final.data.model.ProductoAgregado
import com.example.proyectologin006d_final.data.repository.ProductoAgregadoRepository
import com.example.proyectologin006d_final.data.repository.ProductoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductoViewModel(application: Application) : AndroidViewModel(application) {
    private val database = ProductoDatabase.getDatabase(application)
    private val repository = ProductoRepository(database.productoDao())
    private val productoAgregadoRepository = ProductoAgregadoRepository(database.productoAgregadoDao())

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
        // Productos con rutas de imágenes extraídas del proyecto React
        val productosIniciales = listOf(
            // TC001 - Torta Cuadrada de Chocolate
            Producto(
                id = "TC001",
                categoria = "Tortas Cuadradas",
                nombre = "Torta Cuadrada de Chocolate",
                descripcion = "Deliciosa torta de chocolate con capas de ganache y un toque de avellanas. Personalizable con mensajes especiales.",
                precio = 45000,
                imagen_principal = "/img/torta_cuadrada_chocolate.jpg",
                imagen1 = "/img/tcc_mini_1.webp",
                imagen2 = "/img/tcc_mini_2.webp",
                imagen3 = "/img/tcc_mini_3.jpg",
                stock = 10
            ),
            // TC002 - Torta Cuadrada de Frutas
            Producto(
                id = "TC002",
                categoria = "Tortas Cuadradas",
                nombre = "Torta Cuadrada de Frutas ",
                descripcion = "Una mezcla de frutas frescas y crema chantilly sobre un suave bizcocho de vainilla, ideal para celebraciones.",
                precio = 50000,
                imagen_principal = "/img/torta_cuadrada_frutas.png",
                imagen1 = "/img/tcf_mini_1.png",
                imagen2 = "/img/tcf_mini_2.jpg",
                imagen3 = "/img/tcf_mini_3.jpg",
                stock = 10
            ),
            // TT001 - Torta Circular de Vainilla
            Producto(
                id = "TT001",
                categoria = "Tortas Circulares",
                nombre = "Torta Circular de Vainilla",
                descripcion = "Bizcocho de vainilla clásico relleno con crema pastelera y cubierto con un glaseado dulce, perfecto para cualquier ocasión.",
                precio = 40000,
                imagen_principal = "/img/torta_circular_vainilla.jpg",
                imagen1 = "/img/tcv_mini_1.jpg",
                imagen2 = "/img/tcv_mini_2.webp",
                imagen3 = "/img/tcv_mini_3.avif",
                stock = 10
            ),
            // TT002 - Torta Circular de Manjar
            Producto(
                id = "TT002",
                categoria = "Tortas Circulares",
                nombre = "Torta Circular de Manjar",
                descripcion = " Torta tradicional chilena con manjar y nueces, un deleite para los amantes de los sabores dulces y clásicos.",
                precio = 42000,
                imagen_principal = "/img/torta_circular_manjar.png",
                imagen1 = "/img/tcm_mini_1.jfif",
                imagen2 = "/img/tcm_mini_2.jpg",
                imagen3 = "/img/tcm_mini_3.jfif",
                stock = 10
            ),
            // PI001 - Mousse de Chocolate
            Producto(
                id = "PI001",
                categoria = "Postres Individuales",
                nombre = "Mousse de Chocolate",
                descripcion = "Postre individual cremoso y suave, hecho con chocolate de alta calidad, ideal para los amantes del chocolate.",
                precio = 5000,
                imagen_principal = "/img/mousse_chocolate.png",
                imagen1 = "/img/m_mini_1.jpg",
                imagen2 = "/img/m_mini_2.jpg",
                imagen3 = "/img/m_mini_3.avif",
                stock = 10
            ),
            // PI002 - Tiramisú Clásico
            Producto(
                id = "PI002",
                categoria = "Postres Individuales",
                nombre = "Tiramisú Clásico",
                descripcion = "Un postre italiano individual con capas de café, mascarpone y cacao, perfecto para finalizar cualquier comida.",
                precio = 5500,
                imagen_principal = "/img/tiramisu.png",
                imagen1 = "/img/t_mini_1.jpg",
                imagen2 = "/img/t_mini_2.jpg",
                imagen3 = "/img/t_mini_3.jpg",
                stock = 10
            ),
            // PSA001 - Torta Sin Azúcar De Naranja
            Producto(
                id = "PSA001",
                categoria = "Productos Sin Azúcar",
                nombre = "Torta Sin Azúcar De Naranja",
                descripcion = "Torta ligera y deliciosa, endulzada naturalmente, ideal para quienes buscan opciones más saludables.",
                precio = 48000,
                imagen_principal = "/img/torta_sin_azucar_naranja.webp",
                imagen1 = "/img/tn_mini_1.jpg",
                imagen2 = "/img/tn_mini_2.jpg",
                imagen3 = "/img/tn_mini_3.jpg",
                stock = 10
            ),
            // PSA002 - Cheesecake Sin Azúcar
            Producto(
                id = "PSA002",
                categoria = "Productos Sin Azúcar",
                nombre = "Cheesecake Sin Azúcar",
                descripcion = "Suave y cremoso, este cheesecake es una opción perfecta para disfrutar sin culpa.",
                precio = 47000,
                imagen_principal = "/img/cheesecake_sin_azucar.jpg",
                imagen1 = "/img/c_mini_1.webp",
                imagen2 = "/img/c_mini_2.jfif",
                imagen3 = "/img/c_mini_3.jpg",
                stock = 10
            ),
            // PT001 - Empanada de Manzana
            Producto(
                id = "PT001",
                categoria = "Pastelería Tradicional ",
                nombre = "Empanada de Manzana",
                descripcion = "Pastelería tradicional rellena de manzanas especiadas, perfecta para un dulce desayuno o merienda.",
                precio = 3000,
                imagen_principal = "/img/empanada.png",
                imagen1 = "/img/e_mini_1.jpg",
                imagen2 = "/img/e_mini_2.webp",
                imagen3 = "/img/e_mini_3.jpg",
                stock = 10
            ),
            // PT002 - Tarta de Santiago
            Producto(
                id = "PT002",
                categoria = "Pastelería Tradicional ",
                nombre = "Tarta de Santiago",
                descripcion = "Tradicional tarta española hecha con almendras, azúcar, y huevos, una delicia para los amantes de los postres clásicos.",
                precio = 6000,
                imagen_principal = "/img/tarta_santiago.webp",
                imagen1 = "/img/ts_mini_1.jpg",
                imagen2 = "/img/ts_mini_2.jpg",
                imagen3 = "/img/ts_mini_3.jpg",
                stock = 10
            ),
            // PG001 - Brownie Sin Gluten
            Producto(
                id = "PG001",
                categoria = "Productos Sin Gluten",
                nombre = "Brownie Sin Gluten",
                descripcion = "Rico y denso, este brownie es perfecto para quienes necesitan evitar el gluten sin sacrificar el sabor.",
                precio = 4000,
                imagen_principal = "/img/brownie.webp",
                imagen1 = "/img/b_mini_1.webp",
                imagen2 = "/img/b_mini_2.png",
                imagen3 = "/img/b_mini_3.avif",
                stock = 10
            ),
            // PG002 - Pan Sin Gluten
            Producto(
                id = "PG002",
                categoria = "Productos Sin Gluten",
                nombre = "Pan Sin Gluten",
                descripcion = "Suave y esponjoso, ideal para sándwiches o para acompañar cualquier comida.",
                precio = 3500,
                imagen_principal = "/img/pan.png",
                imagen1 = "/img/p_mini_1.jpg",
                imagen2 = "/img/p_mini_2.jpg",
                imagen3 = "/img/p_mini_3.jpg",
                stock = 10
            ),
            // PV001 - Torta Vegana de Chocolate
            Producto(
                id = "PV001",
                categoria = "Productos Veganos",
                nombre = "Torta Vegana de Chocolate",
                descripcion = "Torta de chocolate húmeda y deliciosa, hecha sin productos de origen animal, perfecta para veganos.",
                precio = 50000,
                imagen_principal = "/img/torta_vegana_chocolate.png",
                imagen1 = "/img/tv_mini_1.jpg",
                imagen2 = "/img/tv_mini_2.webp",
                imagen3 = "/img/tv_mini_3.jpg",
                stock = 10
            ),
            // PV002 - Galletas Veganas de Avena
            Producto(
                id = "PV002",
                categoria = "Productos Veganos",
                nombre = "Galletas Veganas de Avena",
                descripcion = "Crujientes y sabrosas, estas galletas son una excelente opción para un snack saludable y vegano.",
                precio = 4500,
                imagen_principal = "/img/galleta_vegana_avena.jpg",
                imagen1 = "/img/gv_mini_1.jpg",
                imagen2 = "/img/gv_mini_2.jpg",
                imagen3 = "/img/gv_mini_3.jpg",
                stock = 10
            ),
            // TE001 - Torta Especial de Cumpleaños
            Producto(
                id = "TE001",
                categoria = "Tortas Especiales",
                nombre = "Torta Especial de Cumpleaños",
                descripcion = "Diseñada especialmente para celebraciones, personalizable con decoraciones y mensajes únicos.",
                precio = 55000,
                imagen_principal = "/img/torta_cumpleanos.png",
                imagen1 = "/img/tec_mini_1.webp",
                imagen2 = "/img/tec_mini_2.jfif",
                imagen3 = "/img/tec_mini_3.webp",
                stock = 10
            ),
            // TE002 - Torta Especial de Bodas
            Producto(
                id = "TE002",
                categoria = "Tortas Especiales",
                nombre = "Torta Especial de Bodas",
                descripcion = "Elegante y deliciosa, esta torta está diseñada para ser el centro de atención en cualquier boda.",
                precio = 60000,
                imagen_principal = "/img/torta_boda.webp",
                imagen1 = "/img/teb_mini_1.webp",
                imagen2 = "/img/teb_mini_2.webp",
                imagen3 = "/img/teb_mini_3.jpg",
                stock = 10
            )
        )
        repository.insertarProductos(productosIniciales)
    }

    suspend fun guardarProducto(producto: Producto) {
        repository.insertarProducto(producto)
    }

    suspend fun agregarProductoAlCarrito(idProducto: String, correoUsuario: String, comentario: String = ""): Boolean {
        return try {
            val productoAgregado = ProductoAgregado(
                idProducto = idProducto,
                correoUsuario = correoUsuario,
                comentario = comentario
            )
            productoAgregadoRepository.insertarProductoAgregado(productoAgregado)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun obtenerProductoPorId(id: String): Producto? {
        return repository.obtenerProductoPorId(id)
    }
}
