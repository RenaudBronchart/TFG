package com.example.tfg.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.components.DataField
import com.example.tfg.components.SelectDate
import com.example.tfg.components.SelectGender
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.AddUserViewModel
import com.example.tfg.components.PasswordField
import com.example.tfg.components.TopBarComponent
import com.example.tfg.viewmodel.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(navHostController: NavHostController, authViewModel: AuthViewModel, userViewModel: UserViewModel ,addUserViewModel: AddUserViewModel) {
    val userFormState by addUserViewModel.fields.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val message by addUserViewModel.messageConfirmation.collectAsState()

// Lista de campos de texto que se renderizan con DataField
// Cada entrada contiene la etiqueta y su valor actual del formulario
    val fields = listOf(
        "Nombre" to userFormState.name,
        "Apellido" to userFormState.firstname,
        "DNI" to userFormState.dni,
        "Email" to userFormState.email,
        "Teléfono" to userFormState.phone
    )
// Escucha cambios en el mensaje de confirmación y muestra una Snackbar cuando hay un mensaje nuevo
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short // Hace que la Snackbar dure poco tiempo
            )
        }
    }


// Estructura general de la pantalla con barra superior y SnackbarHost
    Scaffold(
        topBar = { TopBarComponent("Crear cuenta", navHostController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        // Contenedor principal que ocupa todo el espacio disponible y aplica padding
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)

        ) {
            // Genera campos de entrada para nombre, apellido, etc. usando DataField
            fields.forEach { (label, value) ->
                DataField(
                    label = label,
                    value = value,
                    onValueChange = { newValue ->
                        addUserViewModel.onCompletedFields(userFormState.copy(
                            name = if (label == "Nombre") newValue else userFormState.name,
                            firstname = if (label == "Apellido") newValue else userFormState.firstname,
                            dni = if (label == "DNI") newValue else userFormState.dni,
                            email = if (label == "Email") newValue else userFormState.email,
                            phone = if (label == "Teléfono") newValue else userFormState.phone
                        ))
                    }
                )
            }

            // Selección de género
            SelectGender(
                selectedGender = userFormState.gender,
                onGenderChange = { newGender ->
                    addUserViewModel.onCompletedFields(userFormState.copy(gender = newGender))
                }
            )

            // Selección de fecha de nacimiento
            SelectDate(
                selectedDate = userFormState.birthday,
                onDateChange = { newDate ->
                    addUserViewModel.onCompletedFields(userFormState.copy(birthday = newDate))
                }
            )

            // Campo de contraseña
            PasswordField(
                value = userFormState.password,
                onValueChange = { newPassword ->
                    addUserViewModel.onCompletedFields(userFormState.copy(password = newPassword))
                }
            )



            // Botón de registro
            Button(
                onClick = {
                    addUserViewModel.registerUser(authViewModel, navHostController, userViewModel) { message ->
                        addUserViewModel.setMessageConfirmation(message)
                    }
                },
                enabled = userFormState.isValid(), // Se habilita solo si los campos son válidos
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }

        }
    }
}




