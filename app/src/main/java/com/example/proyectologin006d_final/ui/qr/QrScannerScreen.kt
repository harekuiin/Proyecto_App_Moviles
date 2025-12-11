package com.example.proyectologin006d_final.ui.qr

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.proyectologin006d_final.data.database.ProductoDatabase
import com.example.proyectologin006d_final.data.repository.UsuarioRepository
import com.example.proyectologin006d_final.ui.theme.Chocolate
import com.example.proyectologin006d_final.ui.theme.CremaPastel
import com.example.proyectologin006d_final.utils.QrScanner
import com.example.proyectologin006d_final.viewmodel.QrViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScannerScreen(
    correoUsuario: String,
    hasCameraPermission: Boolean,
    onRequestPermission: () -> Unit,
    onCodigoDescuentoAplicado: (String) -> Unit,
    navController: androidx.navigation.NavController,
    viewModel: QrViewModel = viewModel()
) {
    val qrResult by viewModel.qrResult.observeAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isScanning by remember { mutableStateOf(true) }

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
                    title = { Text("Escanear Código QR") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(CremaPastel)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (!hasCameraPermission) {
                    Text(
                        "Permiso de cámara requerido",
                        style = MaterialTheme.typography.titleLarge,
                        color = Chocolate,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Para escanear códigos QR de descuento, necesitamos acceso a la cámara",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF5D4037)
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = onRequestPermission,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Chocolate
                        )
                    ) {
                        Text("Conceder permiso de cámara")
                    }
                } else if (qrResult == null && isScanning) {
                    Text(
                        "Escanea un código QR de descuento",
                        style = MaterialTheme.typography.titleLarge,
                        color = Chocolate,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp),
                        textAlign = TextAlign.Center
                    )

                    // Usar el scanner con CameraX
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ) {
                        QrScanner(
                            onQrCodeScanned = { qrContent ->
                                // Procesar el QR detectado
                                viewModel.onQrDetected(qrContent)
                                isScanning = false
                                
                                // Validar si es un código de descuento válido
                                if (viewModel.validarCodigoDescuento(qrContent)) {
                                    // Aplicar descuento al usuario en la base de datos
                                    coroutineScope.launch(Dispatchers.IO) {
                                        try {
                                            val database = ProductoDatabase.getDatabase(context)
                                            val usuarioRepository = UsuarioRepository(database.usuarioDao())
                                            val usuario = usuarioRepository.obtenerUsuarioPorCorreo(correoUsuario)
                                            
                                            if (usuario != null && usuario.codigoUsado != qrContent) {
                                                // Actualizar usuario con el código de descuento
                                                val usuarioActualizado = usuario.copy(
                                                    codigoUsado = qrContent,
                                                    descuento = 10 // 10% de descuento por código FELICES50
                                                )
                                                usuarioRepository.actualizarUsuario(usuarioActualizado)
                                                
                                                // Notificar en el hilo principal
                                                withContext(Dispatchers.Main) {
                                                    Toast.makeText(
                                                        context, 
                                                        "¡Descuento del 10% aplicado exitosamente!", 
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                    onCodigoDescuentoAplicado(qrContent)
                                                }
                                            } else if (usuario != null && usuario.codigoUsado == qrContent) {
                                                withContext(Dispatchers.Main) {
                                                    Toast.makeText(
                                                        context, 
                                                        "Este código ya fue utilizado", 
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        } catch (e: Exception) {
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(
                                                    context, 
                                                    "Error al aplicar descuento: ${e.message}", 
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "QR detectado: $qrContent", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )

                        // Overlay para ayudar al escaneo
                        Surface(
                            modifier = Modifier
                                .size(250.dp)
                                .align(Alignment.Center),
                            color = Color.Transparent,
                            shape = MaterialTheme.shapes.medium,
                            border = BorderStroke(
                                3.dp,
                                Chocolate
                            )
                        ) {}
                    }

                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Enfoca el código QR en el marco central",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF5D4037),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Código válido: FELICES50",
                        style = MaterialTheme.typography.bodySmall,
                        color = Chocolate,
                        fontWeight = FontWeight.Bold
                    )
                } else if (qrResult != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        if (viewModel.validarCodigoDescuento(qrResult!!.content)) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF4CAF50)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "✅ Código de Descuento Válido",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Text(
                                        qrResult!!.content,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        } else {
                            Text(
                                "QR Detectado:",
                                style = MaterialTheme.typography.titleMedium,
                                color = Chocolate,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Text(
                                    qrResult!!.content,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(16.dp),
                                    color = Color(0xFF5D4037)
                                )
                            }
                        }
                        
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                viewModel.clearResult()
                                isScanning = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Chocolate
                            )
                        ) {
                            Text("Escanear otro código QR")
                        }
                    }
                }
            }
        }
    }
}

