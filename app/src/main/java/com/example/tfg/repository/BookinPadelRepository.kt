package com.example.tfg.repository

import com.example.tfg.models.BookingPadel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BookingPadelRepository (
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),  // Inyecta la instancia de FirebaseAuth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()  // Inyecta la instancia de FirebaseFirestore
) {
    private val name_collection = "bookingsPadel"

    // Obtener todas las reservas de la base de datos
    suspend fun getBookingsPadelFromFirestore(): List<BookingPadel> {
        return try {
            val querySnapshot = db.collection(name_collection).get().await()
            querySnapshot.documents.mapNotNull { it.toObject(BookingPadel::class.java) }
        } catch (e: Exception) {
            emptyList()  // Si hay un error, devolvemos una lista vacía
        }
    }

    suspend fun loadBookings(): List<BookingPadel> {
        return try {
            val querySnapshot = db.collection(name_collection).get().await()
            querySnapshot.documents.mapNotNull { it.toObject(BookingPadel::class.java) }
        } catch (e: Exception) {
            emptyList()  // Devolver lista vacía si hay error
        }
    }

    // Reservar una pista de padel
    suspend fun bookCourt(bookingPadel: BookingPadel): Boolean {
        return try {
            db.collection(name_collection).document(bookingPadel.id).set(bookingPadel).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Verificar si la pista está disponible en una fecha y hora determinada
    suspend fun isCourtAvailable(courtId: String, date: String, timeSlot: String): Boolean {
        val existingBooking = db.collection(name_collection)
            .whereEqualTo("courtId", courtId)
            .whereEqualTo("date", date)
            .whereEqualTo("startTime", timeSlot.split(" - ")[0])
            .get().await()

        return existingBooking.isEmpty
    }
}

