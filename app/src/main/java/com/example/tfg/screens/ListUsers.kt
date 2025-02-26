package com.example.tfg.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.UserViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.tfg.models.User


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListUsers(navController: NavHostController, authViewModel: AuthViewModel, userViewModel: UserViewModel) {

val userData by userViewModel.usuarios.collectAsState()

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
                            contentDescription = "volver",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Lista de Usuarios",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Affichage des utilisateurs dans une liste
            LazyColumn {
                items(userData) { usuario ->
                    UserCard(usuario = usuario) { userId ->
                        navController.navigate("editUser/$userId")
                    }
                }
            }
        }
        }
    }

@Composable
fun UserCard(usuario: User, onEditClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Nombre: ${usuario.nombre}", fontWeight = FontWeight.Bold)
            Text(text = "Id: ${usuario.id}")
            Text(text = "Apellido: ${usuario.apellido}")
            Text(text = "Email: ${usuario.email}")
            Text(text = "Dni: ${usuario.dni}")
            Text(text = "Telefono: ${usuario.telefono}")
            Text(text = "Fecha nacimiento: ${usuario.fechaNacimiento}")
            Text(text = "Genero: ${usuario.genero}")
            Text(text = "Role: ${usuario.role}")

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onEditClick(usuario.id) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Modifier")
            }


        }

    }

}




