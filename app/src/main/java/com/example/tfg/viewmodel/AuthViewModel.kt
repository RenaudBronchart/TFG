package com.example.tfg.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class AuthViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _user = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _user.value = firebaseAuth.currentUser
        }
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


}