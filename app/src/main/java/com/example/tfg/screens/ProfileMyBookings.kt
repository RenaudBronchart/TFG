package com.example.tfg.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.components.CardCheckout
import com.example.tfg.components.TopBarComponent
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.BookingPadelViewModel
import com.example.tfg.viewmodel.CourtPadelViewModel



@Composable
fun ProfileMyBookings(navHostController: NavHostController, authViewModel : AuthViewModel, bookingPadelViewModel: BookingPadelViewModel, courtPadelViewModel: CourtPadelViewModel)  {
    val currentUser by authViewModel.user.collectAsState()
    // bookingPadelViewModel.bookingsPadel = stateFlowList de BookingPadel
    // collectAstate permite leer y actualizar AllBookings cada vez que los datos cambian
    val allBookings by bookingPadelViewModel.bookingsPadel.collectAsState()
    val courts by courtPadelViewModel.courtsPadel.collectAsState()
    var displayedCount by remember { mutableStateOf(3) }

    LaunchedEffect(Unit) {
        bookingPadelViewModel.loadBookings()
        courtPadelViewModel.getCourtsPadelFromFirestore()
    }
    // it representa cada elemento de allbookings  con el userId de l'id que hizo la reserva
    //  UiD = user connectado . guardamos solo las reserva de it.userId = a UID actual

    val userBookings = allBookings.filter { it.userId == currentUser?.uid }

    Scaffold(
        topBar = { TopBarComponent("Mis reservas",navHostController) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                //take displayedcout, seleciona solo los elementos de displayedcount
                items(count = userBookings.take(displayedCount).size) { index ->
                    val booking = userBookings[index] // // Obtener la reserva en la posición `index`
                    val courtName = courts.find { it.id == booking.courtId }?.name ?: "Error Pista"

                    CardCheckout(
                        courtName = courtName,
                        displayDate = booking.date,
                        timeSlot = "${booking.startTime} - ${booking.endTime}",
                        showReserveButton = false,
                        onReserveClick = {  }
                    )
                }
            }
            if (displayedCount < userBookings.size) {
                Button(
                    onClick = { displayedCount += 3 }, // 🔹 Muestra 3 reservas más
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Cargar más")
             }   }
        }
    }
}