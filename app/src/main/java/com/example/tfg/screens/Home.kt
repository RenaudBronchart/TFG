package com.example.tfg.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Home(navController: NavHostController,auth: FirebaseAuth) {
   Column (
       horizontalAlignment = Alignment.CenterHorizontally,
       modifier = Modifier
              .fillMaxWidth()
              .height(500.dp)

   ){  }
    Text(
        text = "hello"
    )
    Button(
        onClick = { navController.navigate("AddProduct") },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp) // Bordes redondeados
    ) {
        Text(
            text = "Add Product",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp
        )
    }
    Button(
        onClick = { navController.navigate("Profile") },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp) // Bordes redondeados
    ) {
        Text(
            text = "Profile",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp
        )
    }
}


