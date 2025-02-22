package com.example.tfg.models

import java.util.UUID

data class CourtPadel(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val disponible: Boolean // Si le court est disponible pour la réservation
)