package com.example.tfg.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.UserViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.tfg.components.TopBarComponent
import com.example.tfg.models.User


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminListUsers(navHostController: NavHostController, authViewModel: AuthViewModel, userViewModel: UserViewModel) {

val userData by userViewModel.usuarios.collectAsState()

    Scaffold(
        topBar = { TopBarComponent("Datos", navHostController) },

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
            // mostra lista de usuaruios
            LazyColumn {
                items(userData) { usuario ->
                    UserCard(usuario = usuario) { userId ->
                        navHostController.navigate("editUser/$userId")
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
            Text(text = "Nombre: ${usuario.name}", fontWeight = FontWeight.Bold)
            Text(text = "Id: ${usuario.id}")
            Text(text = "Apellido: ${usuario.firstname}")
            Text(text = "Email: ${usuario.email}")
            Text(text = "Dni: ${usuario.dni}")
            Text(text = "Telefono: ${usuario.phone}")
            Text(text = "Fecha nacimiento: ${usuario.birthday}")
            Text(text = "Genero: ${usuario.gender}")
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




