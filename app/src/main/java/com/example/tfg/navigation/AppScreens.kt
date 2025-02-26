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
    object EditUser: AppScreens("editUser")
    object EditProduct: AppScreens("editProduct")
    object AdminPanel: AppScreens("adminPanel")
    object BookingPadelScreen: AppScreens("bookingPadelScreen")
    object MyBookings: AppScreens("myBookings")
    object CheckoutBooking : AppScreens("checkoutScreen/{courtId}/{courtName}/{date}/{timeSlot}") {
        fun createRoute(courtId: String, courtName: String, date: String, timeSlot: String) =
            "checkoutScreen/$courtId/$courtName/$date/$timeSlot"
    }
}