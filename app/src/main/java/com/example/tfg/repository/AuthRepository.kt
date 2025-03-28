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

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),  // Inyecta la instancia de FirebaseAuth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()  // Inyecta la instancia de FirebaseFirestore
) {

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

     suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult? {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            null
        }
    }

     fun signOut() {
        auth.signOut()
    }


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