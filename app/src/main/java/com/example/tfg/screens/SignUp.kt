package com.example.tfg.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.components.DataField
import com.example.tfg.components.Date
import com.example.tfg.components.SelectGender
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.UserViewModel
import com.example.tfg.components.PasswordField



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(navController: NavHostController, authViewModel: AuthViewModel, userViewModel: UserViewModel) {
    val nombre by userViewModel.nombre.observeAsState("")
    val apellido by userViewModel.apellido.observeAsState("")
    val dni by userViewModel.dni.observeAsState("")
    val email by userViewModel.email.observeAsState("")
    val telefono by userViewModel.telefono.observeAsState("")
    val genero by userViewModel.genero.observeAsState("")
    val fechaNacimiento by userViewModel.fechaNacimiento.observeAsState("")
    //val role by viewModel.role.observeAsState("user")
    val contraseña by userViewModel.contraseña.observeAsState("")
    val isButtonEnabled by userViewModel.isButtonEnable.observeAsState(false)
    val snackbarHostState = remember { SnackbarHostState() }
    val message by userViewModel.messageConfirmation.collectAsState()

    val fields = listOf(
        Triple("Nombre", nombre, "nombre"),
        Triple("Apellido", apellido, "apellido"),
        Triple("DNI", dni, "dni"),
        Triple("Email", email, "email"),
        Triple("Teléfono", telefono, "telefono")
    )

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                title = { Text("Crear cuenta") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("Login") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)

        ) {

            fields.forEach { (label, value, key) ->
                DataField(
                    label = label,
                    value = value,
                    onValueChange = { newValue ->
                        when (key) {
                            "nombre" -> userViewModel.onCompletedFields(newValue, apellido, dni, email, telefono, genero, fechaNacimiento, contraseña)
                            "apellido" -> userViewModel.onCompletedFields(nombre, newValue, dni, email, telefono, genero, fechaNacimiento, contraseña)
                            "dni" -> userViewModel.onCompletedFields(nombre, apellido, newValue, email, telefono, genero, fechaNacimiento, contraseña)
                            "email" -> userViewModel.onCompletedFields(nombre, apellido, dni, newValue, telefono, genero, fechaNacimiento, contraseña)
                            "telefono" -> userViewModel.onCompletedFields(nombre, apellido, dni, email, newValue, genero, fechaNacimiento, contraseña)
                        }
                    }
                )
            }

            // Sélection du genre
            SelectGender(
                selectedGender = genero,
                onGenderChange = { newGender ->
                    userViewModel.onCompletedFields(nombre, apellido, dni, email, telefono, newGender, fechaNacimiento, contraseña)
                }
            )

            // Sélection de la date de naissance
            Date(
                selectedDate = fechaNacimiento,
                onDateChange = { newDate ->
                    userViewModel.onCompletedFields(nombre, apellido, dni, email, telefono, genero, newDate, contraseña)
                }
            )

            // campo para la contrasena
            PasswordField(
                value = contraseña,
                onValueChange = { userViewModel.onCompletedFields(nombre, apellido, dni, email, telefono, genero, fechaNacimiento, it) }
            )
            Button(
                onClick = {
                    userViewModel.registerUser(authViewModel, navController) { message ->
                        userViewModel.setMessageConfirmation(message)
                    }
                },
                enabled = isButtonEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }

        }
    }
}




