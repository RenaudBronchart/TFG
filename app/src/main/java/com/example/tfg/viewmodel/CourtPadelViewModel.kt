package com.example.tfg.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.CourtPadel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CourtPadelViewModel : ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val name_collection = "padelCourt"

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
                val courts = query.documents.mapNotNull { doc ->
                    doc.toObject(CourtPadel::class.java)?.copy(id = doc.id) // anadir id
                }
                _courtsPadel.value = courts
                Log.d("FirestoreDebug", "Courts récupérés: $courts")
            } catch (e: Exception) {
                Log.e("FirestoreError", "Erreur: ${e.message}")
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

    fun getCourtNameById(courtId: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val doc = db.collection(name_collection).document(courtId).get().await()
                val court = doc.toObject(CourtPadel::class.java)
                onResult(court?.name ?: "Inconnu")
            } catch (e: Exception) {
                onResult("Inconnu")
            }
        }
    }

}