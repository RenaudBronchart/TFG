package com.example.tfg.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tfg.screens.AddProduct
import com.example.tfg.screens.Home
import com.example.tfg.screens.Login
import com.example.tfg.screens.SignUp
import com.example.tfg.screens.Profile
import com.example.tfg.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(auth: FirebaseAuth) {

    val navigationController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController =  navigationController, // recordar la navigation
        startDestination = AppScreens.Login.ruta // indicar la ruta donde empezamos la app
    ) {
        composable(AppScreens.Login.ruta) { Login(navigationController, auth)}
        composable(AppScreens.Home.ruta) { Home(navigationController, auth)}
        composable(AppScreens.SignUp.ruta) { SignUp(navigationController,auth,viewModel())}
        composable(AppScreens.Profile.ruta) { Profile(navigationController, authViewModel) }
        composable(AppScreens.AddProduct.ruta) { AddProduct(navigationController, auth,viewModel())}
    }
}