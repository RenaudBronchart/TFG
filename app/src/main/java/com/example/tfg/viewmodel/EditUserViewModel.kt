package com.example.tfg.viewmodel


import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tfg.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class EditUserViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val name_collection = "usuarios"

    private val _usuario = MutableStateFlow(User())  // Nunca es null
    val usuario: StateFlow<User> = _usuario

    private val _messageConfirmation = MutableStateFlow("")
    val mensajeConfirmacion: StateFlow<String> get() = _messageConfirmation

    private var currentUid: String? = null  // Guardamos el UID para evitar recargar datos innecesariamente

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun loadUser(uid: String) {
        if (currentUid == uid) return  // Evitar recargar si ya tenemos los datos correctos
        currentUid = uid

        viewModelScope.launch {
            try {
                val document = db.collection(name_collection).document(uid).get().await()
                val user = document.toObject(User::class.java)

                if (user != null) {
                    _usuario.value = user
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
                val user = usuario.value
                val userUpdates = mapOf(
                    "nombre" to user.nombre,
                    "apellido" to user.apellido,
                    "email" to user.email,
                    "telefono" to user.telefono,
                    "dni" to user.dni,
                    "genero" to user.genero,
                    "fechaNacimiento" to user.fechaNacimiento
                )

                db.collection(name_collection).document(uid).update(userUpdates).await()
                delay(2000)
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
    fun setNombre(value: String) { _usuario.update { it.copy(nombre = value) } }
    fun setApellido(value: String) { _usuario.update { it.copy(apellido = value) } }
    fun setEmail(value: String) { _usuario.update { it.copy(email = value) } }
    fun setTelefono(value: String) { _usuario.update { it.copy(telefono = value) } }
    fun setDni(value: String) { _usuario.update { it.copy(dni = value) } }
    fun setGenero(value: String) { _usuario.update { it.copy(genero = value) } }
    fun setFechaNacimiento(value: String) { _usuario.update { it.copy(fechaNacimiento = value) } }

    fun setMessageConfirmation(message: String) {
        _messageConfirmation.value = message
    }

    fun resetMessage() {
        _messageConfirmation.value = ""
    }
}

