package com.example.tfg.repository

import com.example.tfg.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class UserRepositoryImpl(private val auth: FirebaseAuth, private val db: FirebaseFirestore) : UserRepository {

    private val nameCollection = "users"

    override suspend fun getUsers(): List<User> {
        return try {
            val querySnapshot = db.collection(nameCollection).get().await()
            querySnapshot.documents.mapNotNull { it.toObject(User::class.java) }
        } catch (e: Exception) {
            emptyList() // Si hay error, devolvemos una lista vacía
        }
    }

    override suspend fun getUserById(uid: String): User? {
        return try {
            val document = db.collection(nameCollection).document(uid).get().await()
            document.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun registerUser(user: User): Boolean {
        return try {
            db.collection(nameCollection).document(user.id).set(user).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun updateUser(uid: String, user: User): Boolean {
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
            db.collection(nameCollection).document(uid).update(updates).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}