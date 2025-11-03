package com.example.proyectologin006d_final.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectologin006d_final.ui.detalle.DetalleProductoScreen
import com.example.proyectologin006d_final.ui.home.HomeScreen
import com.example.proyectologin006d_final.ui.login.LoginScreen
import com.example.proyectologin006d_final.ui.register.RegisterScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController)
        }

        composable("register") {
            RegisterScreen(navController = navController)
        }

        composable(
            route = "home/{correo}",
            arguments = listOf(
                navArgument("correo") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val correo = backStackEntry.arguments?.getString("correo").orEmpty()
            HomeScreen(username = correo, navController = navController)
        }

        composable(
            route = "detalle/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id").orEmpty()
            DetalleProductoScreen(codigo = id, navController = navController)
        }
        
        composable(
            route = "admin/{correo}",
            arguments = listOf(
                navArgument("correo") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val correo = backStackEntry.arguments?.getString("correo").orEmpty()
            // Por ahora redirigir a home, se puede crear AdminScreen después
            HomeScreen(username = correo, navController = navController)
        }
    }
}
