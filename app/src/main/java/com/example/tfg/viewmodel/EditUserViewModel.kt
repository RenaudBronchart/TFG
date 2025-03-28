package com.example.tfg.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tfg.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.tfg.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class EditUserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> = _messageConfirmation

    private var currentUid: String? = null

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun getUsersFromFirestore() {
        viewModelScope.launch {
            _users.value = userRepository.getUsers()
        }
    }

    fun loadUser(uid: String) {
        if (currentUid == uid) return
        currentUid = uid

        viewModelScope.launch {
            val loadedUser = userRepository.getUserById(uid)
            if (loadedUser != null) {
                _user.value = loadedUser
            } else {
                Log.e("EditUserViewModel", "Error: Usuario no encontrado")
            }
        }
    }

        // se podria pasar un map of en User y utilizar un emit?
    /*fun updateUser(uid: String, onSuccess: (String) -> Unit) {
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
    }*/

    fun updateUser(uid: String, onSuccess: (String) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            val currentUser = _user.value
            if (currentUser != null) {
                val success = userRepository.updateUser(uid, currentUser)
                _messageConfirmation.value = if (success) {
                    "Usuario actualizado correctamente"
                } else {
                    "Error al actualizar usuario"
                }
                onSuccess(_messageConfirmation.value)
                if (success) getUsersFromFirestore()
            } else {
                _messageConfirmation.value = "No se encontró el usuario"
                onSuccess(_messageConfirmation.value)
            }
            _isLoading.value = false
        }
    }

    /* Métodos para actualizar los valores del usuario
    fun setName(value: String) { _user.update { it.copy(name = value) } }
    fun setFirstname(value: String) { _user.update { it.copy(firstname = value) } }
    fun setEmail(value: String) { _user.update { it.copy(email = value) } }
    fun setPhone(value: String) { _user.update { it.copy(phone = value) } }
    fun setDni(value: String) { _user.update { it.copy(dni = value) } }
    fun setGender(value: String) { _user.update { it.copy(gender = value) } }
    fun setBirthday(value: String) { _user.update { it.copy(birthday = value) } }

    */
    fun updateUserField(field: String, value: String) {
        _user.update { currentUser ->
            currentUser?.let {
                when (field) {
                    "name" -> it.copy(name = value)
                    "firstname" -> it.copy(firstname = value)
                    "email" -> it.copy(email = value)
                    "phone" -> it.copy(phone = value)
                    "dni" -> it.copy(dni = value)
                    "gender" -> it.copy(gender = value)
                    "birthday" -> it.copy(birthday = value)
                    else -> it
                }
            } ?: currentUser  // Si es null, retorna el mismo estado sin cambios
        }
    }

    fun setMessageConfirmation(message: String) {
        _messageConfirmation.value = message
    }

    fun resetMessage() {
        _messageConfirmation.value = ""
    }
}

