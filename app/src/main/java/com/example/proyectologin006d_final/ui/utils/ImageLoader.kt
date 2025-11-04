package com.example.proyectologin006d_final.ui.utils

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberImageFromAssets(
    context: Context,
    assetPath: String,
    key: String
): android.graphics.Bitmap? {
    var bitmap by remember(key) { mutableStateOf<android.graphics.Bitmap?>(null) }
    
    LaunchedEffect(key) {
        if (assetPath.isNotEmpty()) {
            bitmap = withContext(Dispatchers.IO) {
                try {
                    val rutaImagen = assetPath.removePrefix("/")
                    val inputStream = context.assets.open(rutaImagen)
                    val bitmapResult = BitmapFactory.decodeStream(inputStream)
                    inputStream.close()
                    
                    if (bitmapResult == null) {
                        android.util.Log.e("ImageLoader", "No se pudo decodificar bitmap: $rutaImagen")
                    } else {
                        android.util.Log.d("ImageLoader", "Imagen cargada exitosamente: $rutaImagen")
                    }
                    
                    bitmapResult
                } catch (e: Exception) {
                    android.util.Log.e("ImageLoader", "Error cargando imagen desde assets: $assetPath", e)
                    null
                }
            }
        }
    }
    
    return bitmap
}

