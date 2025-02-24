package com.example.tfg.components

import androidx.benchmark.perfetto.ExperimentalPerfettoTraceProcessorApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.tfg.viewmodel.BookingPadelViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow


@Composable
fun CardCourt(
    courtId: String,
    date: String,
    courtName: String,
    bookingPadelViewModel: BookingPadelViewModel,
    onTimeSlotClick: (String) -> Unit
) {
    val timeSlots = listOf("9:00", "11:00", "13:00", "15:00", "17:00", "19:00", "21:00", "23:00")
        .windowed(2, 1) { "${it[0]} - ${it[1]}" } // Génère des créneaux horaires

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Titre avec une icône
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.SportsTennis,
                    contentDescription = "Court de padel",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = courtName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }

            // Disposition des créneaux horaires sous forme de grille
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp
            ) {
                timeSlots.forEach { timeSlot ->
                    val isAvailable = bookingPadelViewModel.isCourtAvailable(courtId, date, timeSlot)
                    TimeCourtCard(timeSlot, isAvailable) {
                        onTimeSlotClick(timeSlot)
                    }
                }
            }
        }
    }
}

@Composable
fun TimeCourtCard(timeSlot: String, isAvailable: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(110.dp)  // Largeur ajustée pour un affichage propre
            .height(50.dp)
            .clickable(enabled = isAvailable, onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isAvailable) MaterialTheme.colorScheme.primary else Color.Gray
        ),
        shape = RoundedCornerShape(12.dp), // Coins arrondis pour un design plus moderne
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = timeSlot.replace("-", " - "), // Ajout d'espaces autour du "-"
                color = if (isAvailable) Color.White else Color.LightGray,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

