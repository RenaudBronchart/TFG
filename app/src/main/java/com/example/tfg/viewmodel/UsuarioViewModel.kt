package com.example.tfg.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tfg.models.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/*class UsuarioViewModel: ViewModel {

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> = _nombre

    private val  _apellido = MutableLiveData<String>()
    val apellido: LiveData<String> = _apellido

    private val _nie = MutableLiveData<String>()
    val nie: LiveData<String> = _nie

    private val _fechaNacimiento = MutableLiveData<String>()
    val fechaNacimiento: LiveData<String> = _fechaNacimiento

    private val _sexo = MutableLiveData<String>()
    val sexo: LiveData<String> = _sexo

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _telefono = MutableLiveData<String>()
    val telefono: LiveData<String> = _telefono

    // MutableStateFlow es un flujo reactivo de Kotlin que siempre mantiene un valor actual
    // A diferencia de LiveData, está diseñado para funcionar de forma óptima con coroutines.
    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    // StateFlow es más eficiente para manejar  cambios de manera reactiva
    val usuarios: StateFlow<List<Usuario>> = _usuarios


}*/