package com.example.proyectologin006d_final.ui.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectologin006d_final.data.model.Producto
import com.example.proyectologin006d_final.ui.theme.Chocolate
import com.example.proyectologin006d_final.ui.theme.CremaPastel
import com.example.proyectologin006d_final.viewmodel.ProductoViewModel
import com.example.proyectologin006d_final.viewmodel.ProductoViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    codigo: String,
    navController: NavController,
    vm: ProductoViewModel = viewModel(
        factory = ProductoViewModelFactory(LocalContext.current.applicationContext as android.app.Application)
    )
) {
    var producto by remember { mutableStateOf<Producto?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(codigo) {
        // Obtener producto desde la lista actual
        val productos = vm.productos.value
        if (productos.isEmpty()) {
            // Si no hay productos, esperar un poco y volver a intentar
            kotlinx.coroutines.delay(500)
            val productosActualizados = vm.productos.value
            producto = productosActualizados.find { it.id == codigo }
        } else {
            producto = productos.find { it.id == codigo }
        }
        isLoading = false
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
                    CircularProgressIndicator()
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
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Espacio para la foto (estructura preparada)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Foto del producto",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF9E9E9E)
                        )
                        // Aquí se puede agregar la imagen cuando esté disponible
                        // Image(
                        //     painter = painterResource(id = obtenerRecursoFoto(producto.foto)),
                        //     contentDescription = producto.nombre,
                        //     contentScale = ContentScale.Crop,
                        //     modifier = Modifier.fillMaxSize()
                        // )
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

                    // Descripción
                    Text(
                        text = "Descripción",
                        style = MaterialTheme.typography.titleLarge,
                        color = Chocolate,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = producto!!.descripcion,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF5D4037),
                        lineHeight = androidx.compose.ui.unit.TextUnit(24f, androidx.compose.ui.unit.TextUnitType.Sp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // ID del producto
                    Text(
                        text = "ID: ${producto!!.id}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9E9E9E)
                    )
                    
                    // Categoría
                    Text(
                        text = "Categoría: ${producto!!.categoria}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9E9E9E)
                    )
                }
            }
        }
    }
}

