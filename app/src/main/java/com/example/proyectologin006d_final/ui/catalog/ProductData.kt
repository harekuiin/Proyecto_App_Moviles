package com.example.proyectologin006d_final.ui.catalog

data class CatalogProduct(
    val code: String,
    val category: String,
    val name: String,
    val priceClp: String,
    val description: String,
    val imageUrl: String? = null // estructura para foto por producto
)

object ProductData {
    val products: List<CatalogProduct> = listOf(
        CatalogProduct(
            code = "TC001",
            category = "Tortas Cuadradas",
            name = "Torta Cuadrada de Chocolate",
            priceClp = "$45.000 CLP",
            description = "Deliciosa torta de chocolate con capas de ganache y un toque de avellanas. Personalizable con mensajes especiales."
        ),
        CatalogProduct(
            code = "TC002",
            category = "Tortas Cuadradas",
            name = "Torta Cuadrada de Frutas",
            priceClp = "$50.000 CLP",
            description = "Mezcla de frutas frescas y crema chantilly sobre bizcocho de vainilla, ideal para celebraciones."
        ),
        CatalogProduct(
            code = "TT001",
            category = "Tortas Circulares",
            name = "Torta Circular de Vainilla",
            priceClp = "$40.000 CLP",
            description = "Bizcocho de vainilla clásico relleno con crema pastelera y glaseado dulce."
        ),
        CatalogProduct(
            code = "TT002",
            category = "Tortas Circulares",
            name = "Torta Circular de Manjar",
            priceClp = "$42.000 CLP",
            description = "Torta tradicional chilena con manjar y nueces."
        ),
        CatalogProduct(
            code = "PI001",
            category = "Postres Individuales",
            name = "Mousse de Chocolate",
            priceClp = "$5.000 CLP",
            description = "Postre individual cremoso y suave hecho con chocolate de alta calidad."
        ),
        CatalogProduct(
            code = "PI002",
            category = "Postres Individuales",
            name = "Tiramisú Clásico",
            priceClp = "$5.500 CLP",
            description = "Postre italiano con capas de café, mascarpone y cacao."
        ),
        CatalogProduct(
            code = "PSA001",
            category = "Productos Sin Azúcar",
            name = "Torta Sin Azúcar de Naranja",
            priceClp = "$48.000 CLP",
            description = "Torta ligera y deliciosa, endulzada naturalmente."
        ),
        CatalogProduct(
            code = "PSA002",
            category = "Productos Sin Azúcar",
            name = "Cheesecake Sin Azúcar",
            priceClp = "$47.000 CLP",
            description = "Suave y cremoso, perfecto para disfrutar sin culpa."
        ),
        CatalogProduct(
            code = "PT001",
            category = "Pastelería Tradicional",
            name = "Empanada de Manzana",
            priceClp = "$3.000 CLP",
            description = "Rellena de manzanas especiadas, perfecta para desayuno o merienda."
        ),
        CatalogProduct(
            code = "PT002",
            category = "Pastelería Tradicional",
            name = "Tarta de Santiago",
            priceClp = "$6.000 CLP",
            description = "Tradicional tarta de almendras, azúcar y huevos."
        ),
        CatalogProduct(
            code = "PG001",
            category = "Productos Sin Gluten",
            name = "Brownie Sin Gluten",
            priceClp = "$4.000 CLP",
            description = "Rico y denso, sin sacrificar el sabor."
        ),
        CatalogProduct(
            code = "PG002",
            category = "Productos Sin Gluten",
            name = "Pan Sin Gluten",
            priceClp = "$3.500 CLP",
            description = "Suave y esponjoso, ideal para sándwiches."
        ),
        CatalogProduct(
            code = "PV001",
            category = "Productos Vegana",
            name = "Torta Vegana de Chocolate",
            priceClp = "$50.000 CLP",
            description = "Húmeda y deliciosa, sin productos de origen animal."
        ),
        CatalogProduct(
            code = "PV002",
            category = "Productos Vegana",
            name = "Galletas Veganas de Avena",
            priceClp = "$4.500 CLP",
            description = "Crujientes y sabrosas, excelente snack saludable."
        ),
        CatalogProduct(
            code = "TE001",
            category = "Tortas Especiales",
            name = "Torta Especial de Cumpleaños",
            priceClp = "$55.000 CLP",
            description = "Personalizable con decoraciones y mensajes únicos."
        ),
        CatalogProduct(
            code = "TE002",
            category = "Tortas Especiales",
            name = "Torta Especial de Boda",
            priceClp = "$60.000 CLP",
            description = "Elegante y deliciosa, ideal para grandes celebraciones."
        ),
    )
}


