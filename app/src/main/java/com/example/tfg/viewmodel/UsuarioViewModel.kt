package com.example.tfg.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UsuarioViewModel: ViewModel() {

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> = _nombre

    private val _apellido = MutableLiveData<String>()
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

    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario

    private val _isButtonEnable = MutableLiveData<Boolean>()
    val isButtonEnable: LiveData<Boolean> = _isButtonEnable

    init {
        getUsuariosFromFirestore()
    }

    fun getUsuariosFromFirestore() {
        viewModelScope.launch {
            // instancia de la base de datos FB
            val db = FirebaseFirestore.getInstance()
            // almacenar el nombre de la colección
            val name_collection = "usuarios"
            val query = db.collection(name_collection).get().await()

            val usuarios = mutableListOf<Usuario>()
            for (document in query.documents) {
                val usuario = document.toObject(Usuario::class.java)
                if (usuario != null) {
                    usuarios.add(usuario)
                }
            }
            _usuarios.value = usuarios
        }
    }


    fun loadUsuario(uid: String) {
        Log.d("UsuarioViewModel", "Appel de loadUsuario avec uid: $uid")

        viewModelScope.launch {
            val db = FirebaseFirestore.getInstance()
            try {
                val document = db.collection("usuarios").document(uid).get().await()
                val usuario = document.toObject(Usuario::class.java)

                if (usuario != null) {
                    _usuario.value = usuario
                    Log.d("UsuarioViewModel", "Données récupérées : ${usuario.nombre}")
                } else {
                    Log.e("UsuarioViewModel", "Aucune donnée trouvée pour cet UID")
                }
            } catch (e: Exception) {
                Log.e("UsuarioViewModel", "Erreur lors du chargement des données : ${e.message}")
            }
        }
    }




    fun onCompletedFields(
        nombre: String,
        apellido: String,
        dni: String,
        email: String,
        telefono: String,
        genero: String,
        fechaNacimiento: String,
        contraseña: String
    ) {
        _nombre.value = nombre
        _apellido.value = apellido
        _dni.value = dni
        _email.value = email
        _telefono.value = telefono
        _genero.value = genero
        _fechaNacimiento.value = fechaNacimiento
        _contraseña.value = contraseña
        _isButtonEnable.value = enableButton(
            nombre, apellido, dni, email, telefono, genero, fechaNacimiento, contraseña
        )

        Log.d("ButtonEnabled", "isButtonEnable: ${_isButtonEnable.value}")
    }

    fun enableButton(
        nombre: String,
        apellido: String,
        dni: String,
        email: String,
        telefono: String,
        genero: String,
        fechaNacimiento: String,
        contraseña: String
    ) =
        nombre.isNotEmpty() && apellido.isNotEmpty() && dni.isNotEmpty() && email.isNotEmpty() && telefono.isNotEmpty() && genero.isNotEmpty() && fechaNacimiento.isNotEmpty() && contraseña.isNotEmpty()

    fun resetFields() {
        _nombre.value = ""
        _apellido.value = ""
        _dni.value = ""
        _email.value = ""
        _telefono.value = ""
        _genero.value = ""
        _fechaNacimiento.value = ""
        _contraseña.value = ""
    }






}