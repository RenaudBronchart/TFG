package com.example.tfg.repository

import com.example.tfg.models.User

interface UserRepository {
    
        suspend fun getUsers(): List<User>
        suspend fun getUserById(uid: String): User?
        suspend fun registerUser(user: User): Boolean
        suspend fun updateUser(uid: String, user: User):Boolean


}