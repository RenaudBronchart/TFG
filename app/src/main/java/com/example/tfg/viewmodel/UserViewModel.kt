package com.example.tfg.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.tfg.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel: ViewModel() {
    // instancia de la base de datos FB
    // almacenar el nombre de la colección
    private  val db = FirebaseFirestore.getInstance()
    private val  name_collection = "usuarios"

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

    private val _role = MutableLiveData<String>("user")
    val role: LiveData<String> get() = _role

    private val _contraseña = MutableLiveData<String>()
    val contraseña: LiveData<String> = _contraseña

    // MutableStateFlow es un flujo reactivo de Kotlin que siempre mantiene un valor actual
    // A diferencia de LiveData, está diseñado para funcionar de forma óptima con coroutines.
    private val _usuarios = MutableStateFlow<List<User>>(emptyList())
    // StateFlow es más eficiente para manejar  cambios de manera reactiva
    val usuarios: StateFlow<List<User>> = _usuarios

    private val _usuario = MutableStateFlow<User?>(null)
    val usuario: StateFlow<User?> = _usuario

    private val _isButtonEnable = MutableLiveData<Boolean>()
    val isButtonEnable: LiveData<Boolean> = _isButtonEnable

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> get() = _messageConfirmation

    init {
        getUsersFromFirestore()
    }

    fun getUsersFromFirestore() {
        viewModelScope.launch {


            val query = db.collection(name_collection).get().await()

            val usuarios = mutableListOf<User>()
            for (document in query.documents) {
                val usuario = document.toObject(User::class.java)
                if (usuario != null) {
                    usuarios.add(usuario)
                }
            }
            _usuarios.value = usuarios
        }
    }


    fun loadUser(uid: String) {
        viewModelScope.launch {

            try {
                val document = db.collection("usuarios").document(uid).get().await()
                val usuario = document.toObject(User::class.java)

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

    fun registerUser(authViewModel: AuthViewModel, navController: NavHostController, onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = authViewModel.createUserWithEmailAndPassword(email.value ?: "", contraseña.value ?: "")
            val user = result?.user

            if (user != null) {
                val usuario = User(
                    nombre = nombre.value ?: "",
                    apellido = apellido.value ?: "",
                    email = email.value ?: "",
                    dni = dni.value ?: "",
                    fechaNacimiento = fechaNacimiento.value ?: "",
                    telefono = telefono.value ?: "",
                    genero = genero.value ?: "",
                    role = "user"
                )

                db.collection(name_collection)
                    .document(user.uid)
                    .set(usuario)
                    .addOnSuccessListener {
                        _messageConfirmation.value = "Cuenta creada correctamente"
                        viewModelScope.launch {
                            delay(1000)
                            resetFields()
                            navController.navigate("Home") {
                                popUpTo("SignUp") { inclusive = true }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        _messageConfirmation.value = "No se ha podido crear la cuenta: ${exception.message}"
                    }
            } else {
                onResult("Error al crear cuenta")
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

    fun enableButton(nombre: String, apellido: String, dni: String, email: String, telefono: String, genero: String, fechaNacimiento: String, contraseña: String
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
    fun setMessageConfirmation(message: String) {
        _messageConfirmation.value = message
    }

}