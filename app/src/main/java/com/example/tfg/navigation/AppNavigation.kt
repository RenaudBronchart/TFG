package com.example.tfg.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tfg.screens.AddProduct
import com.example.tfg.screens.AdminPage
import com.example.tfg.screens.BookingPadelScreen
import com.example.tfg.screens.CheckoutBookingScreen
import com.example.tfg.screens.CheckoutShopping
import com.example.tfg.screens.EditProduct
import com.example.tfg.screens.EshopScreen
import com.example.tfg.screens.Home
import com.example.tfg.screens.Login
import com.example.tfg.screens.SignUp
import com.example.tfg.screens.Profile
import com.example.tfg.screens.ListUsers
import com.example.tfg.screens.EditUser
import com.example.tfg.screens.MyBookings
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.EditProductViewModel
import com.example.tfg.viewmodel.EditUserViewModel
import com.example.tfg.viewmodel.ProductViewModel
import com.example.tfg.viewmodel.UserViewModel
import com.example.tfg.viewmodel.BookingPadelViewModel
import com.example.tfg.viewmodel.CartShoppingViewModel
import com.example.tfg.viewmodel.CourtPadelViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(authViewModel: AuthViewModel) {

    val navigationController = rememberNavController()
    val editUserViewModel: EditUserViewModel = viewModel() // Usamos viewModel() para crear el ViewModel necesario
    val userViewModel: UserViewModel = viewModel() // De nuevo, obtener usuarioViewModel si es necesario
    val productViewModel : ProductViewModel = viewModel()
    val editProductViewModel: EditProductViewModel = viewModel()
    val bookingPadelViewModel: BookingPadelViewModel = viewModel()
    val courtPadelViewModel: CourtPadelViewModel = viewModel()
    val cartShoppingViewModel : CartShoppingViewModel = viewModel()

    NavHost(
        navController =  navigationController, // recordar la navigation
        startDestination = AppScreens.Home.ruta // indicar la ruta donde empezamos la app
    ) {
        composable(AppScreens.Login.ruta) { Login(navigationController, authViewModel)}
        composable(AppScreens.Home.ruta) { Home(navigationController, authViewModel,userViewModel,cartShoppingViewModel)}
        composable(AppScreens.SignUp.ruta) { SignUp(navigationController,authViewModel,userViewModel)}
        composable(AppScreens.Profile.ruta) { Profile(navigationController, authViewModel, userViewModel, cartShoppingViewModel) }
        composable(AppScreens.EshopScreen.ruta) { EshopScreen(navigationController, authViewModel,productViewModel, cartShoppingViewModel)}
        composable(AppScreens.AdminPanel.ruta) { AdminPage(navigationController, authViewModel,userViewModel)}
        composable(AppScreens.AddProduct.ruta) { AddProduct(navigationController, authViewModel, productViewModel)}
        composable(AppScreens.BookingPadelScreen.ruta) { BookingPadelScreen(navigationController, authViewModel,bookingPadelViewModel, courtPadelViewModel,cartShoppingViewModel)}
        composable(AppScreens.ListUsers.ruta) { ListUsers(navigationController, authViewModel, userViewModel)}
        composable(AppScreens.MyBookings.ruta) { MyBookings(navigationController, authViewModel, bookingPadelViewModel,courtPadelViewModel) }
        composable(AppScreens.CheckoutShopping.ruta) { CheckoutShopping(navigationController,cartShoppingViewModel) }
        composable(AppScreens.EditUser.ruta) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            EditUser(navigationController, authViewModel, editUserViewModel, userId)
        }

        composable(AppScreens.EditProduct.ruta)  { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            EditProduct(navigationController, authViewModel, editProductViewModel, productId)
        }

        composable(AppScreens.CheckoutBooking.ruta) { backStackEntry ->
            CheckoutBookingScreen(navigationController, authViewModel, bookingPadelViewModel,
                courtId = backStackEntry.arguments?.getString("courtId") ?: "",
                courtName = backStackEntry.arguments?.getString("courtName") ?: "",
                date = backStackEntry.arguments?.getString("date") ?: "",
                timeSlot = backStackEntry.arguments?.getString("timeSlot") ?: ""
            )
        }
    }
}