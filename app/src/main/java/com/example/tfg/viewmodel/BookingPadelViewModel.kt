package com.example.tfg.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
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

    fun setMessageConfirmation(message: String) {
        _messageConfirmation.value = message
    }

    // Cargar datos
    fun getBookingsPadelFromFirestore() {
        viewModelScope.launch {
            _isLoading.value = true // se inicia la carga de datos
            try {
                val query = db.collection(name_collection).get().await()
                val bookings = query.documents.mapNotNull { it.toObject(BookingPadel::class.java) }
                _bookingsPadel.value = bookings
            } catch (e: Exception) {
                _messageConfirmation.value = "Error para cargar las reservas"
            } finally {
                _isLoading.value = false // operacion terminado
            }
        }
    }
    fun loadBookings() {
        viewModelScope.launch {
            try {
                val result = db.collection(name_collection).get().await()
                val bookings = result.toObjects(BookingPadel::class.java)
                _bookingsPadel.value = bookings
                Log.d("DEBUG", "Réservations chargées : $bookings")
            } catch (e: Exception) {
                Log.e("DEBUG", "Erreur de chargement des réservations", e)
            }
        }
    }


    // Reservar una pista de padel
    fun bookCourt(authViewModel: AuthViewModel, navController: NavHostController, courtId: String, date: String, startTime: String, endTime: String,  onSuccess: (String) -> Unit) {
        val userId = authViewModel.currentUserId.value ?: "" // si null, valor = ""
        val booking = BookingPadel(
            id = UUID.randomUUID().toString(),
            courtId = courtId,
            date = date,
            startTime = startTime,
            endTime = endTime,
            userId = userId,
            status = false // not esta reservado

        )

        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("BookingDebug", "CourtId: $courtId, Date: $date, StartTime: $startTime, EndTime: $endTime, UserId: $userId")
               // Verificar si la pista ya está reservada en la misma fecha y hora
                val existingBooking = db.collection(name_collection)
                    .whereEqualTo("courtId", courtId) // verificacion para ver si existe
                    .whereEqualTo("date", date)
                    .whereEqualTo("startTime", startTime)
                    .get().await()

                if (!existingBooking.isEmpty) {
                    onSuccess("Este horario no está disponible")
                    _isLoading.value = false
                    return@launch
                }

                db.collection(name_collection)
                    .document(booking.id)
                    .set(booking)
                    .await()
               val successMessage = "Reserva realizada con éxito!!"
                _messageConfirmation.value = successMessage
                onSuccess(successMessage)
                getBookingsPadelFromFirestore() // para  actualizar y mostrar las reservas en la UI actualizados
            } catch (e: Exception) {
                onSuccess("Error al realizar la reserva: ${e.message ?: "Desconocido"}") //
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Verificar si la pista est disponible // funcion asincrona
    suspend fun isCourtAvailable(courtId: String, date: String, timeSlot: String): Boolean {
        val existingBooking = db.collection(name_collection)
            .whereEqualTo("courtId", courtId)
            .whereEqualTo("date", date)
            .whereEqualTo("startTime", timeSlot.split(" - ")[0])
            .get().await() // espera la repuesta de firbase sin bloquear

        return existingBooking.isEmpty // si no hay reserva, disponible
    }
}