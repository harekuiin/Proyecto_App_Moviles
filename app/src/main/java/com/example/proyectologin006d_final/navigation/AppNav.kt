package com.example.proyectologin006d_final.navigation


import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectologin006d_final.ui.login.LoginScreen
import com.example.proyectologin006d_final.ui.home.MuestraDatosScreen
import com.example.proyectologin006d_final.ui.home.MuestraDatosScreen
import com.example.proyectologin006d_final.ui.login.LoginScreen
import com.example.proyectologin006d_final.view.DrawerMenu
import com.example.proyectologin006d_final.view.ProductoFormScreen

@Composable

fun AppNav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController)
        } // fin composable

        composable(
            //route = "muestraDatos/{username}",
            route = "DrawerMenu/{username}",
            arguments = listOf(
                navArgument("username") {
                    type = NavType.StringType
                }// fin ListOf
            )// fin ListOf
        ) // fin composable

        { // inicio back
                backStackEntry ->
            val username = backStackEntry.arguments?.getString("username").orEmpty()
           // MuestraDatosScreen(username =username,navController=navController)

            DrawerMenu(username =username,navController=navController)
        } // Termino back

        // Rutas para la pantalla ProductoForm

        composable(
            route="ProductoFormScreen/{nombre}/{precio}",
            arguments = listOf(
                navArgument("nombre") {type = NavType.StringType },
                navArgument("precio") {type = NavType.StringType }
            )// fin ListOf
        ) // fin composable 3
        { // inicio back 2
            backStackEntry ->
            val nombre= Uri.decode(backStackEntry.arguments?.getString("nombre") ?:"")
            val precio= Uri.decode(backStackEntry.arguments?.getString("precio") ?:"")

            ProductoFormScreen(navController,nombre=nombre, precio=precio)

        } // inicio back 2





    }// Fin Nav
} // fun AppNav()