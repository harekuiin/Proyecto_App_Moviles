package com.example.proyectologin006d_final.ui.nosotros

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.navigation.NavController
import com.example.proyectologin006d_final.data.database.ProductoDatabase
import com.example.proyectologin006d_final.data.repository.UsuarioRepository
import com.example.proyectologin006d_final.ui.home.NavigationDrawerContent
import com.example.proyectologin006d_final.ui.theme.Chocolate
import com.example.proyectologin006d_final.ui.theme.CremaPastel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NosotrosScreen(
    navController: NavController,
    username: String
) {
    val context = LocalContext.current
    var nombreUsuario by remember { mutableStateOf(username) }
    
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
                        title = { Text("Nosotros") },
                        navigationIcon = {
                            IconButton(onClick = { drawerState.open() }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menú")
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }) {
                                Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
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
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Hero Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Conoce Nuestra Historia",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Chocolate,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "En Mil Sabores, cada postre cuenta una historia de pasión, tradición y amor por la repostería. Desde nuestros inicios, hemos dedicado nuestro corazón a crear experiencias dulces únicas.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF5D4037),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }

                // Content Section - Nuestra Pasión
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Nuestra Pasión por la Repostería",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Chocolate,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Text(
                            text = "Fundada con el sueño de llevar la dulzura a cada hogar, Mil Sabores nació de la pasión por crear postres que no solo deleiten el paladar, sino que también calienten el corazón.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5D4037),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Cada receta que preparamos lleva consigo años de experiencia, ingredientes cuidadosamente seleccionados y, sobre todo, el amor que ponemos en cada preparación. Desde tortas clásicas hasta innovaciones modernas, cada producto refleja nuestro compromiso con la excelencia.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5D4037),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Creemos que los momentos especiales merecen postres especiales. Por eso, trabajamos incansablemente para que cada celebración, cada cumpleaños, cada ocasión importante tenga el acompañamiento dulce perfecto.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5D4037)
                        )
                    }
                }

                // Valores Section
                Text(
                    text = "Nuestros Valores",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Chocolate,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                val valores = listOf(
                    "Calidad Artesanal" to "Utilizamos técnicas tradicionales combinadas con ingredientes de la más alta calidad para garantizar que cada postre sea una obra de arte culinaria.",
                    "Amor por el Detalle" to "Cada decoración, cada sabor, cada textura está cuidadosamente pensada para crear una experiencia sensorial completa que perdure en la memoria.",
                    "Ingredientes Frescos" to "Trabajamos con proveedores locales para asegurar la frescura de nuestros ingredientes, apoyando también a nuestra comunidad y garantizando sabores auténticos.",
                    "Momentos Especiales" to "Entendemos que cada ocasión es única. Por eso, personalizamos nuestros productos para que cada celebración sea verdaderamente memorable.",
                    "Compromiso Familiar" to "Somos una empresa familiar que valora las tradiciones y el trabajo en equipo. Cada miembro de nuestro equipo comparte la misma pasión por la repostería.",
                    "Innovación Constante" to "Aunque respetamos las recetas tradicionales, siempre estamos innovando para ofrecer nuevas experiencias y sabores que sorprendan a nuestros clientes."
                )

                valores.forEach { (titulo, descripcion) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFF8F0)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = titulo,
                                style = MaterialTheme.typography.titleMedium,
                                color = Chocolate,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = descripcion,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF5D4037)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Misión y Visión
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Nuestra Misión",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Chocolate,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Crear momentos de felicidad a través de postres excepcionales que combinen tradición e innovación, utilizando ingredientes de calidad y técnicas artesanales que han sido perfeccionadas a lo largo de los años.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5D4037),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        Text(
                            text = "Nuestra Visión",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Chocolate,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Ser reconocidos como la pastelería líder en la región, siendo el destino preferido para quienes buscan calidad, sabor y experiencia en cada bocado. Aspiramos a ser parte de los momentos más importantes de las familias de nuestra comunidad.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5D4037)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        }
    }
}

