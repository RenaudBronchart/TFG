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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.EditUserViewModel
import com.example.tfg.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyData(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    viewModel: UsuarioViewModel,
    editUserViewModel: EditUserViewModel
) {
    val currentUser by authViewModel.user.collectAsState()
    val usuarioData by viewModel.usuario.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {

        editUserViewModel.setMensajeConfirmacion("")
    }

    // Charger les données de l'utilisateur
    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { uid ->
            viewModel.loadUsuario(uid)  // Charger l'utilisateur depuis Firestore
        }
    }

    // Charger les données dans editUserViewModel
    LaunchedEffect(usuarioData) {
        usuarioData?.let {
            editUserViewModel.cargarDatosUsuario(it)
        }
    }

    // Observer les valeurs mises à jour dans EditUserViewModel
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
        Field("Género", genero, editUserViewModel::setGenero),
        Field("Fecha de nacimiento", fechaNacimiento, editUserViewModel::setFechaNacimiento)
    )

    // Observer des changements de message pour afficher un Snackbar
    val mensaje by editUserViewModel.mensajeConfirmacion.collectAsState()

    // Afficher un Snackbar quand le message change
    LaunchedEffect(mensaje) {
        if (mensaje.isNotEmpty()) {
            snackbarHostState.showSnackbar(mensaje)  // Afficher le Snackbar
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
        if (usuarioData != null) {
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
                Button(
                    onClick = {
                        currentUser?.uid?.let { uid ->
                            editUserViewModel.saveUsuario(uid) { mensaje ->
                                // Enregistrer le message de confirmation dans le ViewModel
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
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.surface,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

data class Field(
    val label: String,
    val value: String,
    val onValueChange: (String) -> Unit
)