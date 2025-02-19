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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.components.Date
import com.example.tfg.components.SelectGender
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.EditUserViewModel
import com.example.tfg.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUser(navController: NavHostController, authViewModel: AuthViewModel, userViewModel: UserViewModel,
             editUserViewModel: EditUserViewModel) {

    val currentUser by authViewModel.user.collectAsState()
    val userData by userViewModel.usuario.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val message by editUserViewModel.mensajeConfirmacion.collectAsState()
    val nombre by editUserViewModel.nombre
    val apellido by editUserViewModel.apellido
    val email by editUserViewModel.email
    val dni by editUserViewModel.dni
    val telefono by editUserViewModel.telefono
    val genero by editUserViewModel.genero
    val fechaNacimiento by editUserViewModel.fechaNacimiento

    // Liste des champs
    val fields = listOf(
        Field("Nombre", nombre, editUserViewModel::setNombre),
        Field("Apellido", apellido, editUserViewModel::setApellido),
        Field("Email", email, editUserViewModel::setEmail),
        Field("DNI", dni, editUserViewModel::setDni),
        Field("Teléfono", telefono, editUserViewModel::setTelefono),
    )

    LaunchedEffect(Unit) {
        editUserViewModel.setMensajeConfirmacion("")
    }

    // Charger les données de l'utilisateur
    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { uid ->
            userViewModel.loadUser(uid)  // Charger l'utilisateur depuis Firestore
        }
    }

    // Charger les données dans editUserViewModel
    LaunchedEffect(userData) {
        userData?.let {
            editUserViewModel.loadDataUser(it)
        }
    }

    // Afficher un Snackbar quand le message change
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)  // Afficher le Snackbar
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                title = { Text("Mis datos") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("Profile") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        if (userData != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                fields.forEach { field ->
                    DataField(
                        label = field.label,
                        value = field.value,
                        onValueChange = field.onValueChange
                    )
                }
                // selecionar el genero
                SelectGender(
                    selectedGender = genero,
                    onGenderChange = {
                        newGender -> editUserViewModel.setGenero(newGender)
                    }
                )
                Date(
                    selectedDate = fechaNacimiento,
                    onDateChange = { newDate ->
                        editUserViewModel.setFechaNacimiento(newDate)
                    }
                )
                Button(
                    onClick = {
                        currentUser?.uid?.let { uid ->
                            editUserViewModel.saveUsuario(uid) { mensaje ->
                                editUserViewModel.setMensajeConfirmacion(mensaje)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar cambios")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataField(label: String, value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange ,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        enabled = true,
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        colors = TextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
        )
    )
}

data class Field(
    val label: String,
    val value: String,
    val onValueChange: (String) -> Unit
)