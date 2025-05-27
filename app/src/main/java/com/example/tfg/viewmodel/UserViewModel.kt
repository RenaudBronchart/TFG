package com.example.tfg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.User
import com.example.tfg.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel responsable de gestionar la carga de usuarios.
// Permite obtener todos los usuarios o cargar uno específico desde Firestore.
class UserViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        getUsersFromFirestore()
    }
    // Cargar todos los usuarios desde Firestore
    fun getUsersFromFirestore() {
        viewModelScope.launch {
            _users.value = userRepository.getUsers()
        }
    }
    // Cargar un usuario específico por su UID
    fun loadUser(uid: String) {
        viewModelScope.launch {
            _user.value = userRepository.getUserById(uid)
        }
    }
}