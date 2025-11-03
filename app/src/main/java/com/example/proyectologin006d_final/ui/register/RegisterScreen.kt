package com.example.proyectologin006d_final.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectologin006d_final.ui.theme.CremaPastel
import com.example.proyectologin006d_final.ui.theme.Chocolate
import com.example.proyectologin006d_final.viewmodel.AuthViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    vm: RegisterViewModel = viewModel(
        factory = AuthViewModelFactory(LocalContext.current.applicationContext as android.app.Application)
    )
) {
    val state = vm.uiState
    var showPass by remember { mutableStateOf(false) }
    var showConfirmPass by remember { mutableStateOf(false) }

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
                    title = { Text("Pastelería Mil Sabores") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Chocolate,
                        titleContentColor = CremaPastel
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(CremaPastel),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¡Únete a nuestra familia!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Chocolate,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Celebra 50 años de tradición",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF5D4037)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.run,
                    onValueChange = vm::onRunChange,
                    label = { Text("RUT (sin puntos, con guion)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.95f)
                )

                OutlinedTextField(
                    value = state.nombre,
                    onValueChange = vm::onNombreChange,
                    label = { Text("Nombre completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.95f)
                )

                OutlinedTextField(
                    value = state.email,
                    onValueChange = vm::onEmailChange,
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.95f),
                    placeholder = { Text("usuario@gmail.com o @duoc.cl") }
                )

                OutlinedTextField(
                    value = state.fechaNacimiento,
                    onValueChange = vm::onFechaNacimientoChange,
                    label = { Text("Fecha de nacimiento") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.95f),
                    placeholder = { Text("YYYY-MM-DD") }
                )

                OutlinedTextField(
                    value = state.telefono,
                    onValueChange = vm::onTelefonoChange,
                    label = { Text("Teléfono") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.95f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(0.95f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = state.pais,
                        onValueChange = vm::onPaisChange,
                        label = { Text("País") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = state.comuna,
                        onValueChange = vm::onComunaChange,
                        label = { Text("Comuna") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = state.password,
                    onValueChange = vm::onPasswordChange,
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { showPass = !showPass }) {
                            Text(if (showPass) "Ocultar" else "Ver", style = MaterialTheme.typography.bodySmall)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.95f)
                )

                OutlinedTextField(
                    value = state.confirmPassword,
                    onValueChange = vm::onConfirmPasswordChange,
                    label = { Text("Confirmar contraseña") },
                    singleLine = true,
                    visualTransformation = if (showConfirmPass) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { showConfirmPass = !showConfirmPass }) {
                            Text(if (showConfirmPass) "Ocultar" else "Ver", style = MaterialTheme.typography.bodySmall)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.95f)
                )

                OutlinedTextField(
                    value = state.codigoPromocional,
                    onValueChange = vm::onCodigoPromocionalChange,
                    label = { Text("Código promocional (opcional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.95f),
                    placeholder = { Text("Ej: FELICES50") }
                )

                if (state.error != null) {
                    Text(
                        text = state.error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (state.successMessage != null) {
                    Text(
                        text = state.successMessage ?: "",
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        vm.submit { correo, isAdmin ->
                            if (isAdmin) {
                                navController.navigate("admin/$correo") {
                                    popUpTo("register") { inclusive = true }
                                    launchSingleTop = true
                                }
                            } else {
                                navController.navigate("home/$correo") {
                                    popUpTo("register") { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        }
                    },
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth(0.6f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Chocolate
                    )
                ) {
                    Text(if (state.isLoading) "Registrando..." else "Registrarse")
                }

                TextButton(
                    onClick = { navController.navigate("login") }
                ) {
                    Text("¿Ya tienes cuenta? Inicia sesión")
                }
            }
        }
    }
}

