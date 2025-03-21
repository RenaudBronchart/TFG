package com.example.tfg.screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.BookingPadelViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tfg.components.BottomBarComponent
import com.example.tfg.components.CardCourt
import com.example.tfg.viewmodel.CourtPadelViewModel
import java.time.LocalDate
import com.example.tfg.components.DayCard
import com.example.tfg.components.TopBarComponent
import com.example.tfg.components.getWeekDays
import com.example.tfg.viewmodel.CartShoppingViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingPadelScreen(
    navHostController: NavHostController, authViewModel: AuthViewModel, bookingPadelViewModel: BookingPadelViewModel, courtPadelViewModel: CourtPadelViewModel, cartShoppingViewModel: CartShoppingViewModel) {
    val weekDays = getWeekDays(LocalDate.now())
    val selectedDate = remember { mutableStateOf(LocalDate.now()) } //
    val courts = courtPadelViewModel.courtsPadel.collectAsState().value
    val cartItems by cartShoppingViewModel.CartShopping.collectAsState()

    // cargemos con la funcion // Lanuched permite que estas funciones se ejecutan directamente
    //
    LaunchedEffect(Unit) {
        courtPadelViewModel.getCourtsPadelFromFirestore()
        bookingPadelViewModel.loadBookings()
    }

    Scaffold(
        topBar = { TopBarComponent("Reservar pista", navHostController) },
        bottomBar = { BottomBarComponent(navHostController, cartItems) }
    ) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 8.dp)
            ) {
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    // dentro de LazyRow -> items
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
                // dentro de LazyColumn-> items
                items(courts) { court ->
                    CardCourt(
                        courtId = court.id,
                        date = selectedDate.value.toString(),
                        courtName = court.name,
                        bookingPadelViewModel = bookingPadelViewModel
                    ) { timeSlot ->
                        navHostController.navigate(
                            "checkoutScreen/${court.id}/${court.name}/${selectedDate.value}/$timeSlot"
                        )
                    }
                }
            }
        }
    }
}

