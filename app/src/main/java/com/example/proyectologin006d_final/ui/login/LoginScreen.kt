package com.example.proyectologin006d_final.ui.login



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectologin006d_final.ui.login.LoginUiState
import com.example.proyectologin006d_final.ui.login.LoginViewModel
import com.example.proyectologin006d_final.viewmodel.AuthViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
// Permite usar funciones Material 3 qe son experimentales
@Composable  // Genera Interfz Garfica

fun LoginScreen(
    navController: NavController,
    vm: LoginViewModel = viewModel(
        factory = AuthViewModelFactory(LocalContext.current.applicationContext as android.app.Application)
    )
) {
    val state = vm.uiState
    var showPass by remember { mutableStateOf(false) }

    Scaffold (
            // Crea Estuctra basica de la pantalla Se define topBar, BottomBar
            topBar = {
                TopAppBar(
                    title = {Text("Pastelería Mil Sabores",
                        color =MaterialTheme.colorScheme.onPrimary,
                    )},
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )

                // Crea un AppBar con un titulo

            }// fin topBar
        ) // fin Scaff
        {// Inicio Inner
                innerPadding ->
            // Representa el espacio interno para que no choque con el topBar

            Column (  //   Colaca los elementos de la Ui
                modifier = Modifier
                    .padding( innerPadding)
                    // Evita que quede oculto
                    .fillMaxSize() // Hace que la columnna tome el todo el tamaño
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally  // Centra horizontalmente
                //Define  que elementos dentro la columna estaran separados por 20.dp
            ) {
            // inicio Contenido
                Text(text="Pastelería Mil Sabores",
                    style= MaterialTheme.typography.headlineLarge,
                    color=MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold


                ) // Muestra un texto simple en la pantalla
            
            Text(text="¡Celebra 50 años de tradición!",
                    style= MaterialTheme.typography.bodyMedium,
                    color=MaterialTheme.colorScheme.onSurface
                )




                // Logo de la pastelería (por ahora sin imagen, se puede agregar después)
                Spacer(modifier = Modifier.height(20.dp))


// agregar un espacio entre la imagen y el boton

                Spacer(modifier = Modifier.height(66.dp))







                OutlinedTextField(
                    value = state.correo,
                    onValueChange = vm::onCorreoChange,
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.95f),
                    placeholder = { Text("usuario@gmail.com o @duoc.cl") }
                ) // fin correo


                OutlinedTextField(
                    value = state.password,
                    onValueChange = vm::onPasswordChange,
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { showPass = !showPass }) {
                            Text(if (showPass) "Ocultar" else "Ver")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.95f)
                ) // fin passw


                if (state.error != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = state.error ?: "",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }


// agregar un espacio entre la imagen y el boton

                Spacer(modifier = Modifier.height(66.dp))

                Button(onClick = {
                    vm.submit { correo, isAdmin ->
                        if (isAdmin) {
                            navController.navigate("admin/$correo") {
                                popUpTo("login") { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate("home/$correo") {
                                popUpTo("login") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                },
                    enabled=!state.isLoading,
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text(if (state.isLoading) "Validando..." else "Iniciar sesión")
                }

                TextButton(
                    onClick = { navController.navigate("register") }
                ) {
                    Text("¿No tienes cuenta? Regístrate")
                }


            }// fin Contenido

        } // Fin inner
}// Fin LoginScreen
