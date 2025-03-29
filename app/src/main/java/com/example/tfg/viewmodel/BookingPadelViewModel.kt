package com.example.tfg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.tfg.models.BookingPadel
import com.example.tfg.repository.BookingPadelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class BookingPadelViewModel : ViewModel() {
    private val bookingPadelRepository: BookingPadelRepository = BookingPadelRepository()

    private val _bookingsPadel = MutableStateFlow<List<BookingPadel>>(emptyList())
    val bookingsPadel: StateFlow<List<BookingPadel>> = _bookingsPadel

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> = _messageConfirmation

    fun setMessageConfirmation(message: String) {
        _messageConfirmation.value = message
    }

    // Cargar las reservas desde Firestore
    fun getBookingsPadelFromFirestore() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val bookings = bookingPadelRepository.getBookingsPadelFromFirestore()
                _bookingsPadel.value = bookings
            } catch (e: Exception) {
                _messageConfirmation.value = "Error para cargar las reservas"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadBookings() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val bookings = bookingPadelRepository.loadBookings()
                _bookingsPadel.value = bookings
            } catch (e: Exception) {
                _messageConfirmation.value = "Error al cargar las reservas"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función para refrescar las reservas después de realizar una acción (como una nueva reserva)
    fun refreshBookings() {
        loadBookings()  // Esto simplemente vuelve a cargar las reservas
    }

    // Reservar una pista de padel
    fun bookCourt(
        authViewModel: AuthViewModel,
        navController: NavHostController,
        courtId: String,
        date: String,
        startTime: String,
        endTime: String,
        onSuccess: (String) -> Unit
    ) {
        val userId = authViewModel.currentUserId.value ?: "" // Si es null, asignamos ""
        val booking = BookingPadel(
            id = UUID.randomUUID().toString(),
            courtId = courtId,
            date = date,
            startTime = startTime,
            endTime = endTime,
            userId = userId,
            status = false // no está reservado aún
        )

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Verificar si el court está disponible
                val isAvailable = bookingPadelRepository.isCourtAvailable(courtId, date, startTime)
                if (!isAvailable) {
                    _messageConfirmation.value = "Este horario no está disponible"
                    onSuccess("Este horario no está disponible")
                    _isLoading.value = false
                    return@launch
                }

                // Si está disponible, realizar la reserva
                val success = bookingPadelRepository.bookCourt(booking)
                if (success) {
                    _messageConfirmation.value = "Reserva realizada con éxito!!"
                    onSuccess("Reserva realizada con éxito!!")
                    getBookingsPadelFromFirestore() // Actualizar las reservas en la UI
                } else {
                    _messageConfirmation.value = "Error al realizar la reserva"
                    onSuccess("Error al realizar la reserva")
                }
            } catch (e: Exception) {
                _messageConfirmation.value = "Error al realizar la reserva: ${e.message ?: "Desconocido"}"
                onSuccess("Error al realizar la reserva")
            } finally {
                _isLoading.value = false
            }
        }
    }
}