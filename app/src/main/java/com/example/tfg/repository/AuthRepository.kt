package com.example.tfg.repository


import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

// Repositorio encargado de manejar la autenticación y autorización con Firebase Authentication.
// Incluye funciones para iniciar sesión, registrar usuarios, cerrar sesión y verificar roles.
class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),  // Inyecta la instancia de FirebaseAuth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()  // Inyecta la instancia de FirebaseFirestore
) {
    // Obtiene el usuario actualmente autenticado (puede ser null si no hay sesión)
    fun fetchCurrentUser(): FirebaseUser? = auth.currentUser


    // Inicia sesión con email y contraseña.
    // Devuelve null en caso de éxito o un mensaje de error si falla.
    suspend fun signInWithEmailAndPassword(email: String, password: String): String? {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            // Si se inicia sesión correctamente, devolvemos null (no hay error)
            null
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthInvalidUserException,
                is FirebaseAuthInvalidCredentialsException -> "Usuario o contraseña incorrecta"
                else -> "Error al iniciar sesión: ${e.localizedMessage}"
            }
        }
    }
    // Crea un nuevo usuario con email y contraseña.
    // Devuelve el AuthResult si fue exitoso, o null si ocurrió un error.
     suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult? {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            null
        }
    }
    // Cierra la sesión del usuario actual.
     fun signOut() {
        auth.signOut()
    }

    // Verifica si un usuario tiene rol de administrador consultando su documento en Firestore.
    suspend fun isAdmin(userId: String): Boolean {
        return try {
            val document = db.collection("users").document(userId).get().await()
            val role = document.getString("role") ?: "user"
            role == "admin"
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al verificar rol: ${e.localizedMessage}", e)
            false
        }
    }
}