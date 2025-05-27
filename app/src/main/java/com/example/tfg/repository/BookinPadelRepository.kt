package com.example.tfg.repository

import com.example.tfg.models.BookingPadel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// Repositorio que encapsula el acceso a Firestore para reservas de pádel.
// Se encarga de crear, consultar y verificar la disponibilidad de pistas.
class BookingPadelRepository (
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),  // Inyecta la instancia de FirebaseAuth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()  // Inyecta la instancia de FirebaseFirestore
) {
    private val name_collection = "bookingsPadel"

    // Obtiene todas las reservas de la colección en Firestore.
    // Devuelve una lista de BookingPadel o una lista vacía si ocurre un error.
    suspend fun getBookingsPadelFromFirestore(): List<BookingPadel> {
        return try {
            val querySnapshot = db.collection(name_collection).get().await()
            querySnapshot.documents.mapNotNull { it.toObject(BookingPadel::class.java) }
        } catch (e: Exception) {
            emptyList()  // Si hay un error, devolvemos una lista vacía
        }
    }
    //para obtener las reservas
    suspend fun loadBookings(): List<BookingPadel> {
        return try {
            val querySnapshot = db.collection(name_collection).get().await()
            querySnapshot.documents.mapNotNull { it.toObject(BookingPadel::class.java) }
        } catch (e: Exception) {
            emptyList()  // Devolver lista vacía si hay error
        }
    }

    // Crea una nueva reserva en Firestore con el ID del booking como documento.
    // Devuelve true si fue exitosa, o false si ocurrió un error.
    suspend fun bookCourt(bookingPadel: BookingPadel): Boolean {
        return try {
            db.collection(name_collection).document(bookingPadel.id).set(bookingPadel).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Verifica si una pista está disponible en una fecha y franja horaria determinada.
    // Devuelve true si no hay reservas conflictivas, false si ya existe una.
    suspend fun isCourtAvailable(courtId: String, date: String, timeSlot: String): Boolean {
        val existingBooking = db.collection(name_collection)
            .whereEqualTo("courtId", courtId)
            .whereEqualTo("date", date)
            .whereEqualTo("startTime", timeSlot.split(" - ")[0])
            .get().await()

        return existingBooking.isEmpty
    }
}

