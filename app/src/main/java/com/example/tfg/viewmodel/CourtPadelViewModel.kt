package com.example.tfg.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.BookingPadel
import com.example.tfg.models.CourtPadel
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class CourtPadelViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val name_collection = "courtsPadel"

    private val _courtsPadel = MutableStateFlow<List<CourtPadel>>(emptyList())
    val courtsPadel: StateFlow<List<CourtPadel>> = _courtsPadel

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Charger les courts de padel depuis Firestore
    fun getCourtsPadelFromFirestore() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val query = db.collection(name_collection).get().await()
                val courts = query.documents.mapNotNull { it.toObject(CourtPadel::class.java) }
                _courtsPadel.value = courts
            } catch (e: Exception) {
                // Gestion des erreurs ici
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Ajouter un court de padel
    fun addCourtPadel(courtPadel: CourtPadel, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val docRef = db.collection(name_collection).document(courtPadel.id)
                docRef.set(courtPadel).await()
                _courtsPadel.value = _courtsPadel.value + courtPadel
                onSuccess()
            } catch (e: Exception) {
                onFailure("Erreur lors de l'ajout du court")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Supprimer un court de padel
    fun deleteCourtPadel(courtPadel: CourtPadel, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                db.collection(name_collection).document(courtPadel.id).delete().await()
                _courtsPadel.value = _courtsPadel.value.filterNot { it.id == courtPadel.id }
                onSuccess()
            } catch (e: Exception) {
                onFailure("Erreur lors de la suppression du court")
            } finally {
                _isLoading.value = false
            }
        }
    }
}