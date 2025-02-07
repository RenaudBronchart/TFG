package com.example.tfg.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tfg.models.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UsuarioViewModel: ViewModel() {

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> = _nombre

    private val  _apellido = MutableLiveData<String>()
    val apellido: LiveData<String> = _apellido

    private val _dni = MutableLiveData<String>()
    val dni: LiveData<String> = _dni

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _telefono = MutableLiveData<String>()
    val telefono: LiveData<String> = _telefono

    private val _genero = MutableLiveData<String>()
    val genero: LiveData<String> = _genero

    private val _fechaNacimiento = MutableLiveData<String>()
    val fechaNacimiento: LiveData<String> = _fechaNacimiento

    private val _contraseña = MutableLiveData<String>()
    val contraseña: LiveData<String> = _contraseña

    // MutableStateFlow es un flujo reactivo de Kotlin que siempre mantiene un valor actual
    // A diferencia de LiveData, está diseñado para funcionar de forma óptima con coroutines.
    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    // StateFlow es más eficiente para manejar  cambios de manera reactiva
    val usuarios: StateFlow<List<Usuario>> = _usuarios

    private val _isButtonEnable = MutableLiveData<Boolean>()
    val isButtonEnable: LiveData<Boolean> = _isButtonEnable

    fun onCompletedFields(nombre: String, apellido: String, dni: String, email: String, telefono: String, genero: String, fechaNacimiento: String, contraseña: String) {
        _nombre.value = nombre
        _apellido.value = apellido
        _dni.value = dni
        _email.value = email
        _telefono.value = telefono
        _genero.value = genero
        _fechaNacimiento.value = fechaNacimiento
        _contraseña.value = contraseña
        _isButtonEnable.value = enableButton(nombre, apellido, dni, email, telefono, genero, fechaNacimiento, contraseña)

        Log.d("ButtonEnabled", "isButtonEnable: ${_isButtonEnable.value}")
    }

    fun enableButton(nombre: String, apellido: String, dni: String, email: String, telefono: String, genero: String, fechaNacimiento: String, contraseña: String) =
         nombre.isNotEmpty() && apellido.isNotEmpty() && dni.isNotEmpty() && email.isNotEmpty() && telefono.isNotEmpty() && genero.isNotEmpty() && fechaNacimiento.isNotEmpty() && contraseña.isNotEmpty()


}