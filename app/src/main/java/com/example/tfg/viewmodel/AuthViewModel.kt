package com.example.tfg.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class AuthViewModel : ViewModel() {
    // Instancia directamente el repositorio
    private val authRepository: AuthRepository = AuthRepository()
    private val db = FirebaseFirestore.getInstance()

    // Flujos para mantener el estado de la autenticación y el usuario
    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId

    private val _user = MutableStateFlow<FirebaseUser?>(authRepository.fetchCurrentUser())
    val user: StateFlow<FirebaseUser?> = _user


    init {
        // Actualizamos el estado del usuario y verificamos si es admin
        _user.value?.let { user ->
            viewModelScope.launch {
                _isAdmin.value = authRepository.isAdmin(user.uid)
            }
        }
    }
    // Función para cerrar sesión
    fun signOut() {
        authRepository.signOut()
        _user.value = null // Se actualiza el flujo de usuario cuando se cierra sesión
        _currentUserId.value = null // Se borra el ID del usuario
    }

    // Función para iniciar sesión con correo y contraseña
    suspend fun signInWithEmailAndPassword(email: String, password: String): String? {
        val result = authRepository.signInWithEmailAndPassword(email, password)
        if (result == null) {
            _user.value = authRepository.fetchCurrentUser()
            _currentUserId.value = _user.value?.uid
            _user.value?.uid?.let { uid ->
                _isAdmin.value = authRepository.isAdmin(uid)
            }
        }
        return result
    }

    // Función para crear un nuevo usuario con correo y contraseña
    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult? {
        return try {
            authRepository.createUserWithEmailAndPassword(email, password)
        } catch (e: Exception) {
            null
        }
    }

}