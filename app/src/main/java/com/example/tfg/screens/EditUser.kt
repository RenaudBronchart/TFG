package com.example.tfg.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.components.DataField
import com.example.tfg.components.Date
import com.example.tfg.components.SelectGender
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.EditUserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUser(navController: NavHostController, authViewModel: AuthViewModel, editUserViewModel: EditUserViewModel) {
    val currentUser by authViewModel.user.collectAsState()
    val usuario by editUserViewModel.usuario.collectAsState() // Manejar el estado de usuario
    val snackbarHostState = remember { SnackbarHostState() }
    val message by editUserViewModel.mensajeConfirmacion.collectAsState()
    val isLoading by editUserViewModel.isLoading.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Resetear mensaje de confirmación cuando se carga la pantalla
    LaunchedEffect(Unit) {
        editUserViewModel.resetMessage()
    }

    // Cargar datos del usuario cuando el UID cambie
    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { editUserViewModel.loadUser(it) }
    }

    // Mostrar mensaje en Snackbar si hay un mensaje de confirmación
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)
            editUserViewModel.resetMessage() // Limpiar mensaje después de mostrarlo
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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyColumn {
                item {
                    DataField(label = "Nombre", value = usuario.nombre, onValueChange = editUserViewModel::setNombre)
                    DataField(label = "Apellido", value = usuario.apellido, onValueChange = editUserViewModel::setApellido)
                    DataField(label = "Email", value = usuario.email, onValueChange = editUserViewModel::setEmail)
                    DataField(label = "Teléfono", value = usuario.telefono, onValueChange = editUserViewModel::setTelefono)
                    DataField(label = "DNI", value = usuario.dni, onValueChange = editUserViewModel::setDni)

                    Spacer(modifier = Modifier.height(16.dp))

                    SelectGender(
                        selectedGender = usuario.genero,
                        onGenderChange = editUserViewModel::setGenero
                    )
                    Date(
                        selectedDate = usuario.fechaNacimiento,
                        onDateChange = editUserViewModel::setFechaNacimiento
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            currentUser?.uid?.let { uid ->
                                editUserViewModel.updateUser(uid) { message ->
                                    editUserViewModel.setMessageConfirmation(message)
                                    coroutineScope.launch {
                                        delay(500)
                                    }
                                    navController.popBackStack()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isLoading) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(end = 8.dp)
                                )
                                Text("Actualizando...")
                            }
                        } else {
                            Text("Guardar cambios")
                        }
                    }
                }
            }
        }
    }}
