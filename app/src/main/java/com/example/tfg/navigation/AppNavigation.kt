package com.example.tfg.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tfg.screens.Home
import com.example.tfg.screens.Login
import com.example.tfg.screens.SignUp
import com.example.tfg.screens.Profile
import com.google.firebase.auth.FirebaseAuth

// para que ponemos Composable
@Composable
fun AppNavigation(auth: FirebaseAuth) {

    val navigationController = rememberNavController()
    NavHost(
        navController =  navigationController, // recordar la navigation
        startDestination = AppScreens.Login.ruta // indicar la ruta donde empezamos la app
    ) {

        composable(AppScreens.Login.ruta) { Login(navigationController, auth)}
        composable(AppScreens.Home.ruta) { Home(navigationController, auth)}
        composable(AppScreens.SignUp.ruta) { SignUp(navigationController,auth) }
        composable(AppScreens.Profile.ruta) { Profile(navigationController, auth) }

    }
}