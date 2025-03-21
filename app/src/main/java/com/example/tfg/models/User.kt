package com.example.tfg.models

import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(), // para generar un id unico
    val name: String = "",
    val firstname: String ="",
    val dni: String = "",
    val email: String =  "",
    val phone: String = "",
    val gender:String ="",
    val birthday: String = "",
    val role: String = "",
    )