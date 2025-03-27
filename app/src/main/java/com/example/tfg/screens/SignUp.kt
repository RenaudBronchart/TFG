package com.example.tfg.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import com.example.tfg.viewmodel.UserViewModel
import com.example.tfg.components.PasswordField
import com.example.tfg.components.TopBarComponent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(navHostController: NavHostController, authViewModel: AuthViewModel, userViewModel: UserViewModel) {
    val name by userViewModel.name.collectAsState("")
    val firstname by userViewModel.firstname.collectAsState("")
    val dni by userViewModel.dni.collectAsState("")
    val email by userViewModel.email.collectAsState("")
    val phone by userViewModel.phone.collectAsState("")
    val gender by userViewModel.gender.collectAsState("")
    val birthday by userViewModel.birthday.collectAsState("")
    //val role by viewModel.role.observeAsState("user")
    val password by userViewModel.password.collectAsState("")
    val isButtonEnabled by userViewModel.isButtonEnable.collectAsState(false)
    val snackbarHostState = remember { SnackbarHostState() }
    val message by userViewModel.messageConfirmation.collectAsState()

    val fields = listOf(
        Triple("Nombre", name, "nombre"),
        Triple("Apellido", firstname, "apellido"),
        Triple("DNI", dni, "dni"),
        Triple("Email", email, "email"),
        Triple("Teléfono", phone, "telefono")
    )

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)
        }
    }
    Scaffold(
        topBar = { TopBarComponent("Crear cuenta", navHostController) },
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
                            "nombre" -> userViewModel.onCompletedFields(newValue, firstname, dni, email, phone, gender, birthday, password)
                            "apellido" -> userViewModel.onCompletedFields(name, newValue, dni, email, phone, gender, birthday, password)
                            "dni" -> userViewModel.onCompletedFields(name, firstname, newValue, email, phone, gender, birthday, password)
                            "email" -> userViewModel.onCompletedFields(name, firstname, dni, newValue, phone, gender, birthday, password)
                            "telefono" -> userViewModel.onCompletedFields(name, firstname, dni, email, newValue, gender, birthday, password)
                        }
                    }
                )
            }

            // Sélection du genre
            SelectGender(
                selectedGender = gender,
                onGenderChange = { newGender ->
                    userViewModel.onCompletedFields(name, firstname, dni, email, phone, newGender, birthday, password)
                }
            )

            // Sélection de la date de naissance
            SelectDate(
                selectedDate = birthday,
                onDateChange = { newDate ->
                    userViewModel.onCompletedFields(name, firstname, dni, email, phone, gender, newDate, password)
                }
            )

            // campo para la contrasena
            PasswordField(
                value = password,
                onValueChange = { userViewModel.onCompletedFields(name, firstname, dni, email, phone, gender, birthday, it) }
            )
            Button(
                onClick = {
                    userViewModel.registerUser(authViewModel, navHostController) { message ->
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




