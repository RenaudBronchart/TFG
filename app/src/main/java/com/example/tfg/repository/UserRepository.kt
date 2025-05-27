package com.example.tfg.repository

import com.example.tfg.models.User
import com.example.tfg.models.UserFormState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// Repositorio que encapsula el acceso a Firebase Authentication y Firestore para gestionar usuarios.
// Permite registrar, consultar y actualizar usuarios en la aplicación.
class UserRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val collectionName = "users"
    // Obtiene la lista completa de usuarios desde Firestore.
    // Devuelve una lista vacía si ocurre un error.
     suspend fun getUsers(): List<User> {
        return try {
            val querySnapshot = db.collection(collectionName).get().await()
            querySnapshot.documents.mapNotNull { it.toObject(User::class.java) }
        } catch (e: Exception) {
            emptyList() // Si hay error, devolvemos una lista vacía
        }
    }
    // Obtiene un usuario específico a partir de su UID.
    // Devuelve null si no se encuentra o si ocurre un error.
     suspend fun getUserById(uid: String): User? {
        return try {
            val document = db.collection(collectionName).document(uid).get().await()
            document.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // Registrar un nuevo usuario
    suspend fun registerUser(userFormState: UserFormState): Result<String> {
        return try {
            // Primero se valida que los campos no estén vacíos
            if (!userFormState.isValid()) {
                return Result.failure(Exception("Todos los campos son obligatorios"))
            }

            // Crear usuario en Firebase Auth
            val result = auth.createUserWithEmailAndPassword(userFormState.email, userFormState.password).await()

            // Crear el objeto User
            val newUser = User(
                id = result.user?.uid ?: throw Exception("Error al obtener UID del usuario"),
                name = userFormState.name,
                firstname = userFormState.firstname,
                email = userFormState.email,
                dni = userFormState.dni,
                birthday = userFormState.birthday,
                phone = userFormState.phone,
                gender = userFormState.gender,
                role = "user" // Rol predeterminado
            )

            // Guardar el usuario en Firestore
            db.collection(collectionName).document(newUser.id).set(newUser).await()

            Result.success("Cuenta creada correctamente")
        } catch (e: Exception) {
            // Si hay algún error, devolvemos el fallo con el mensaje de excepción
            Result.failure(e)
        }
    }
    // Actualiza los datos personales de un usuario existente en Firestore.
    // Devuelve true si fue exitoso, o false si ocurrió un error.
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