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
import java.util.UUID

class UserViewModel: ViewModel() {
    // instancia de la base de datos FB
    // almacenar el nombre de la colección
    private  val db = FirebaseFirestore.getInstance()
    private val  name_collection = "users"

    private val _name = MutableStateFlow<String>("")
    val name: StateFlow<String> = _name //

    private val _firstname = MutableStateFlow<String>("")
    val firstname: StateFlow<String> = _firstname

    private val _dni = MutableStateFlow<String>("")
    val dni: StateFlow<String> = _dni

    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> = _email

    private val _phone = MutableStateFlow<String>("")
    val phone: StateFlow<String> = _phone

    private val _gender = MutableStateFlow<String>("")
    val gender: StateFlow<String> = _gender

    private val _birthday = MutableStateFlow<String>("")
    val birthday: StateFlow<String> = _birthday

    private val _role = MutableStateFlow<String>("user")
    val role: StateFlow<String> get() = _role

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password

    // MutableStateFlow es un flujo reactivo de Kotlin que siempre mantiene un valor actual
    // MutableStateFlow A diferencia de LiveData, está diseñado para funcionar de forma óptima con coroutines.
    private val _users = MutableStateFlow<List<User>>(emptyList())
    // StateFlow es más eficiente para manejar  cambios de manera reactiva
    val users: StateFlow<List<User>> = _users

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isButtonEnable = MutableStateFlow(false)
    val isButtonEnable: StateFlow<Boolean> = _isButtonEnable

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> get() = _messageConfirmation

    init {
        getUsersFromFirestore()
    }

    fun getUsersFromFirestore() {
        viewModelScope.launch {
            try {
                val query = db.collection(name_collection).get().await()

                // Utilisation de mapNotNull pour récupérer les utilisateurs valides
                val users = query.documents.mapNotNull { it.toObject(User::class.java) }

                // Mise à jour de l'état avec la liste des utilisateurs
                _users.value = users

                Log.d("UserViewModel", "Nombre d'utilisateurs récupérés : ${users.size}")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Erreur lors de la récupération des utilisateurs : ${e.message}")
            }
        }
    }



    fun loadUser(uid: String) {
        viewModelScope.launch {
            try {
                val document = db.collection(name_collection).document(uid).get().await()
                val user = document.toObject(User::class.java)
                _user.value = user
            } catch (e: Exception) {
                Log.e("UsuarioViewModel", "error para cargar datos : ${e.message}")
            }
        }
    }

    fun registerUser(authViewModel: AuthViewModel, navController: NavHostController, onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = authViewModel.createUserWithEmailAndPassword(email.value ?: "", password.value ?: "")
            val user = result?.user

            if (user != null) {
                val userId = user.uid
                val newUser  = User(
                    id = userId,
                    name = name.value ?: "",
                    firstname = firstname.value ?: "",
                    email = email.value ?: "",
                    dni = dni.value ?: "",
                    birthday = birthday.value ?: "",
                    phone = phone.value ?: "",
                    gender = gender.value ?: "",
                    role = "user"
                )
                try {
                    db.collection(name_collection).document(userId).set(newUser).await()
                    _messageConfirmation.value = "Cuenta creada correctamente"
                    delay(1000)
                    resetFields()
                    navController.navigate("Home") {
                        popUpTo("SignUp") { inclusive = true }
                    }
                } catch (exception: Exception) {
                    _messageConfirmation.value = "No se ha podido crear la cuenta: ${exception.message}"
                }
            } else {
                onResult("Error al crear cuenta")
            }
        }
    }

    fun onCompletedFields(
        name: String,
        firstname: String,
        dni: String,
        email: String,
        phone: String,
        gender: String,
        birthday: String,
        password: String
    ) {
        _name.value = name
        _firstname.value = firstname
        _dni.value = dni
        _email.value = email
        _phone.value = phone
        _gender.value = gender
        _birthday.value = birthday
        _password.value = password
        _isButtonEnable.value = enableButton(
            name, firstname, dni, email, phone, gender, birthday, password
        )

        Log.d("ButtonEnabled", "isButtonEnable: ${_isButtonEnable.value}")
    }

    fun enableButton(name: String, firstname: String, dni: String, email: String, phone: String, gender: String, birthday: String, password: String
    ) =
        name.isNotEmpty() && firstname.isNotEmpty() && dni.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && gender.isNotEmpty() && birthday.isNotEmpty() && password.isNotEmpty()

    fun resetFields() {
        _name.value = ""
        _firstname.value = ""
        _dni.value = ""
        _email.value = ""
        _phone.value = ""
        _gender.value = ""
        _birthday.value = ""
        _password.value = ""
    }
    fun setMessageConfirmation(message: String) {
        _messageConfirmation.value = message
    }

}