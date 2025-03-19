package com.example.tfg.components


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable


@Composable
@OptIn(ExperimentalMaterial3Api::class)
// message par el texto del dialogo, y dos acions para confirmar o cancelar
fun mostrarMessageConfirmation(message:String, onConfirm:() -> Unit, onDismiss:() -> Unit) {

    // componente de JetPack para mostrar ventana
    AlertDialog(
        onDismissRequest = { onDismiss()}, // si usuario pincha fuera, se quita la ventana
        // botn de Si para confirmar cuando pincha en si , llama a la funcion onconfirm
        confirmButton = {
            TextButton(onClick = {onConfirm()}) {
                Text("Si")
            }
        },
        // button para cancelar,
        dismissButton = {
            TextButton(onClick = {onDismiss()}) {
                Text("Cancelar")
            }
        },
        title = { Text  ("Confirmation")},
        text = { Text(message)}
    )
}