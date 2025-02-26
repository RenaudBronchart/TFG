package com.example.tfg.screens


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.BookingPadelViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tfg.components.CardCourt
import com.example.tfg.viewmodel.CourtPadelViewModel
import java.time.LocalDate
import com.example.tfg.components.DayCard
import com.example.tfg.components.getWeekDays
import com.example.tfg.navigation.AppScreens


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingPadelScreen(
    navController: NavHostController, authViewModel: AuthViewModel, bookingPadelViewModel: BookingPadelViewModel, courtPadelViewModel: CourtPadelViewModel
) {
    val weekDays = getWeekDays(LocalDate.now())
    val selectedDate = remember { mutableStateOf(LocalDate.now()) } //
    val courts = courtPadelViewModel.courtsPadel.collectAsState().value

    LaunchedEffect(Unit) {
        courtPadelViewModel.getCourtsPadelFromFirestore()
    }

    LaunchedEffect(Unit) {
        bookingPadelViewModel.loadBookings()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                title = { Text("Reservar pista") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("Home") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {
            // LazyRow pour afficher les jours de la semaine
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 8.dp)
            ) {
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(weekDays) { day ->
                        DayCard(
                            day = day,

                            isSelected = day == selectedDate.value,
                            onClick = { selectedDate.value = day }
                        )
                    }
                }
            }

            // LazyColumn pour afficher les courts
            LazyColumn {
                items(courts) { court ->
                    CardCourt(
                        courtId = court.id,
                        date = selectedDate.value.toString(),
                        courtName = court.nombre,
                        bookingPadelViewModel = bookingPadelViewModel
                    ) { timeSlot ->
                        navController.navigate(
                            AppScreens.CheckoutBooking.createRoute(
                                court.id, court.nombre, selectedDate.value.toString(), timeSlot
                            )
                        )
                    }
                }
            }
        }
    }
}

