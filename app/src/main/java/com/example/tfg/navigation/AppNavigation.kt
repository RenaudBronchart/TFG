package com.example.tfg.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tfg.screens.AdminAddProduct
import com.example.tfg.screens.AdminEditProduct
import com.example.tfg.screens.AdminListUsers
import com.example.tfg.screens.AdminPage
import com.example.tfg.screens.CheckoutShopping
import com.example.tfg.screens.EditUserMyProfil
import com.example.tfg.screens.EditUserProfil
import com.example.tfg.screens.EshopScreen
import com.example.tfg.screens.Home
import com.example.tfg.screens.Login
import com.example.tfg.screens.OrderDoneScreen
import com.example.tfg.screens.Profile
import com.example.tfg.screens.ProfileMyOrders
import com.example.tfg.screens.SignUp
import com.example.tfg.viewmodel.AddProductViewModel
import com.example.tfg.viewmodel.AddUserViewModel
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.BookingPadelViewModel
import com.example.tfg.viewmodel.CartShoppingViewModel
import com.example.tfg.viewmodel.CourtPadelViewModel
import com.example.tfg.viewmodel.DeleteProductViewModel
import com.example.tfg.viewmodel.EditProductViewModel
import com.example.tfg.viewmodel.EditUserViewModel
import com.example.tfg.viewmodel.OrderViewModel
import com.example.tfg.viewmodel.ProductViewModel
import com.example.tfg.viewmodel.UserViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {

    // rememberNavController() crea y recuerda una instancia de NavController,
    // que permite manejar la navegación entre pantallas en Jetpack Compose.
    val navigationController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val addUserViewModel: AddUserViewModel = viewModel()
    val addProductViewModel: AddProductViewModel = viewModel()
    val editUserViewModel: EditUserViewModel = viewModel() // Usamos viewModel() para crear el ViewModel necesario
    val userViewModel: UserViewModel = viewModel() // De nuevo, obtener usuarioViewModel si es necesario
    val deleteProductViewModel: DeleteProductViewModel = viewModel()
    val productViewModel : ProductViewModel = viewModel()
    val editProductViewModel: EditProductViewModel = viewModel()
    val bookingPadelViewModel: BookingPadelViewModel = viewModel()
    val courtPadelViewModel: CourtPadelViewModel = viewModel()
    val cartShoppingViewModel : CartShoppingViewModel = viewModel()
    val orderViewModel : OrderViewModel = viewModel()


    NavHost(
        navController =  navigationController,
        startDestination = AppScreens.Home.ruta // indicar la ruta donde empezamos la app
    ) {
        composable(AppScreens.Login.ruta) { Login(navigationController, authViewModel)}
        composable(AppScreens.Home.ruta) { Home(navigationController,authViewModel,userViewModel,cartShoppingViewModel) }
        composable(AppScreens.SignUp.ruta) { SignUp(navigationController,authViewModel,addUserViewModel)}
        composable(AppScreens.Profile.ruta) { Profile(navigationController, authViewModel, userViewModel, cartShoppingViewModel) }
        composable(AppScreens.EshopScreen.ruta) { EshopScreen(navigationController, authViewModel,productViewModel, cartShoppingViewModel,deleteProductViewModel)}
        composable(AppScreens.AdminPanel.ruta) { AdminPage(navigationController, authViewModel,userViewModel,cartShoppingViewModel)}
        composable(AppScreens.AdminAddProduct.ruta) { AdminAddProduct(navigationController, authViewModel, productViewModel ,addProductViewModel)}

        composable(AppScreens.AdminListUsers.ruta) { AdminListUsers(navigationController, authViewModel, userViewModel)}
        composable(AppScreens.CheckoutShopping.ruta) { CheckoutShopping(navigationController,authViewModel,cartShoppingViewModel) }

        /*composable(AppScreens.BookingPadelScreen.ruta) { BookingPadelScreen(navigationController, authViewModel,bookingPadelViewModel, courtPadelViewModel,cartShoppingViewModel)}
        composable(AppScreens.ProfileMyBookings.ruta) { ProfileMyBookings(navigationController, authViewModel, bookingPadelViewModel,courtPadelViewModel) }*/

        composable(AppScreens.ProfileMyOrders.ruta) { ProfileMyOrders (navigationController,authViewModel, orderViewModel )}
        composable(AppScreens.OrderDoneScreen.ruta) { OrderDoneScreen (navigationController,authViewModel, orderViewModel,cartShoppingViewModel )}
        // declaramos una ruta en la navigation con composable para llegar a la pagina de Edituser
        // backStackEntry va a permitir obtener los parametros de navagacion de backStackEntry
         //   ?: ""
        //→ Si userId es null, devuelve una cadena vacía "" en su lugar.
        composable(AppScreens.EditUserProfil.ruta) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            // Verifica que userId sea el correcto
            EditUserProfil(navigationController, authViewModel, editUserViewModel, userId)
        }
        composable(AppScreens.EditUserMyProfil.ruta) { backStackEntry ->
            //  el userId del backStackEntry (que es pasado por la nav)
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            EditUserMyProfil(navController = navigationController, authViewModel , editUserViewModel , userId )
        }
        composable(AppScreens.AdminEditProduct.ruta)  { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            AdminEditProduct(navigationController, authViewModel, editProductViewModel, productId)
        }

        /*composable(AppScreens.CheckoutBooking.ruta) { backStackEntry ->
            CheckoutBookingScreen(navigationController, authViewModel, bookingPadelViewModel,
                courtId = backStackEntry.arguments?.getString("courtId") ?: "",
                courtName = backStackEntry.arguments?.getString("courtName") ?: "",
                date = backStackEntry.arguments?.getString("date") ?: "",
                timeSlot = backStackEntry.arguments?.getString("timeSlot") ?: ""
            )
        }*/
    }
}