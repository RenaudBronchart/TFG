package com.example.tfg.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import com.example.tfg.components.SelectDate
import com.example.tfg.components.SelectGender
import com.example.tfg.components.TopBarComponent
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.EditUserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun EditUserMyProfil(navController: NavHostController, authViewModel: AuthViewModel, editUserViewModel: EditUserViewModel,userId: String) {
    val user by editUserViewModel.user.collectAsState() // Manejar el estado de usuario
    val currentUser by authViewModel.user.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val message by editUserViewModel.messageConfirmation.collectAsState()
    val isLoading by editUserViewModel.isLoading.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Resetear mensaje de confirmación cuando se carga la pantalla
    LaunchedEffect(Unit) {
        editUserViewModel.resetMessage()
    }
    // Verificar si currentUser está disponible y tiene UID
    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { userId ->
            editUserViewModel.loadUser(userId) // Aquí se usa el UID del currentUser para cargar datos.
        }
    }

    // Mostrar mensaje en Snackbar si hay un mensaje de confirmación
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)
            delay(2000)  // Muestra el mensaje durante 2 segundos
            editUserViewModel.resetMessage() // Limpia el mensaje después de mostrarlo
        }
    }
    Scaffold(
        topBar = { TopBarComponent("Datos", navController)},
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
                    // Campos editables del usuario
                    DataField(label = "Nombre", value = user?.name ?:"", onValueChange = { editUserViewModel.updateUserField("name", it) })
                    DataField(label = "Apellido", value = user?.firstname ?:"", onValueChange ={ editUserViewModel.updateUserField("firstname", it) })
                    DataField(label = "Email", value = user?.email ?:"", onValueChange = { editUserViewModel.updateUserField("email", it) })
                    DataField(label = "Teléfono", value = user?.phone ?:"", onValueChange = { editUserViewModel.updateUserField("phone", it) })
                    DataField(label = "DNI", value = user?.dni ?:"", onValueChange ={ editUserViewModel.updateUserField("dni", it) })

                    SelectGender(
                        selectedGender = user?.gender ?: "",
                        onGenderChange = { editUserViewModel.updateUserField("gender", it) }
                    )
                    SelectDate(
                        selectedDate = user?.birthday ?: "",
                        onDateChange = { editUserViewModel.updateUserField("birthday", it) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // Actualizamos los datos del usuario
                            editUserViewModel.updateUser(userId) { message ->
                                editUserViewModel.setMessage(message)
                                coroutineScope.launch {
                                    delay(2000)
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
    }
}
