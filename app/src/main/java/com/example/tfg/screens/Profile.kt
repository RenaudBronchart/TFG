package com.example.tfg.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(navController: NavHostController, authViewModel: AuthViewModel, viewModel: UsuarioViewModel) {

    authViewModel.user.collectAsState().value?.let { firebaseUser ->
        Log.d("Profile", "Utilisateur connecté: ${firebaseUser?.uid ?: "Aucun utilisateur"}")
    }
    val usuario by viewModel.usuarios.collectAsState()

    Scaffold(

        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("Login") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back",
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
            CardItem(
                icon = Icons.Default.Person,
                text = "Mis datos",
                onClick = { navController.navigate("MyData") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CardItem(
                icon = Icons.Default.Book,
                text = "Mis reservas",
                onClick = { navController.navigate("AddProduct") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CardItem(
                icon = Icons.Default.ShoppingCart,
                text = "Mis compras",
                onClick = { navController.navigate("AddProduct") }
            )

            Spacer(modifier = Modifier.height(36.dp))

            CardItem(
                icon = Icons.Default.ExitToApp,
                text = "Desconectar",
                onClick = {
                    authViewModel.signOut()
                    navController.navigate("Login") {
                        popUpTo("Login") { inclusive = true }
                    }
                }
            )

            }



    }




}

@Composable
fun CardItem(
    icon: ImageVector,
    text:String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(30.dp),
                tint= MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(10.dp)) // Espace entre l'icône et le texte
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    ),

                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}