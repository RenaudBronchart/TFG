package com.example.tfg.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier


@Composable
fun CardCourt() {
    val backgroundColor = MaterialTheme.colorScheme.primary
    val timeSlotsLine1 = listOf("9:00", "11:00", "13:00", "15:00")  // Première ligne
    val timeSlotsLine2 = listOf("17:00", "19:00", "21:00")  // Deuxième ligne


    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(220.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Pista 1",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            // primera linea de row
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp), // espacio entre elementos
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(timeSlotsLine1) { timeSlot ->
                    TimeCourtCard(timeSlot)
                }
            }

            // Seconda primera linea de row
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp), //
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(timeSlotsLine2) { timeSlot ->
                    TimeCourtCard(timeSlot)
                }
            }
        }
    }
}
@Composable
fun TimeCourtCard(timeSlot: String) {
    // Card para el horarios
    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(70.dp)
            .height(40.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)),
        onClick = {
            // para acceder a la pagina de reserva

        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = timeSlot,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
