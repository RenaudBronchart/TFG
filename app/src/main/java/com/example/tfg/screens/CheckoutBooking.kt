package com.example.tfg.screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.tfg.components.CardCheckout
import com.example.tfg.components.TopBarComponent
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.BookingPadelViewModel



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CheckoutBookingScreen(navHostController: NavHostController, authViewModel: AuthViewModel, bookingPadelViewModel: BookingPadelViewModel,
                          courtId: String, courtName: String, date: String, timeSlot: String){

    val snackbarHostState = remember { SnackbarHostState() }
    val message by bookingPadelViewModel.messageConfirmation.collectAsState()

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message) //
            bookingPadelViewModel.setMessageConfirmation("") //
            navHostController.popBackStack() //
        }
    }
    Scaffold(
        topBar = { TopBarComponent("Confirmacion de reserva", navHostController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CardCheckout( courtName, date, timeSlot,
                onReserveClick = {
                    bookingPadelViewModel.bookCourt(authViewModel, navHostController, courtId, date,
                        startTime = timeSlot.split(" - ")[0], // 🔹 hora inicio
                        endTime = timeSlot.split(" - ")[1]   // 🔹 hora para el fin
                    ) {  confirmationMessage -> bookingPadelViewModel.setMessageConfirmation(confirmationMessage)
                    }
                }
            )
        }
    }
}



