package com.example.proyectologin006d_final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.proyectologin006d_final.navigation.AppNav
import com.example.proyectologin006d_final.navigation.AppNav
import com.example.proyectologin006d_final.ui.theme.ProyectoLogin006D_finalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoLogin006D_finalTheme {
                AppNav()
            }
        }
    }
}
