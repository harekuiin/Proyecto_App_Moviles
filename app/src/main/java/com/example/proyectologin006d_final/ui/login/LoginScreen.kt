package com.example.proyectologin006d_final.ui.login



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.proyectologin006d_final.ui.theme.Chocolate
import com.example.proyectologin006d_final.ui.theme.CremaPastel
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectologin006d_final.ui.login.LoginUiState
import com.example.proyectologin006d_final.ui.login.LoginViewModel


@OptIn(ExperimentalMaterial3Api::class)
// Permite usar funciones Material 3 qe son experimentales
@Composable  // Genera Interfz Garfica

fun LoginScreen(   navController: NavController,
                   vm: LoginViewModel = viewModel()
) {
    val state = vm.uiState
    var showPass by remember { mutableStateOf(false) }


    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Chocolate,
            background = CremaPastel,
            surface = CremaPastel,
            onPrimary = CremaPastel,
            onBackground = Color(0xFF5D4037),
            onSurface = Color(0xFF5D4037)
        )
    ){ // inicio Aplicar Material



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
                    .background(CremaPastel),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally  // Centra horizontalmente
                //Define  que elementos dentro la columna estaran separados por 20.dp
            )// fin column
            {// inicio Contenido
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
                    value = state.username,
                    onValueChange = vm::onUsernameChange,
                    label = { Text("Usuario") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.95f)
                ) // fin user


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
                    vm.submit { user ->
                        navController.navigate("home/$user") {
                            popUpTo("login"){inclusive = true} // no volver al login con Back
                            launchSingleTop = true
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


    } // fin Aplicar Material
}// Fin HomeScreen


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    // Crear un navController de manera ficticia para fines de la vista previa
    val navController = rememberNavController()

    // Puedes usar un ViewModel simulado aquí si no tienes acceso a uno real
    val vm = LoginViewModel() // Suponiendo que LoginViewModel está correctamente configurado para la vista previa

    LoginScreen(navController = navController, vm = vm)
}