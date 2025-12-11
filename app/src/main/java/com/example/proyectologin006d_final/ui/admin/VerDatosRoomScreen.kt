package com.example.proyectologin006d_final.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import com.example.proyectologin006d_final.data.database.ProductoDatabase
import com.example.proyectologin006d_final.data.model.Producto
import com.example.proyectologin006d_final.data.model.ProductoAgregado
import com.example.proyectologin006d_final.data.model.Usuario
import com.example.proyectologin006d_final.data.repository.ProductoAgregadoRepository
import com.example.proyectologin006d_final.data.repository.ProductoRepository
import com.example.proyectologin006d_final.data.repository.UsuarioRepository
import com.example.proyectologin006d_final.ui.theme.Chocolate
import com.example.proyectologin006d_final.ui.theme.CremaPastel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerDatosRoomScreen(
    navController: NavController,
    username: String
) {
    var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }
    var usuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    var productosAgregados by remember { mutableStateOf<List<ProductoAgregado>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableStateOf(0) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val database = ProductoDatabase.getDatabase(context)
            val productoRepository = ProductoRepository(database.productoDao())
            val usuarioRepository = UsuarioRepository(database.usuarioDao())
            val productoAgregadoRepository = ProductoAgregadoRepository(database.productoAgregadoDao())

            productos = productoRepository.obtenerProductos().first()
            usuarios = usuarioRepository.obtenerTodosLosUsuarios().first()
            productosAgregados = productoAgregadoRepository.obtenerTodosLosProductosAgregados().first()
            
            isLoading = false
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
                    title = { Text("Datos en Room") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(CremaPastel)
            ) {
                // Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Chocolate,
                    contentColor = CremaPastel
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Productos (${productos.size})", color = if (selectedTab == 0) CremaPastel else Color(0xFFB0BEC5)) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Usuarios (${usuarios.size})", color = if (selectedTab == 1) CremaPastel else Color(0xFFB0BEC5)) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("Agregados (${productosAgregados.size})", color = if (selectedTab == 2) CremaPastel else Color(0xFFB0BEC5)) }
                    )
                }

                // Contenido
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Chocolate)
                    }
                } else {
                    when (selectedTab) {
                        0 -> ProductosTab(productos)
                        1 -> UsuariosTab(usuarios)
                        2 -> ProductosAgregadosTab(productosAgregados)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductosTab(productos: List<Producto>) {
    if (productos.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No hay productos en la base de datos",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF5D4037)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(productos) { producto ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "ID: ${producto.id}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Chocolate
                        )
                        Text(
                            text = "Nombre: ${producto.nombre}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5D4037)
                        )
                        Text(
                            text = "Categoría: ${producto.categoria}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF9E9E9E)
                        )
                        Text(
                            text = "Precio: $${producto.precio} CLP",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5D4037)
                        )
                        Text(
                            text = "Stock: ${producto.stock}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF9E9E9E)
                        )
                        Text(
                            text = "Imagen: ${producto.imagen_principal}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF9E9E9E),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UsuariosTab(usuarios: List<Usuario>) {
    if (usuarios.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No hay usuarios en la base de datos",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF5D4037)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(usuarios) { usuario ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Correo: ${usuario.correo}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Chocolate
                        )
                        Text(
                            text = "Nombre: ${usuario.nombre}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5D4037)
                        )
                        Text(
                            text = "RUN: ${usuario.run.ifEmpty { "No registrado" }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF9E9E9E)
                        )
                        Text(
                            text = "Descuento: ${usuario.descuento}%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5D4037)
                        )
                        Text(
                            text = "Torta Gratis: ${if (usuario.tortaGratis) "Sí" else "No"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF9E9E9E)
                        )
                        Text(
                            text = "Es Admin: ${if (usuario.isAdmin) "Sí" else "No"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (usuario.isAdmin) Color(0xFF4CAF50) else Color(0xFF9E9E9E)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductosAgregadosTab(productosAgregados: List<ProductoAgregado>) {
    if (productosAgregados.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No hay productos agregados en la base de datos",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF5D4037)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(productosAgregados) { productoAgregado ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "ID: ${productoAgregado.id}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Chocolate
                        )
                        Text(
                            text = "ID Producto: ${productoAgregado.idProducto}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5D4037)
                        )
                        Text(
                            text = "Usuario: ${productoAgregado.correoUsuario}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5D4037)
                        )
                        Text(
                            text = "Fecha: ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date(productoAgregado.fechaAgregado))}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF9E9E9E)
                        )
                    }
                }
            }
        }
    }
}

