package com.example.tfg.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGender(selectedGender: String, onGenderChange: (String) -> Unit) {
    // Opciones disponibles en el menú
    val options = listOf("Hombre", "Mujer")
    // Estado para controlar si el menú está expandido (visible)
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it } // // Aquí se cambia el estado
    ) {
        TextField(
            value = selectedGender,
            onValueChange = {},// No editable directamente
            readOnly = true,
            label = { Text("Género") },
            trailingIcon = {
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "List desplegable")
            },
            modifier = Modifier
                .menuAnchor() // Asegura que el menú aparezca anclado correctamente
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
        // componente que representa el menú desplegable con la lista
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false } //  // Cerrar el menú si el usuario toca fuera
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onGenderChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}