package com.example.tfg.navigation

// Sealed class porque va a contener una serie de objetos relacionados (pantallas de app)
sealed class AppScreens(val ruta: String) {
    object Login: AppScreens("login")
    object SignUp: AppScreens("signUp")
    object Profile: AppScreens("profile")
    object Home: AppScreens("home")
    object AddProduct: AppScreens("addProduct")
    object EshopScreen: AppScreens("eshopScreen")
    object ListUsers: AppScreens("listUsers")
    object MyData: AppScreens("myData")
}