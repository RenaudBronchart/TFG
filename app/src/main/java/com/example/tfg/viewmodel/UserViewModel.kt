package com.example.tfg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.User
import com.example.tfg.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val userRepository: UserRepository = UserRepository()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        getUsersFromFirestore()
    }

    fun getUsersFromFirestore() {
        viewModelScope.launch {
            _users.value = userRepository.getUsers()
        }
    }


    fun loadUser(uid: String) {
        viewModelScope.launch {
            _user.value = userRepository.getUserById(uid)
        }
    }
}