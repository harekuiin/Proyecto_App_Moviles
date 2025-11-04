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
import com.example.proyectologin006d_final.ui.nosotros.NosotrosScreen
import com.example.proyectologin006d_final.ui.contacto.ContactoScreen
import com.example.proyectologin006d_final.ui.admin.VerDatosRoomScreen
import com.example.proyectologin006d_final.ui.qr.QrScannerScreen
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.proyectologin006d_final.utils.CameraPermissionHelper

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
            route = "detalle/{id}/{correo}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                },
                navArgument("correo") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id").orEmpty()
            val correo = backStackEntry.arguments?.getString("correo").orEmpty()
            DetalleProductoScreen(codigo = id, correoUsuario = correo, navController = navController)
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

        composable(
            route = "nosotros/{correo}",
            arguments = listOf(
                navArgument("correo") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val correo = backStackEntry.arguments?.getString("correo").orEmpty()
            NosotrosScreen(navController = navController, username = correo)
        }

        composable(
            route = "contacto/{correo}",
            arguments = listOf(
                navArgument("correo") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val correo = backStackEntry.arguments?.getString("correo").orEmpty()
            ContactoScreen(navController = navController, username = correo)
        }

        composable(
            route = "ver_datos/{correo}",
            arguments = listOf(
                navArgument("correo") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val correo = backStackEntry.arguments?.getString("correo").orEmpty()
            VerDatosRoomScreen(navController = navController, username = correo)
        }

        composable(
            route = "qr_scanner/{correo}",
            arguments = listOf(
                navArgument("correo") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val correo = backStackEntry.arguments?.getString("correo").orEmpty()
            val context = androidx.compose.ui.platform.LocalContext.current
            var hasCameraPermission by remember { 
                mutableStateOf(CameraPermissionHelper.hasCameraPermission(context)) 
            }
            
            val requestPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                hasCameraPermission = isGranted
            }
            
            QrScannerScreen(
                correoUsuario = correo,
                hasCameraPermission = hasCameraPermission,
                onRequestPermission = {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                },
                onCodigoDescuentoAplicado = { codigo ->
                    // Cuando se detecta un código de descuento válido, volver a la pantalla anterior
                    navController.popBackStack()
                },
                navController = navController
            )
        }
    }
}
