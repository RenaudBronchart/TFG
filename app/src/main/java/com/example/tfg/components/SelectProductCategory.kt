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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

// Componente reutilizable que permite seleccionar una categoría de producto desde un menú desplegable.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectProductCategory(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    categories: List<String> = listOf("Palas de pádel", "Pelotas") //
) {
    // Estado local que controla si el menú está desplegado o no
    var expanded by remember { mutableStateOf(false) }
    // Contenedor que gestiona el menú desplegable y su TextField asociado
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it } // Alterna el estado al hacer clic en el TextField
    ) {
        // TextField de solo lectura que muestra la categoría seleccionada
        TextField(
            value = selectedCategory.ifEmpty { "Selecciona una categoría" },
            onValueChange = {},
            readOnly = true,
            label = { Text("Categoría") },
            trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = "Liste déroulante") },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface
            )
        )
        // Menú desplegable con opciones de categoría
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}