package com.example.tfg.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId

    private val _user = MutableStateFlow<FirebaseUser?>(authRepository.fetchCurrentUser())
    val user: StateFlow<FirebaseUser?> = _user

    init {
        val currentUser = authRepository.fetchCurrentUser()
        _user.value = currentUser
        _currentUserId.value = currentUser?.uid
        // Usamos una corrutina para llamar a isAdmin de manera asíncrona
        currentUser?.uid?.let { checkIfUserIsAdmin(it) }
    }

    fun signOut() {
        authRepository.signOut()
        _user.value = null
        _currentUserId.value = null
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): String? {
        return authRepository.signInWithEmailAndPassword(email, password)
    }

    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult? {
        return authRepository.createUserWithEmailAndPassword(email, password)
    }

    private fun checkIfUserIsAdmin(userId: String) {
        // Ejecutamos la verificación dentro de una corrutina
        viewModelScope.launch {
            _isAdmin.value = authRepository.isAdmin(userId)
        }
    }
}