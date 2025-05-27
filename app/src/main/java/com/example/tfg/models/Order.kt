package com.example.tfg.models

import java.util.UUID

// Modelo de datos que representa un order de la aplicación.
data class Order(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "", //
    val products: List<Product> = emptyList(),
    val totalAmount: Double = 0.0, // precio total
    val createdAt: String= "",
)