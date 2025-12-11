package com.example.proyectologin006d_final

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.proyectologin006d_final.navigation.AppNav
import com.example.proyectologin006d_final.ui.theme.ProyectoLogin006D_finalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            setContent {
                ProyectoLogin006D_finalTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNav()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error al inicializar la aplicación", e)
            // Mostrar un mensaje de error en lugar de pantalla negra
            setContent {
                ProyectoLogin006D_finalTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        androidx.compose.material3.Text(
                            text = "Error al cargar la aplicación. Por favor, reinicia la app.",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
