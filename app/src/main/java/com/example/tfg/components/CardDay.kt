package com.example.tfg.components


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayCard(day: LocalDate, isToday: Boolean) {
    // Utilisation de la couleur primaire du thème pour le jour actuel
    val backgroundColor = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (isToday) Color.White else MaterialTheme.colorScheme.onSurface

    // para poder definir si queremos mas grande o pequenas las card
    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(75.dp)
            .height(75.dp)
            .clickable {
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = MaterialTheme.shapes.medium, // Forme arrondie pour les coins
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)) // Bordure discrète
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center // para centrar el contenido
        ) {
            // Para mostrar los datos dia mes
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // el dia con la modificacion ingles -> esp
                Text(
                    day.format(DateTimeFormatter.ofPattern("EEE")).let {
                        it.replace("Mon", "Lun")
                            .replace("Tue", "Mar")
                            .replace("Wed", "Mié")
                            .replace("Thu", "Jue")
                            .replace("Fri", "Vie")
                            .replace("Sat", "Sab")
                            .replace("Sun", "Dom")
                    },
                    style = MaterialTheme.typography.bodySmall.copy(color = textColor)
                )
                // "el dia en numero
                Text(
                    day.format(DateTimeFormatter.ofPattern("dd")),
                    style = MaterialTheme.typography.bodySmall.copy(color = textColor)
                )
                // el mes con el cambio de nombre ingles -> es
                Text(
                    day.format(DateTimeFormatter.ofPattern("MMM")).let {
                        it.replace("Jan", "Ene")
                            .replace("Feb", "Feb")
                            .replace("Mar", "Mar")
                            .replace("Apr", "Abr")
                            .replace("May", "May")
                            .replace("Jun", "Jun")
                            .replace("Jul", "Jul")
                            .replace("Aug", "Ago")
                            .replace("Sep", "Sep")
                            .replace("Oct", "Oct")
                            .replace("Nov", "Nov")
                            .replace("Dec", "Dic")
                    },
                    style = MaterialTheme.typography.bodySmall.copy(color = textColor)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getWeekDays(currentDay: LocalDate): List<LocalDate> {
    val days = mutableListOf<LocalDate>()
    for (i in 0..6) {
        days.add(currentDay.plusDays(i.toLong()))
    }
    return days
}