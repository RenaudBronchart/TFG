package com.example.tfg.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.tfg.models.User
import com.example.tfg.models.UserFormState
import com.example.tfg.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class AddUserViewModel : ViewModel() {
    private val userRepository: UserRepository = UserRepository()

    private val _fields = MutableStateFlow(UserFormState())
    val fields: StateFlow<UserFormState> = _fields

    // MutableStateFlow es un flujo reactivo de Kotlin que siempre mantiene un valor actual
    // MutableStateFlow A diferencia de LiveData, está diseñado para funcionar de forma óptima con coroutines.
    private val _users = MutableStateFlow<List<User>>(emptyList())
    // StateFlow es más eficiente para manejar  cambios de manera reactiva
    val users: StateFlow<List<User>> = _users

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> = _messageConfirmation


    // Función para registrar un usuario
    fun registerUser(authViewModel: AuthViewModel, navController: NavHostController, onResult: (String) -> Unit) {
        viewModelScope.launch {
            val result = userRepository.registerUser(_fields.value)

            // Verifica si el resultado fue exitoso
            result.onSuccess {
                _messageConfirmation.value = it
                delay(2000)
                resetFields() // Resetear los campos después de registrar el usuario
                navController.navigate("Home") {
                    popUpTo("SignUp") { inclusive = true }
                }
            }.onFailure {
                _messageConfirmation.value = "Error: ${it.message}"
                onResult("Error al crear cuenta")
            }
        }
    }

    // Actualiza los campos del formulario
    fun onCompletedFields(userForm: UserFormState) {
        _fields.value = userForm
    }

    // Resetear los campos después del registro
    fun resetFields() {
        _fields.value = UserFormState() // Resetea todos los campos a valores predeterminados
    }

    // Establecer mensaje de confirmación
    fun setMessageConfirmation(message: String) {
        _messageConfirmation.value = message
    }

}