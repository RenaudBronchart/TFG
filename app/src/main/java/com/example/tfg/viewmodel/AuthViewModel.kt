package com.example.tfg.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    // Inicializas el AuthRepository directamente
    private val authRepository: AuthRepository = AuthRepository()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()


    // Flujos para mantener el estado de la autenticación y el usuario
    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId

    // Inicializamos el flujo del usuario, que observa el cambio en el usuario actual
    private val _user = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    // Inicialización: Si hay un usuario actual, se carga el ID y se verifica si es admin
    init {
        val currentUser = auth.currentUser
        _user.value = currentUser
        _currentUserId.value = currentUser?.uid
        currentUser?.uid?.let { checkIfUserIsAdmin(it) }
    }

    // Función para cerrar sesión
    fun signOut() {
        auth.signOut()
        _user.value = null // Se actualiza el flujo de usuario cuando se cierra sesión
        _currentUserId.value = null // Se borra el ID del usuario
    }

    // Función para iniciar sesión con correo y contraseña
    suspend fun signInWithEmailAndPassword(email: String, password: String): String? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.uid
        } catch (e: Exception) {
            null
        }
    }

    // Función para crear un nuevo usuario con correo y contraseña
    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult? {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            null
        }
    }

    // Verificación si el usuario es admin (esto puede cambiarse a una verificación real con Firestore)
    private fun checkIfUserIsAdmin(userId: String) {
        // Simula la verificación del admin (esto podría ir con Firestore o la lógica que necesites)
        _isAdmin.value = userId == "adminId" // Cambia esto según la lógica de tu app
    }
}