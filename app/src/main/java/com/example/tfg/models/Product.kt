package com.example.tfg.models

import java.util.UUID

data class Product(
    val id: String = UUID.randomUUID().toString(), // para generar un id unico
    val nombre:String = "",
    val precio:Double = 0.0,
    val descripcion:String = "",
    val categoria:String = "",
    val imagen:String = "",
    val stock:Int = 0,
    val quantity:Int = 1,
    val marca:String = "",
    )