package com.example.tfg.navigation

// Sealed class porque va a contener una serie de objetos relacionados (pantallas de app)
// Una sealed class permite definir rutas con parámetros dinámicos (EditUser/{userId})
sealed class AppScreens(val ruta: String) {
    object Login: AppScreens("login")
    object SignUp: AppScreens("signUp")
    object Profile: AppScreens("profile")
    object Home: AppScreens("home")
    object AdminAddProduct: AppScreens("adminAddProduct")
    object EshopScreen: AppScreens("eshopScreen")
    object AdminListUsers: AppScreens("adminListUsers")
    object EditUserProfil: AppScreens("editUser/{userId}")
    object EditUserMyProfil: AppScreens("editMyProfile/{userId}")
    object AdminEditProduct: AppScreens("adminEditProduct/{productId}")
    object AdminPanel: AppScreens("adminPanel")
    object BookingPadelScreen: AppScreens("bookingPadelScreen")
    object ProfileMyBookings: AppScreens("profileMyBookings")
    object CheckoutBooking: AppScreens("checkoutScreen/{courtId}/{courtName}/{date}/{timeSlot}")
    object CheckoutShopping: AppScreens("checkoutShopping")
    object ProfileMyOrders : AppScreens("profileMyOrders")
    object OrderDoneScreen : AppScreens("orderDoneScreen")
}