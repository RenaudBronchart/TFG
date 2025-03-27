package com.example.tfg.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    suspend fun signInWithEmailAndPassword(email: String, password: String): String?
    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult?
    fun signOut()
    fun fetchCurrentUser(): FirebaseUser?
    // suspend porque funcion asincrona -> hace una operación asincrónica con Firestore,
    suspend fun isAdmin(userId: String): Boolean

}