package com.example.tfg.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember


@Composable
fun PickImageFromGallery(
    onImagePicked: (Uri?) -> Unit // Función que se ejecutará cuando el usuario seleccione una imagen
): () -> Unit {
    // Creamos un launcher que abre la galería para seleccionar una imagen
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // Cuando el usuario selecciona una imagen, pasamos el Uri a la función recibida
        onImagePicked(uri)
    }

    // Devolvemos una función que, al ser llamada, abrirá la galería
    return remember {
        { launcher.launch("image/*") } // Solo permitimos seleccionar archivos de tipo imagen
    }
}



