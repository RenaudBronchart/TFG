package com.example.tfg.repository


import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(private val auth: FirebaseAuth, private val db: FirebaseFirestore) : AuthRepository {


    override suspend fun signInWithEmailAndPassword(email: String, password: String): String? {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            null
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthInvalidUserException,
                is FirebaseAuthInvalidCredentialsException -> "Usuario o contraseña incorrectos"
                else -> "Error al iniciar sesión"
            }
        }
    }

    override suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult? {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            null
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun fetchCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override suspend fun isAdmin(userId: String): Boolean {
        val role = db.collection("users").document(userId).get().await().getString("role")
        return role == "admin"
    }
}