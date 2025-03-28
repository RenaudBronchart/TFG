package com.example.tfg.models

import java.util.UUID

data class Product(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val category: String = "",
    val image: String = "",
    val stock: Int = 0,
    val quantity: Int = 1,
    val brand: String = ""
)