package com.example.tfg.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.components.Date
import com.example.tfg.components.SelectGender
import com.example.tfg.models.Usuario
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.UsuarioViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(navController: NavHostController, authViewModel : AuthViewModel, viewModel: UsuarioViewModel) {

    val db = FirebaseFirestore.getInstance()
    val name_collection = "usuarios"

    var selectedGender by remember { mutableStateOf("Selecciona un opción") }
    val nombre:String by viewModel.nombre.observeAsState("")
    val apellido:String by viewModel.apellido.observeAsState("")
    val dni:String by viewModel.dni.observeAsState("")
    val email:String by viewModel.email.observeAsState("")
    val telefono:String by viewModel.telefono.observeAsState("")
    val genero:String by viewModel.genero.observeAsState("")
    val fechaNacimiento:String by viewModel.fechaNacimiento.observeAsState("")
    val contraseña:String by viewModel.contraseña.observeAsState("")

    val coroutineScope = rememberCoroutineScope()

    var mensaje_confirmacion by remember { mutableStateOf("") }
    val isButtonEnable:Boolean by viewModel.isButtonEnable.observeAsState(initial = false)

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
                            contentDescription = "vovler",
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
                .padding(16.dp)
        ) {
            TextField(
                value = nombre,
                onValueChange = { viewModel.onCompletedFields(it, apellido, email, dni, telefono, genero, fechaNacimiento, contraseña)
                                },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            TextField(
                value = apellido,
                onValueChange = { viewModel.onCompletedFields(nombre, it, email, dni, telefono, genero, fechaNacimiento, contraseña)
                                },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            TextField(
                value = dni,
                onValueChange = { viewModel.onCompletedFields(nombre, apellido, it, email, telefono, genero, fechaNacimiento, contraseña)
                                },
                label = { Text("DNI/NIE") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            TextField(
                value = email,
                onValueChange = { viewModel.onCompletedFields(nombre, apellido, dni, it, telefono, genero, fechaNacimiento, contraseña)
                                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            TextField(
                value = telefono,
                onValueChange = { viewModel.onCompletedFields(nombre, apellido, dni, email, it, genero, fechaNacimiento, contraseña)
                                },
                label = { Text("Telefono") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            SelectGender(
                selectedGender = selectedGender,
                onGenderChange = { newGender ->
                    viewModel.onCompletedFields(nombre, apellido, dni, email, telefono, newGender, fechaNacimiento, contraseña)
                    selectedGender = newGender
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Date(
                selectedDate = fechaNacimiento,
                onDateChange = { newDate ->
                    viewModel.onCompletedFields(nombre, apellido, dni, email, telefono, genero, newDate, contraseña)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = contraseña,
                onValueChange = { viewModel.onCompletedFields(nombre, apellido, dni, email, telefono, genero, fechaNacimiento, it)
                                },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                visualTransformation = PasswordVisualTransformation()
            )

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

                            db.collection(name_collection)
                                .document(user.uid)
                                .set(usuario)
                                .addOnSuccessListener {
                                    mensaje_confirmacion = "Datos guardados correctamente"
                                    viewModel.resetFields()
                                }
                                .addOnFailureListener { exception ->
                                    mensaje_confirmacion = "No se ha guardado correctamente: ${exception.message}"
                                }
                        } else {
                            mensaje_confirmacion = "Error al crear cuenta"
                        }
                    }
                },
                enabled = isButtonEnable,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }

            Spacer(modifier = Modifier.size(5.dp))
            Text(text = mensaje_confirmacion)
        }
    }
}


