package com.example.tfg.viewmodel



import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// ViewModel encargado de gestionar la autenticación de usuarios utilizando Firebase Authentication.
// Incluye funciones para login, registro, cierre de sesión y verificación de permisos de administrador.
class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    // valor del usuario actualmente autenticado
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _resetMessage = MutableStateFlow<String?>(null)
    val resetMessage: StateFlow<String?> = _resetMessage


    // Listener que actualiza automáticamente el ID del usuario cuando cambia el estado de autenticación
    init {
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            _currentUserId.value = user?.uid
        }
    }

    // Contiene el usuario autenticado y verifica si es administrador
    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

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
            val result = authRepository.createUserWithEmailAndPassword(email, password)
            if (result != null) {
                _user.value = authRepository.fetchCurrentUser()
                _currentUserId.value = _user.value?.uid

                // Verificamos si es admin, aunque un nuevo usuario no lo sea por defecto
                _user.value?.uid?.let { uid ->
                    _isAdmin.value = authRepository.isAdmin(uid)
                }
            }
            result
        } catch (e: Exception) {
            null
        }
    }

    fun checkIfUserIsAdmin(userId: String) {
        viewModelScope.launch {
            val result = authRepository.isAdmin(userId)
            _isAdmin.value = result
        }
    }

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            try {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).await()
            } catch (e: Exception) {
                Log.e("Login", "Error al enviar email: ${e.message}")
            }
        }
    }

    fun clearResetMessage() {
        _resetMessage.value = null
    }

}