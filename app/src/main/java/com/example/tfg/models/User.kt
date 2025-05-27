package com.example.tfg.models

import java.util.UUID

//  Modelo de datos que representa un usuario de la aplicación.
data class User(
    val id: String = "",
    val name: String = "",
    val firstname: String ="",
    val dni: String = "",
    val email: String =  "",
    val phone: String = "",
    val gender:String ="",
    val birthday: String = "",
    val role: String = "",
    )