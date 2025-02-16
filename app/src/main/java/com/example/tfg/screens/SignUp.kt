package com.example.tfg.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.components.Date
import com.example.tfg.components.SelectGender
import com.example.tfg.models.Usuario
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.UserViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(navController: NavHostController, authViewModel: AuthViewModel, viewModel: UserViewModel) {

    val db = FirebaseFirestore.getInstance()
    val nameCollection = "usuarios"

    var selectedGender by remember { mutableStateOf("Selecciona una opción") }

    // Observer les valeurs depuis le ViewModel
    val nombre by viewModel.nombre.observeAsState("")
    val apellido by viewModel.apellido.observeAsState("")
    val dni by viewModel.dni.observeAsState("")
    val email by viewModel.email.observeAsState("")
    val telefono by viewModel.telefono.observeAsState("")
    val genero by viewModel.genero.observeAsState("")
    val fechaNacimiento by viewModel.fechaNacimiento.observeAsState("")
    val contraseña by viewModel.contraseña.observeAsState("")
    val isButtonEnabled by viewModel.isButtonEnable.observeAsState(false)

    val coroutineScope = rememberCoroutineScope()
    var messageConfirmacion by remember { mutableStateOf("") }

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
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            // Champs de texte avec DataField
            DataField("Nombre", nombre) { viewModel.onCompletedFields(it, apellido, dni, email, telefono, genero, fechaNacimiento, contraseña) }
            DataField("Apellido", apellido) { viewModel.onCompletedFields(nombre, it, dni, email, telefono, genero, fechaNacimiento, contraseña) }
            DataField("DNI", dni) { viewModel.onCompletedFields(nombre, apellido, it, email, telefono, genero, fechaNacimiento, contraseña) }
            DataField("Email", email) { viewModel.onCompletedFields(nombre, apellido, dni, it, telefono, genero, fechaNacimiento, contraseña) }
            DataField("Teléfono", telefono) { viewModel.onCompletedFields(nombre, apellido, dni, email, it, genero, fechaNacimiento, contraseña) }

            // Sélection du genre
            SelectGender(
                selectedGender = selectedGender,
                onGenderChange = { newGender ->
                    viewModel.onCompletedFields(nombre, apellido, dni, email, telefono, newGender, fechaNacimiento, contraseña)
                    selectedGender = newGender
                }
            )

            // Sélection de la date de naissance
            Date(
                selectedDate = fechaNacimiento,
                onDateChange = { newDate ->
                    viewModel.onCompletedFields(nombre, apellido, dni, email, telefono, genero, newDate, contraseña)
                }
            )

            // Champ du mot de passe
            DataField(
                label = "Contraseña",
                value = contraseña,
                onValueChange = { viewModel.onCompletedFields(nombre, apellido, dni, email, telefono, genero, fechaNacimiento, it) },
                isPassword = true
            )

            // Bouton d'inscription
            Button(
                onClick = {
                    coroutineScope.launch {
                        val result = authViewModel.createUserWithEmailAndPassword(email, contraseña)
                        val user = result?.user

                        if (user != null) {
                            val usuario = Usuario(
                                nombre = nombre,
                                apellido = apellido,
                                email = email,
                                dni = dni,
                                fechaNacimiento = fechaNacimiento,
                                telefono = telefono,
                                genero = selectedGender
                            )

                            db.collection(nameCollection)
                                .document(user.uid)
                                .set(usuario)
                                .addOnSuccessListener {
                                    messageConfirmacion = "Datos guardados correctamente"
                                    viewModel.resetFields()
                                    navController.navigate("Home") {
                                        popUpTo("SignUp") { inclusive = true }
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    messageConfirmacion = "No se ha guardado correctamente: ${exception.message}"
                                }
                        } else {
                            messageConfirmacion = "Error al crear cuenta"
                        }
                    }
                },
                enabled = isButtonEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }

            Spacer(modifier = Modifier.size(5.dp))
            Text(text = messageConfirmacion)
        }
    }
}

// ✅ Composant DataField (Réutilisable)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataField(label: String, value: String, onValueChange: (String) -> Unit, isPassword: Boolean = false) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        enabled = true,
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.surface,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}


