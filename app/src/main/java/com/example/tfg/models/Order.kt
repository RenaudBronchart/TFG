package com.example.tfg.models

import java.util.UUID

data class Order(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "", //
    val products: List<Product> = emptyList(),
    val totalAmount: Double = 0.0, // precio total
    val createdAt: String= "",
)