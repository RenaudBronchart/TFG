package com.example.tfg.repository

import com.example.tfg.models.CourtPadel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// Repositorio que encapsula el acceso a Firestore para gestionar pistas de pádel (courts).
// Permite obtener, agregar, eliminar y consultar información de pistas almacenadas en la base de datos.
class CourtPadelRepository(
private val auth: FirebaseAuth = FirebaseAuth.getInstance(),  // Inyecta la instancia de FirebaseAuth
private val db: FirebaseFirestore = FirebaseFirestore.getInstance()  // Inyecta la instancia de FirebaseFirestore
) {
    private val name_collection = "padelCourt"

    // Cargar los courts de padel desde Firestore
    suspend fun getCourtsPadelFromFirestore(): List<CourtPadel> {
        return try {
            val query = db.collection(name_collection).get().await()
            query.documents.mapNotNull { doc ->
                doc.toObject(CourtPadel::class.java)?.copy(id = doc.id) // Añadir id
            }
        } catch (e: Exception) {
            throw Exception("Error al obtener los courts de padel: ${e.message}")
        }
    }

    // Agregar un court de padel
    suspend fun addCourtPadel(courtPadel: CourtPadel): Boolean {
        return try {
            val docRef = db.collection(name_collection).document(courtPadel.id)
            docRef.set(courtPadel).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Eliminar un court de padel
    suspend fun deleteCourtPadel(courtPadel: CourtPadel): Boolean {
        return try {
            db.collection(name_collection).document(courtPadel.id).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Obtener el nombre del court por su ID
    suspend fun getCourtNameById(courtId: String): String {
        return try {
            val doc = db.collection(name_collection).document(courtId).get().await()
            val court = doc.toObject(CourtPadel::class.java)
            court?.name ?: "Inconnu"
        } catch (e: Exception) {
            "Inconnu"
        }
    }
}


