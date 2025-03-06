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
    object EditUser: AppScreens("editUser/{userId}")
    object EditProduct: AppScreens("editProduct/{productId}")
    object AdminPanel: AppScreens("adminPanel")
    object BookingPadelScreen: AppScreens("bookingPadelScreen")
    object MyBookings: AppScreens("myBookings")
    object CheckoutBooking: AppScreens("checkoutScreen/{courtId}/{courtName}/{date}/{timeSlot}")
    object CheckoutShopping: AppScreens("checkoutShopping")
    object MyOrders : AppScreens("MyOrders")
}