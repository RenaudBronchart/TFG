package com.example.tfg.screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.tfg.components.CardCheckout
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.BookingPadelViewModel



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutBookingScreen(navController: NavHostController, authViewModel: AuthViewModel, bookingPadelViewModel: BookingPadelViewModel, courtId: String,
                          courtName: String, date: String, timeSlot: String) {

    val snackbarHostState = remember { SnackbarHostState() }
    val message by bookingPadelViewModel.messageConfirmation.collectAsState()

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message) //
            bookingPadelViewModel.setMessageConfirmation("") //
            navController.popBackStack() //
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                title = { Text("Confirmation de réservation") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("BookingPadelScreen") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color.White
                        )
                    }
                }
            )
        },
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
            CardCheckout(
                courtName = courtName,
                date = date,
                timeSlot = timeSlot,
                onReserveClick = {
                    bookingPadelViewModel.bookCourt(
                        authViewModel = authViewModel,
                        navController = navController,
                        courtId = courtId,
                        date = date,
                        startTime = timeSlot.split(" - ")[0], // 🔹 hora inicio
                        endTime = timeSlot.split(" - ")[1]   // 🔹 hora para el fin
                    ) {  confirmationMessage ->
                        bookingPadelViewModel.setMessageConfirmation(confirmationMessage)

                    }
                }
            )
        }
    }
}



