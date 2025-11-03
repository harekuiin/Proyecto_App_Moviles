package com.example.proyectologin006d_final.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.proyectologin006d_final.data.database.ProductoDatabase
import com.example.proyectologin006d_final.data.repository.UsuarioRepository
import com.example.proyectologin006d_final.ui.theme.Chocolate
import com.example.proyectologin006d_final.ui.theme.CremaPastel
import com.example.proyectologin006d_final.viewmodel.ProductoViewModel
import com.example.proyectologin006d_final.viewmodel.ProductoViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    username: String,
    navController: NavController,
    vm: ProductoViewModel = viewModel(
        factory = ProductoViewModelFactory(LocalContext.current.applicationContext as android.app.Application)
    )
) {
    val productos by vm.productos.collectAsState()
    val context = LocalContext.current
    var nombreUsuario by remember { mutableStateOf(username) } // Por defecto muestra el correo
    
    // Obtener el nombre del usuario desde la base de datos
    LaunchedEffect(username) {
        withContext(Dispatchers.IO) {
            val database = ProductoDatabase.getDatabase(context)
            val usuarioRepository = UsuarioRepository(database.usuarioDao())
            val usuario = usuarioRepository.obtenerUsuarioPorCorreo(username)
            if (usuario != null && usuario.nombre.isNotEmpty()) {
                nombreUsuario = usuario.nombre
            }
        }
    }

    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

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
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                NavigationDrawerContent(
                    username = username,
                    nombreUsuario = nombreUsuario,
                    navController = navController,
                    onCloseDrawer = { drawerState.close() }
                )
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Pastelería Mil Sabores") },
                        navigationIcon = {
                            IconButton(onClick = { drawerState.open() }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menú"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ExitToApp,
                                    contentDescription = "Cerrar sesión"
                                )
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
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(CremaPastel)
            ) {
                Text(
                    text = "Bienvenid@, $nombreUsuario",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Chocolate,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                if (productos.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay productos disponibles",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF5D4037)
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(productos) { producto ->
                            ProductoCard(
                                producto = producto,
                                onClick = {
                                    navController.navigate("detalle/${producto.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerContent(
    username: String,
    nombreUsuario: String,
    navController: NavController,
    onCloseDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(CremaPastel)
            .padding(16.dp)
    ) {
        Text(
            text = "Pastelería Mil Sabores",
            style = MaterialTheme.typography.headlineMedium,
            color = Chocolate,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "Bienvenid@, $nombreUsuario",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF5D4037),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        NavigationDrawerItem(
            label = { Text("Inicio") },
            selected = false,
            onClick = {
                onCloseDrawer()
                navController.navigate("home/$username") {
                    popUpTo("home/$username") { inclusive = true }
                    launchSingleTop = true
                }
            },
            modifier = Modifier.padding(vertical = 4.dp)
        )

        NavigationDrawerItem(
            label = { Text("Nosotros") },
            selected = false,
            onClick = {
                onCloseDrawer()
                navController.navigate("nosotros/$username") {
                    launchSingleTop = true
                }
            },
            modifier = Modifier.padding(vertical = 4.dp)
        )

        NavigationDrawerItem(
            label = { Text("Contacto") },
            selected = false,
            onClick = {
                onCloseDrawer()
                navController.navigate("contacto/$username") {
                    launchSingleTop = true
                }
            },
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Text(
            text = "© 2025 Pastelería Mil Sabores",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF9E9E9E),
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun ProductoCard(
    producto: com.example.proyectologin006d_final.data.model.Producto,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Imagen principal del producto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                if (producto.imagen_principal.isNotEmpty()) {
                    // Convertir ruta "/img/archivo.jpg" a "img/archivo.jpg" para assets
                    val rutaImagen = producto.imagen_principal.removePrefix("/")
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("file:///android_asset/$rutaImagen")
                            .crossfade(true)
                            .build(),
                        contentDescription = producto.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        error = androidx.compose.ui.res.painterResource(android.R.drawable.ic_menu_gallery),
                        placeholder = androidx.compose.ui.res.painterResource(android.R.drawable.ic_menu_gallery)
                    )
                } else {
                    Text(
                        text = "Sin imagen",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9E9E9E)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Chocolate,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = producto.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF5D4037),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "$${producto.precio} CLP",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Chocolate,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

