package com.example.tfg.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.components.Date
import com.example.tfg.components.GenderSelection
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

//import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(navController: NavHostController,auth: FirebaseAuth) {
    var gender by remember { mutableStateOf("Hombre") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    //val db = Firebase.firestore.getInstance()
    //var name_collection = "usuarios"

    //var


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
                            contentDescription = "back"
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
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            TextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            TextField(
                value = dni,
                onValueChange = { dni = it },
                label = { Text("DNI/NIE") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            TextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("telefono") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            GenderSelection(
                selectedGender = gender,
                onGenderChange = { newGender -> gender = newGender }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Date()

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Logique d'inscription (par exemple, utiliser Firebase Auth pour créer un utilisateur)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear cuenta")
            }
        }
    }
}


