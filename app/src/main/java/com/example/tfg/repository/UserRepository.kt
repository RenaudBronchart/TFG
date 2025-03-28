package com.example.tfg.repository

import com.example.tfg.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class UserRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val collectionName = "users"

     suspend fun getUsers(): List<User> {
        return try {
            val querySnapshot = db.collection(collectionName).get().await()
            querySnapshot.documents.mapNotNull { it.toObject(User::class.java) }
        } catch (e: Exception) {
            emptyList() // Si hay error, devolvemos una lista vacía
        }
    }

     suspend fun getUserById(uid: String): User? {
        return try {
            val document = db.collection(collectionName).document(uid).get().await()
            document.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

     suspend fun registerUser(user: User): Boolean {
        return try {
            db.collection(collectionName).document(user.id).set(user).await()
            true
        } catch (e: Exception) {
            false
        }
    }

     suspend fun updateUser(uid: String, user: User): Boolean {
        return try {
            val updates = mapOf(
                "name" to user.name,
                "firstname" to user.firstname,
                "email" to user.email,
                "phone" to user.phone,
                "dni" to user.dni,
                "gender" to user.gender,
                "birthday" to user.birthday
            )
            db.collection(collectionName).document(uid).update(updates).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}