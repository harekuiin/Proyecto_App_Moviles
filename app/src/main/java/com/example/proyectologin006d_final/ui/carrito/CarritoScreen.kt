package com.example.proyectologin006d_final.ui.carrito

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.example.proyectologin006d_final.data.model.Producto
import com.example.proyectologin006d_final.data.model.ProductoAgregado
import com.example.proyectologin006d_final.ui.theme.Chocolate
import com.example.proyectologin006d_final.ui.theme.CremaPastel
import com.example.proyectologin006d_final.ui.utils.rememberImageFromAssets
import com.example.proyectologin006d_final.viewmodel.ProductoViewModel
import com.example.proyectologin006d_final.viewmodel.ProductoViewModelFactory

// Data class para combinar información del carrito con el producto
data class ItemCarrito(
    val productoAgregado: ProductoAgregado,
    val producto: Producto?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    correoUsuario: String,
    navController: NavController,
    vm: ProductoViewModel = viewModel(
        factory = ProductoViewModelFactory(LocalContext.current.applicationContext as android.app.Application)
    )
) {
    val productosAgregados by vm.obtenerCarritoPorUsuario(correoUsuario).collectAsState(initial = emptyList())
    val productos by vm.productos.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var mostrarDialogoVaciar by remember { mutableStateOf(false) }

    // Combinar productos agregados con información del producto
    val itemsCarrito = remember(productosAgregados, productos) {
        productosAgregados.map { agregado ->
            ItemCarrito(
                productoAgregado = agregado,
                producto = productos.find { it.id == agregado.idProducto }
            )
        }
    }

    // Calcular total
    val total = remember(itemsCarrito) {
        itemsCarrito.sumOf { item ->
            (item.producto?.precio ?: 0) * item.productoAgregado.cantidad
        }
    }

    // Calcular cantidad total de items
    val cantidadTotal = remember(itemsCarrito) {
        itemsCarrito.sumOf { it.productoAgregado.cantidad }
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
                    title = { Text("Mi Carrito") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    },
                    actions = {
                        if (itemsCarrito.isNotEmpty()) {
                            IconButton(onClick = { mostrarDialogoVaciar = true }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Vaciar carrito"
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Chocolate,
                        titleContentColor = CremaPastel,
                        navigationIconContentColor = CremaPastel,
                        actionIconContentColor = CremaPastel
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(CremaPastel)
            ) {
                if (itemsCarrito.isEmpty()) {
                    // Carrito vacío
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(100.dp),
                                tint = Color(0xFFB0BEC5)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Tu carrito está vacío",
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color(0xFF5D4037),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Agrega productos desde el catálogo",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF9E9E9E),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = { navController.navigate("home/$correoUsuario") },
                                colors = ButtonDefaults.buttonColors(containerColor = Chocolate)
                            ) {
                                Text("Ver catálogo", color = CremaPastel)
                            }
                        }
                    }
                } else {
                    // Lista de productos en el carrito
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(itemsCarrito, key = { it.productoAgregado.id }) { item ->
                            ItemCarritoCard(
                                item = item,
                                onIncrementar = {
                                    coroutineScope.launch {
                                        vm.incrementarCantidad(item.productoAgregado.id)
                                    }
                                },
                                onDecrementar = {
                                    coroutineScope.launch {
                                        vm.decrementarCantidad(item.productoAgregado.id)
                                    }
                                },
                                onEliminar = {
                                    coroutineScope.launch {
                                        vm.eliminarDelCarrito(item.productoAgregado.id)
                                    }
                                }
                            )
                        }
                    }

                    // Resumen del carrito
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Productos:",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color(0xFF5D4037)
                                )
                                Text(
                                    text = "$cantidadTotal ${if (cantidadTotal == 1) "item" else "items"}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color(0xFF5D4037)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            HorizontalDivider(color = Color(0xFFE0E0E0))

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total:",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Chocolate
                                )
                                Text(
                                    text = "$${String.format("%,d", total)} CLP",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Chocolate
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { /* TODO: Implementar proceso de compra */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Chocolate),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Proceder al pago",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = CremaPastel
                                )
                            }
                        }
                    }
                }
            }
        }

        // Diálogo de confirmación para vaciar carrito
        if (mostrarDialogoVaciar) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoVaciar = false },
                title = { Text("Vaciar carrito", color = Chocolate) },
                text = { Text("¿Estás seguro de que deseas eliminar todos los productos del carrito?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                vm.vaciarCarrito(correoUsuario)
                            }
                            mostrarDialogoVaciar = false
                        }
                    ) {
                        Text("Vaciar", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoVaciar = false }) {
                        Text("Cancelar", color = Chocolate)
                    }
                }
            )
        }
    }
}

@Composable
fun ItemCarritoCard(
    item: ItemCarrito,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit,
    onEliminar: () -> Unit
) {
    val producto = item.producto
    val cantidad = item.productoAgregado.cantidad
    val subtotal = (producto?.precio ?: 0) * cantidad

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del producto
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                if (producto != null && producto.imagen_principal.isNotEmpty()) {
                    val context = LocalContext.current
                    val bitmap = rememberImageFromAssets(
                        context = context,
                        assetPath = producto.imagen_principal,
                        key = producto.id
                    )
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = producto.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                } else {
                    Text(
                        text = "Sin imagen",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9E9E9E)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Información del producto
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = producto?.nombre ?: "Producto no encontrado",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Chocolate,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$${String.format("%,d", producto?.precio ?: 0)} c/u",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9E9E9E)
                )

                // Mostrar comentario si existe
                if (item.productoAgregado.comentario.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "📝 ${item.productoAgregado.comentario}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF5D4037),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Controles de cantidad
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón decrementar
                    FilledIconButton(
                        onClick = onDecrementar,
                        modifier = Modifier.size(32.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color(0xFFE0E0E0)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Reducir cantidad",
                            modifier = Modifier.size(18.dp),
                            tint = Color(0xFF5D4037)
                        )
                    }

                    // Cantidad
                    Text(
                        text = "$cantidad",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Chocolate,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    // Botón incrementar
                    FilledIconButton(
                        onClick = onIncrementar,
                        modifier = Modifier.size(32.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Chocolate
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Aumentar cantidad",
                            modifier = Modifier.size(18.dp),
                            tint = CremaPastel
                        )
                    }
                }
            }

            // Subtotal y botón eliminar
            Column(
                horizontalAlignment = Alignment.End
            ) {
                IconButton(
                    onClick = onEliminar,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color(0xFFE57373)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$${String.format("%,d", subtotal)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Chocolate
                )
            }
        }
    }
}

