package com.example.proyectologin006d_final.ui.contacto

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.proyectologin006d_final.data.database.ProductoDatabase
import com.example.proyectologin006d_final.data.repository.UsuarioRepository
import com.example.proyectologin006d_final.ui.home.NavigationDrawerContent
import com.example.proyectologin006d_final.ui.theme.Chocolate
import com.example.proyectologin006d_final.ui.theme.CremaPastel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactoScreen(
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

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Validación de dominio permitido
    fun isAllowedDomain(email: String): Boolean {
        if (email.isBlank()) return false
        val lowerEmail = email.lowercase()
        return lowerEmail.endsWith("@gmail.com") || 
               lowerEmail.endsWith("@duoc.cl") || 
               lowerEmail.endsWith("@profesor.duoc.cl")
    }

    fun handleSubmit() {
        // Validaciones
        if (nombre.isBlank()) {
            error = "El nombre es obligatorio"
            return
        }

        if (email.isBlank()) {
            error = "El correo es obligatorio"
            return
        }

        if (!isAllowedDomain(email)) {
            error = "El correo debe ser @gmail.com, @duoc.cl o @profesor.duoc.cl"
            return
        }

        if (mensaje.isBlank()) {
            error = "El mensaje es obligatorio"
            return
        }

        if (mensaje.length < 10) {
            error = "El mensaje debe tener al menos 10 caracteres"
            return
        }

        error = null
        isLoading = true

        // Simular envío (en producción, aquí harías una llamada a API)
        coroutineScope.launch {
            delay(1000) // Simular delay de red
            isLoading = false
            successMessage = "¡Mensaje enviado! Te responderemos pronto."
            nombre = ""
            email = ""
            mensaje = ""
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
                        title = { Text("Contacto") },
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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Text(
                    text = "Contacto",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Chocolate,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "¡Nos encantaría saber de ti!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF5D4037),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Formulario
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.95f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Envíanos un mensaje",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Chocolate,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { 
                                nombre = it
                                error = null
                            },
                            label = { Text("Nombre") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { 
                                email = it
                                error = null
                            },
                            label = { Text("Correo Electrónico") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("usuario@gmail.com o @duoc.cl") }
                        )

                        OutlinedTextField(
                            value = mensaje,
                            onValueChange = { 
                                mensaje = it
                                error = null
                            },
                            label = { Text("Mensaje") },
                            minLines = 5,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (error != null) {
                            Text(
                                text = error ?: "",
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        if (successMessage != null) {
                            Text(
                                text = successMessage ?: "",
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Button(
                            onClick = { handleSubmit() },
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Chocolate
                            )
                        ) {
                            Text(if (isLoading) "Enviando..." else "Enviar")
                        }
                    }
                }
            }
        }
        }
    }
}

