package com.example.tfg.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tfg.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.tfg.repository.UserRepository
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ViewModel responsable de editar usuarios existentes.
// Permite cargar datos desde Firestore, actualizar campos y sincronizar cambios.
class EditUserViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> = _messageConfirmation

    private var currentUid: String? = null

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Carga todos los usuarios desde Firestore
    fun getUsersFromFirestore() {
        viewModelScope.launch {
            _users.value = userRepository.getUsers()
        }
    }
    // Carga un usuario específico desde Firestore mediante su UID
    fun loadUser(uid: String) {
        Log.d("EditUserViewModel", "loadUser called with uid: $uid")

        // Siempre recargar usuario sin importar si es el mismo
        currentUid = uid // Actualizar currentUid para el seguimiento

        viewModelScope.launch {
            try {
                val loadedUser = userRepository.getUserById(uid)
                if (loadedUser != null) {
                    _user.value = loadedUser
                    Log.d("EditUserViewModel", "Usuario cargado: $loadedUser")
                } else {
                    Log.e("EditUserViewModel", "Error: Usuario no encontrado")
                }
            } catch (e: Exception) {
                Log.e("EditUserViewModel", "Error cargando usuario: ${e.message}")
            }
        }
    }

    // Actualiza los datos del usuario actual en Firestore
    fun updateUser(uid: String, onSuccess: (String) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            val currentUser = _user.value
            if (currentUser != null) {
                val success = userRepository.updateUser(uid, currentUser)
                val message = if (success) {
                    "Usuario actualizado correctamente"
                } else {
                    "Error al actualizar usuario"
                }
                _messageConfirmation.value = message
                onSuccess(message)

                // Llamar a getUsersFromFirestore solo si es necesario
                if (success) {
                    getUsersFromFirestore()
                }
            } else {
                val errorMessage = "No se encontró el usuario"
                _messageConfirmation.value = errorMessage
                onSuccess(errorMessage)
            }
            _isLoading.value = false
        }
    }

    // Actualiza un campo específico del usuario actual
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
            } ?: currentUser
        }
    }

    fun setMessage(message: String) {
        _messageConfirmation.value = message
    }

    fun resetMessage() {
        _messageConfirmation.value = ""
    }
}

