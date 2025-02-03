package com.example.tfg.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tfg.screens.Login

// para que ponemos Composable
@Composable
fun AppNavigation() {

    val navigationController = rememberNavController()
    NavHost(
        navController =  navigationController, // recordar la navigation
        startDestination = AppScreens.Login.ruta // indicar la ruta donde empezamos la app
    ) {

        composable(AppScreens.Login.ruta) { Login(navigationController) }



    }
}