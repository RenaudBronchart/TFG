package com.example.tfg.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGender(selectedGender: String, onGenderChange: (String) -> Unit) {

    val options = listOf("Hombre", "Mujer")
    var expanded by remember { mutableStateOf(false) }


    ExposedDropdownMenuBox( // componente para crear un menú desplegable
        expanded = expanded, // para definir si el menu esta expandido o no
        onExpandedChange = { expanded = it } // actualiza el estado cuando el usuario interactua
    ) {
        TextField(
            value = selectedGender,
            onValueChange = {},
            readOnly = true,
            label = { Text("Género") },
            trailingIcon = {
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "List desplegable")
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )


        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
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