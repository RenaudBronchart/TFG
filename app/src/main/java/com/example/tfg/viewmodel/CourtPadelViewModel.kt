package com.example.tfg.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.CourtPadel
import com.example.tfg.repository.CourtPadelRepository
import com.example.tfg.repository.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


// ViewModel responsable de gestionar las pistas de pádel.
// Maneja operaciones como carga, adición y eliminación de pistas desde Firestore.
class CourtPadelViewModel(
    private val courtPadelRepository: CourtPadelRepository = CourtPadelRepository()
) : ViewModel() {


    private val _courtsPadel = MutableStateFlow<List<CourtPadel>>(emptyList())
    val courtsPadel: StateFlow<List<CourtPadel>> = _courtsPadel

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Carga todas las pistas de pádel desde Firestore
    fun getCourtsPadelFromFirestore() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val courts = courtPadelRepository.getCourtsPadelFromFirestore()
                _courtsPadel.value = courts
                Log.d("FirestoreDebug", "Pistas obtenidas: $courts")
            } catch (e: Exception) {
                Log.e("FirestoreError", "Error al obtener pistas: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Agregar un court de padel
    fun addCourtPadel(courtPadel: CourtPadel, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = courtPadelRepository.addCourtPadel(courtPadel)
                if (success) {
                    _courtsPadel.value = _courtsPadel.value + courtPadel
                    onSuccess()
                } else {
                    onFailure("Error al agregar el court de padel")
                }
            } catch (e: Exception) {
                onFailure("Error al agregar el court de padel")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteCourtPadel(courtPadel: CourtPadel, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = courtPadelRepository.deleteCourtPadel(courtPadel)
                if (success) {
                    _courtsPadel.value = _courtsPadel.value.filterNot { it.id == courtPadel.id }
                    onSuccess()
                } else {
                    onFailure("Error al eliminar el court de padel")
                }
            } catch (e: Exception) {
                onFailure("Error al eliminar el court de padel")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Obtener el nombre del court por su ID
    fun getCourtNameById(courtId: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val name = courtPadelRepository.getCourtNameById(courtId)
                onResult(name)
            } catch (e: Exception) {
                onResult("Inconnu")
            }
        }
    }
}