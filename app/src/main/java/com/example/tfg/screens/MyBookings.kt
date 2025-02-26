package com.example.tfg.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.components.CardCheckout
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.BookingPadelViewModel
import com.example.tfg.viewmodel.CourtPadelViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookings(navController: NavHostController, authViewModel : AuthViewModel, bookingPadelViewModel: BookingPadelViewModel,courtPadelViewModel: CourtPadelViewModel)  {
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
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                title = { Text("Mis reservas") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("Profile") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                }
            )
        }
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
                    val courtName = courts.find { it.id == booking.courtId }?.nombre ?: "Error Pista"

                    CardCheckout(
                        courtName = courtName,
                        date = booking.date,
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