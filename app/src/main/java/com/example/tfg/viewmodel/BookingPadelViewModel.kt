package com.example.tfg.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.BookingPadel
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class BookingPadelViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val name_collection = "bookingsPadel"

    private val _bookingsPadel = MutableStateFlow<List<BookingPadel>>(emptyList())
    val bookingsPadel: StateFlow<List<BookingPadel>> = _bookingsPadel

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> = _messageConfirmation

    // Charger les réservations depuis Firestore
    fun getBookingsPadelFromFirestore() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val query = db.collection(name_collection).get().await()
                val bookings = query.documents.mapNotNull { it.toObject(BookingPadel::class.java) }
                _bookingsPadel.value = bookings
            } catch (e: Exception) {
                _messageConfirmation.value = "Erreur lors du chargement des réservations"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Réserver un terrain de padel
    fun bookCourt(courtId: String, date: String, startTime: String, endTime: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val booking = BookingPadel(
            id = UUID.randomUUID().toString(),
            courtId = courtId,
            date = date,
            startTime = startTime,
            endTime = endTime,
            userId = "currentUserId",  // Vous devrez probablement remplacer cette partie par un utilisateur authentifié
            status = false  // Marquer la réservation comme non disponible
        )

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val existingBooking = db.collection(name_collection)
                    .whereEqualTo("courtId", courtId)
                    .whereEqualTo("date", date)
                    .whereEqualTo("startTime", startTime)
                    .get().await()

                if (!existingBooking.isEmpty) {
                    onFailure("Ce créneau est déjà réservé")
                    _isLoading.value = false
                    return@launch
                }

                db.collection(name_collection)
                    .document(booking.id)
                    .set(booking)
                    .await()

                _messageConfirmation.value = "Réservation réussie !"
                getBookingsPadelFromFirestore() // Actualiser la liste des réservations
                onSuccess()
            } catch (e: Exception) {
                onFailure("Erreur lors de la réservation")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Vérifier si le terrain est disponible à un créneau donné
    fun isCourtAvailable(courtId: String, date: String, timeSlot: String): Boolean {
        return _bookingsPadel.value.none { it.courtId == courtId && it.date == date && it.startTime == timeSlot }
    }
}