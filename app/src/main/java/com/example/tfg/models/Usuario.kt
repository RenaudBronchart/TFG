package com.example.tfg.models

import java.util.UUID

data class Usuario(
    val id: String = UUID.randomUUID().toString(), // para generar un id unico
    val nombre: String = "",
    val apellido: String ="",
    val nie: String = "",
    val fechaNacimiento: String = "",
    val sexo:String ="",
    val email: String =  "",
    val telefono: String = "",

    )