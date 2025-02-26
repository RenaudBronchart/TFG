package com.example.tfg.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class AuthViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val name_collection = "usuarios"

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId

    private val _user = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val currentUser = firebaseAuth.currentUser
        _user.value = currentUser
        _currentUserId.value = currentUser?.uid
    }

    init {
        auth.addAuthStateListener(authStateListener)
    }

    fun removeAuthListener() {
        auth.removeAuthStateListener(authStateListener)
    }
    fun refreshCurrentUser() {
        auth.currentUser?.reload()?.addOnCompleteListener {
            _user.value = auth.currentUser
        }
    }
    fun signOut() {
        auth.signOut()
        _user.value = null
    }
    fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                    authResult ->
                authResult.user?.let { user ->
                    _user.value = user
                    _currentUserId.value = user.uid
                    checkIfUserIsAdmin(user.uid)
                }
            }
    }

    // llama al metodo de FireBase Authentication auth.createUserWithEmailAndPassword(email, password)
    // y devuelve el resultado // devuelve un objeto tipo Task<AuthResult>
    // Task es una clase de Google Play Services que representa una tarea asincrona
    // AuthResult es una clase de Firebase Authentication que representa el resultado de una autenticación
    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult? {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Error creando usuario: ${e.message}")
            null
        }
    }

    fun fetchCurrentUser() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            _currentUserId.value = user.uid
            checkIfUserIsAdmin(user.uid) // Vérifie si l'utilisateur est admin
        } else {
            Log.e("AuthViewModel", "fetchCurrentUser: Aucun utilisateur connecté")
        }
    }


    fun checkIfUserIsAdmin(userId: String) {
        db.collection(name_collection).document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val role = document.getString("role") ?: "user" // Si null, considère "user"
                    _isAdmin.value = role == "admin"

                }
            }
            .addOnFailureListener { exception ->
                Log.e("AuthViewModel", "Erreur Firebase : ${exception.message}")
            }
    }

}