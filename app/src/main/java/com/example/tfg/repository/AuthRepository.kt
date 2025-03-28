package com.example.tfg.repository


import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),  // Inyecta la instancia de FirebaseAuth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()  // Inyecta la instancia de FirebaseFirestore
) {


    suspend fun signInWithEmailAndPassword(email: String, password: String): String? {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()

            // Si la autenticación es exitosa, no retornamos ningún error
            authResult.user?.uid ?: null
        } catch (e: Exception) {
            // Manejo de errores con mensajes específicos
            when (e) {
                is FirebaseAuthInvalidUserException -> "Usuario no encontrado."
                is FirebaseAuthInvalidCredentialsException -> "Contraseña incorrecta."
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

     fun fetchCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

     suspend fun isAdmin(userId: String): Boolean {
        val role = db.collection("users").document(userId).get().await().getString("role")
        return role == "admin"
    }
}