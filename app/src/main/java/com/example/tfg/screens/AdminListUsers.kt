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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.AddUserViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.tfg.components.TopBarComponent
import com.example.tfg.models.User
import com.example.tfg.viewmodel.UserViewModel


@Composable
fun AdminListUsers(navHostController: NavHostController, authViewModel: AuthViewModel, userViewModel: UserViewModel) {
    val userData by userViewModel.users.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.getUsersFromFirestore()
    }

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
                items(userData) { user ->
                    UserCard(user = user) { userId ->
                        navHostController.navigate("editUser/$userId")
                    }
                }
            }
        }
        }
    }

@Composable
fun UserCard(user: User, onEditClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Nombre: ${user.name}", fontWeight = FontWeight.Bold)
            Text(text = "Id: ${user.id}")
            Text(text = "Apellido: ${user.firstname}")
            Text(text = "Email: ${user.email}")
            Text(text = "Dni: ${user.dni}")
            Text(text = "Telefono: ${user.phone}")
            Text(text = "Fecha nacimiento: ${user.birthday}")
            Text(text = "Genero: ${user.gender}")
            Text(text = "Role: ${user.role}")

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onEditClick(user.id) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Modificar")
            }


        }

    }

}




