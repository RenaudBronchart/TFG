package com.example.tfg.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun PasswordField(value: String, onValueChange: (String) -> Unit) {
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    fun validatePassword(password: String): Boolean {
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val isLongEnough = password.length >= 6

        errorMessage = when {
            password.isEmpty() -> "La contraseña no puede ser vacía"
            !isLongEnough -> "La contraseña debe tener al menos 6 caracteres"
            !hasUpperCase -> "La contraseña debe contener al menos una letra mayúscula"
            !hasDigit -> "La contraseña debe contener al menos un número"
            else -> ""
        }

        return hasUpperCase && hasDigit && isLongEnough
    }

    TextField(
        value = value,
        onValueChange = {
            onValueChange(it)
            validatePassword(it)
        },
        label = { Text("Contraseña") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = "Toggle password visibility")
            }
        },
        isError = errorMessage.isNotEmpty(), // ✅ Placé correctement
        colors = TextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
        )
    )

    if (errorMessage.isNotEmpty()) {
        Text(
            text = errorMessage,
            color = Color.Red,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
        )
    }
}

