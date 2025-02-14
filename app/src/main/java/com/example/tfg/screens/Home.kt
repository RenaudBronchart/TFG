package com.example.tfg.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tfg.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Home(navController: NavHostController, authViewModel : AuthViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center, // Centrer verticalement
        modifier = Modifier
            .fillMaxSize() // Remplit l'écran
            .padding(16.dp) // Ajoute un peu d'espace autour
    ) {
        Text(
            text = "Hello",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp) // Espacement sous le texte
        )

        Button(
            onClick = { navController.navigate("AddProduct") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Add Product",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp)) // Espacement entre les boutons

        Button(
            onClick = { navController.navigate("Profile") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Profile",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )

        }
        Button(
            onClick = { navController.navigate("EshopScreen") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Profile",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )

        }

        Button(
            onClick = { navController.navigate("ListUsers") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "ListUsers",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )

        }
    }
}


