package com.example.tfg.models

import java.util.UUID
// Modelo de datos que representa un booking padel de la aplicación.
data class BookingPadel(
    val id: String = UUID.randomUUID().toString(), // ID único autogenerado
    val userId: String = "",  // id del usuario
    val courtId: String = "",  // id de la pista
    val date: String = "",
    val startTime: String = "",  // Hora de inicio y queremos que empieza a la 9.00
    val endTime: String = "",  // Hora de fin, de momento queremos 23.00
    val status: Boolean = true  // `true` = disponible, `false` = reservado
)