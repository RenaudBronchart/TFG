package com.example.tfg.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tfg.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class EditUserViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val name_collection = "users"

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _user = MutableStateFlow(User())  // Nunca es null
    val user: StateFlow<User> = _user

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> get() = _messageConfirmation

    private var currentUid: String? = null  //

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


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
        if (currentUid == uid) return  // Evitar recargar si ya tenemos los datos correctos
        currentUid = uid

        viewModelScope.launch {
            try {
                val document = db.collection(name_collection).document(uid).get().await()
                val user = document.toObject(User::class.java)

                if (user != null) {
                    _user.value = user
                } else {
                    Log.e("EditUserViewModel", "Error: Usuario no encontrado")
                }
            } catch (e: Exception) {
                Log.e("EditUserViewModel", "Error al cargar usuario: ${e.message}")
            }
        }
    }

        // se podria pasar un map of en User y utilizar un emit?
    fun updateUser(uid: String, onSuccess: (String) -> Unit) {
            _isLoading.value = true
        viewModelScope.launch {
            try {
                val user = user.value
                val userUpdates = mapOf(
                    "name" to user.name,
                    "firstname" to user.firstname,
                    "email" to user.email,
                    "phone" to user.phone,
                    "dni" to user.dni,
                    "gender" to user.gender,
                    "birthday" to user.birthday
                )

                db.collection(name_collection).document(uid).update(userUpdates).await()


                getUsersFromFirestore()
                val successMessage = "Usuario actualizado correctamente"
                _messageConfirmation.value = successMessage
                onSuccess(successMessage)

            } catch (exception: Exception) {
                val errorMessage = "Error al actualizar usuario: ${exception.message}"
                _messageConfirmation.value = errorMessage
                Log.e("UpdateUser", "Error: ${exception.message}")
                onSuccess(errorMessage)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Métodos para actualizar los valores del usuario
    fun setName(value: String) { _user.update { it.copy(name = value) } }
    fun setFirstname(value: String) { _user.update { it.copy(firstname = value) } }
    fun setEmail(value: String) { _user.update { it.copy(email = value) } }
    fun setPhone(value: String) { _user.update { it.copy(phone = value) } }
    fun setDni(value: String) { _user.update { it.copy(dni = value) } }
    fun setGender(value: String) { _user.update { it.copy(gender = value) } }
    fun setBirthday(value: String) { _user.update { it.copy(birthday = value) } }

    fun setMessageConfirmation(message: String) {
        _messageConfirmation.value = message
    }

    fun resetMessage() {
        _messageConfirmation.value = ""
    }
}

