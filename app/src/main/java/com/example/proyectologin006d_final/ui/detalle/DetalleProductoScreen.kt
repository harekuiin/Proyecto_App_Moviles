package com.example.proyectologin006d_final.ui.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import com.example.proyectologin006d_final.ui.utils.rememberImageFromAssets
import com.example.proyectologin006d_final.data.model.Producto
import com.example.proyectologin006d_final.ui.theme.Chocolate
import com.example.proyectologin006d_final.ui.theme.CremaPastel
import com.example.proyectologin006d_final.ui.theme.RosaSuave
import com.example.proyectologin006d_final.viewmodel.ProductoViewModel
import com.example.proyectologin006d_final.viewmodel.ProductoViewModelFactory

// Descripciones breves inventadas para cada producto
fun obtenerDescripcionBreve(producto: Producto): String {
    return when (producto.id) {
        "TC001" -> "Sumérgete en el sabor intenso del chocolate belga mezclado con crema chantilly. Cada bocado es una experiencia de texturas suaves y sabores equilibrados que deleitarán tu paladar. Perfecta para compartir en momentos especiales."
        "TC002" -> "Disfruta de una combinación única de frutas frescas de temporada sobre un bizcocho esponjoso. La frescura de las frutas complementa perfectamente la dulzura natural, creando un postre equilibrado y refrescante."
        "TT001" -> "La clásica torta de vainilla que nunca pasa de moda. Con un sabor suave y delicado, este postre tradicional es ideal para quienes buscan algo dulce sin ser abrumador. Perfecta para el té de la tarde."
        "TT002" -> "El sabor tradicional chileno se encuentra en cada capa de esta torta. El manjar casero junto con nueces tostadas crean una combinación irresistible que te transportará a los sabores de la infancia."
        "PI001" -> "Una experiencia cremosa y ligera que se derrite en tu boca. El mousse de chocolate está elaborado con cacao de alta calidad, creando una textura aérea y un sabor profundo que satisface cualquier antojo dulce."
        "PI002" -> "El postre italiano por excelencia. Capas de café expreso, mascarpone cremoso y cacao en polvo se combinan para crear una experiencia gastronómica única. Cada cucharada es un viaje a Italia."
        "PSA001" -> "Endulzada naturalmente con frutas, esta torta es perfecta para quienes buscan opciones más saludables sin sacrificar el sabor. La naranja aporta un toque cítrico refrescante que complementa la dulzura natural."
        "PSA002" -> "Un cheesecake cremoso y suave que no necesita azúcar para ser delicioso. La textura es perfecta y el sabor es naturalmente dulce, ideal para disfrutar sin preocupaciones."
        "PT001" -> "La receta tradicional de nuestras abuelas, con relleno de manzanas canela y azúcar. Cada bocado es un regreso a los sabores caseros y auténticos de la pastelería chilena."
        "PT002" -> "La tarta española más famosa, hecha con almendras molidas y un toque de limón. Su textura densa y su sabor único la convierten en el postre perfecto para acompañar un café."
        "PG001" -> "Un brownie denso y rico que no contiene gluten pero mantiene todo el sabor. Perfecto para celíacos y amantes del chocolate. Cada bocado es una explosión de sabor."
        "PG002" -> "Pan fresco y esponjoso elaborado con harinas especiales sin gluten. Ideal para desayunos y meriendas, mantiene la textura y sabor del pan tradicional."
        "PV001" -> "Una torta de chocolate completamente vegana que no deja nada que desear. Elaborada con ingredientes naturales y sin productos de origen animal, mantiene toda la riqueza del chocolate."
        "PV002" -> "Galletas crujientes y sabrosas hechas con avena y endulzantes naturales. Perfectas para un snack saludable en cualquier momento del día, ideales para veganos y amantes de lo natural."
        "TE001" -> "Diseñada especialmente para hacer tu cumpleaños inolvidable. Cada detalle está pensado para celebrar, desde la decoración hasta el sabor. Un postre que convertirá cualquier celebración en un momento especial."
        "TE002" -> "La torta perfecta para el día más importante de tu vida. Elegante, sofisticada y deliciosa, está diseñada para ser el centro de atención en tu boda. Cada capa es una obra de arte culinaria."
        else -> "Un postre elaborado con ingredientes de la más alta calidad, preparado con amor y dedicación. Cada bocado es una experiencia única que deleitará todos tus sentidos."
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    codigo: String,
    correoUsuario: String,
    navController: NavController,
    vm: ProductoViewModel = viewModel(
        factory = ProductoViewModelFactory(LocalContext.current.applicationContext as android.app.Application)
    )
) {
    var producto by remember { mutableStateOf<Producto?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var mensajeConfirmacion by remember { mutableStateOf<String?>(null) }
    var isAgregando by remember { mutableStateOf(false) }
    var comentario by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(codigo) {
        isLoading = true
        
        // Intentar múltiples estrategias para obtener el producto
        var productoEncontrado: Producto? = null
        
        // Estrategia 1: Buscar en el StateFlow
        productoEncontrado = vm.productos.value.find { it.id == codigo }
        
        // Estrategia 2: Si no está en el StateFlow, obtener directamente del ViewModel
        if (productoEncontrado == null) {
            productoEncontrado = vm.obtenerProductoPorId(codigo)
        }
        
        // Estrategia 3: Si aún no está, esperar un poco y buscar en el StateFlow nuevamente
        if (productoEncontrado == null) {
            kotlinx.coroutines.delay(500)
            productoEncontrado = vm.productos.value.find { it.id == codigo }
        }
        
        // Estrategia 4: Último intento con más tiempo de espera
        if (productoEncontrado == null) {
            kotlinx.coroutines.delay(1000)
            productoEncontrado = vm.obtenerProductoPorId(codigo)
        }
        
        producto = productoEncontrado
        isLoading = false
    }

    fun agregarAlCarrito() {
        if (producto == null) return
        
        isAgregando = true
        mensajeConfirmacion = null
        
        coroutineScope.launch {
            val exito = vm.agregarProductoAlCarrito(producto!!.id, correoUsuario, comentario)
            isAgregando = false
            
            if (exito) {
                mensajeConfirmacion = "¡Producto agregado exitosamente al carrito!"
                comentario = "" // Limpiar el comentario después de agregar
                // Ocultar el mensaje después de 3 segundos
                kotlinx.coroutines.delay(3000)
                mensajeConfirmacion = null
            } else {
                mensajeConfirmacion = "Error al agregar el producto. Intenta nuevamente."
                kotlinx.coroutines.delay(3000)
                mensajeConfirmacion = null
            }
        }
    }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Chocolate,
            background = CremaPastel,
            surface = CremaPastel,
            onPrimary = CremaPastel,
            onBackground = Color(0xFF5D4037),
            onSurface = Color(0xFF5D4037)
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalle del Producto") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Chocolate,
                        titleContentColor = CremaPastel,
                        navigationIconContentColor = CremaPastel
                    )
                )
            }
        ) { innerPadding ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Chocolate)
                }
            } else if (producto == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Producto no encontrado",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF5D4037)
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(CremaPastel)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Imagen del producto
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (producto!!.imagen_principal.isNotEmpty()) {
                            val context = LocalContext.current
                            val bitmap = rememberImageFromAssets(
                                context = context,
                                assetPath = producto!!.imagen_principal,
                                key = producto!!.id
                            )
                            
                            if (bitmap != null) {
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = producto!!.nombre,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Text(
                                    text = "Cargando...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF9E9E9E)
                                )
                            }
                        } else {
                            Text(
                                text = "Sin imagen",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF9E9E9E)
                            )
                        }
                    }

                    // Categoría
                    Text(
                        text = producto!!.categoria,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF9E9E9E),
                        fontWeight = FontWeight.Medium
                    )

                    // Nombre
                    Text(
                        text = producto!!.nombre,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Chocolate,
                        fontWeight = FontWeight.Bold
                    )

                    Divider(color = Color(0xFFE0E0E0))

                    // Precio
                    Text(
                        text = "$${producto!!.precio} CLP",
                        style = MaterialTheme.typography.displayMedium,
                        color = Chocolate,
                        fontWeight = FontWeight.Bold
                    )

                    Divider(color = Color(0xFFE0E0E0))

                    // Descripción breve inventada
                    Text(
                        text = "Sobre este producto",
                        style = MaterialTheme.typography.titleLarge,
                        color = Chocolate,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = obtenerDescripcionBreve(producto!!),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF5D4037),
                        lineHeight = androidx.compose.ui.unit.TextUnit(24f, androidx.compose.ui.unit.TextUnitType.Sp)
                    )

                    Divider(color = Color(0xFFE0E0E0))

                    // Descripción original
                    Text(
                        text = "Descripción",
                        style = MaterialTheme.typography.titleLarge,
                        color = Chocolate,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = producto!!.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF5D4037),
                        lineHeight = androidx.compose.ui.unit.TextUnit(20f, androidx.compose.ui.unit.TextUnitType.Sp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mensaje de confirmación
                    if (mensajeConfirmacion != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (mensajeConfirmacion!!.contains("exitosamente")) 
                                    Color(0xFF4CAF50) 
                                else 
                                    Color(0xFFF44336)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = mensajeConfirmacion ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    // Botón Agregar al Carrito
                    Button(
                        onClick = { agregarAlCarrito() },
                        enabled = !isAgregando && mensajeConfirmacion == null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Chocolate
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isAgregando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = CremaPastel
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Agregando...", color = CremaPastel)
                        } else {
                            Text(
                                "Agregar al carrito",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = CremaPastel
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Campo de comentario opcional
                    OutlinedTextField(
                        value = comentario,
                        onValueChange = { comentario = it },
                        label = { Text("Comentario (opcional)") },
                        placeholder = { Text("Ej: Sin nueces, mensaje especial, etc.") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Chocolate,
                            unfocusedBorderColor = Color(0xFFB0BEC5),
                            focusedLabelColor = Chocolate,
                            cursorColor = Chocolate
                        ),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 3,
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Información adicional
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "ID: ${producto!!.id}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF9E9E9E)
                        )
                        Text(
                            text = "Stock: ${producto!!.stock}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF9E9E9E)
                        )
                    }
                }
            }
        }
    }
}
