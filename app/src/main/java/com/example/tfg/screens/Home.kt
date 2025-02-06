package com.example.tfg.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Home(navController: NavHostController,auth: FirebaseAuth) {

    Text(
        text = "hello"
    )

}


