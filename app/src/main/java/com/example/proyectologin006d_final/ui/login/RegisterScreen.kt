package com.example.proyectologin006d_final.ui.login

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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val nombre = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val edad = remember { mutableStateOf("") }
    val codigo = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Crear cuenta",
                    color = MaterialTheme.colorScheme.onPrimary,
                )})
            }
        ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Regístrate para acceder al catálogo")
            OutlinedTextField(value = nombre.value, onValueChange = { nombre.value = it }, label = { Text("Nombre completo") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = email.value, onValueChange = { email.value = it }, label = { Text("Correo electrónico") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = edad.value, onValueChange = { edad.value = it }, label = { Text("Edad") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = codigo.value, onValueChange = { codigo.value = it }, label = { Text("Código (opcional)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = password.value, onValueChange = { password.value = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
            Spacer(Modifier.height(8.dp))
            Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
                Text("Crear cuenta")
            }
        }
        }
    }
}


