package com.example.tfg.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.Order
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class OrderViewModel:ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val name_collection = "orders"

    private val _orders =
        MutableStateFlow<List<Order>>(emptyList())// emptyList() se usa para inicializar la lista como vacía
    val orders: StateFlow<List<Order>> = _orders

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> = _messageConfirmation

    fun setMessageConfirmation(message: String) {
        _messageConfirmation.value = message
    }

    fun getOrdersFromFireStore() {
        viewModelScope.launch {

            _isLoading.value = true // Indicamos que la carga está en proceso
            try {
                // queremos los documebntos
                val query = db.collection(name_collection).get().await()
                //  los documentos en objetos orders
                val orders = query.documents.mapNotNull { it.toObject(Order::class.java) }
                // Actualizamos orders con la lista  obtenida
                _orders.value = orders
            } catch (e: Exception) {
                _messageConfirmation.value = "Error para cargar las reservas"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadOrders(userId: String) {
        viewModelScope.launch {
            try {
                val result = db.collection(name_collection).get().await()
                val orders = result.toObjects(Order::class.java)
                _orders.value = orders
                Log.d("DEBUG", "Réservations chargées : $orders")
            } catch (e: Exception) {
                Log.e("DEBUG", "Erreur de chargement des réservations", e)
            }
        }
    }

    fun loadLastOrder(orderId: String) {
        viewModelScope.launch {
            try {
                val result = db.collection(name_collection)
                    .document(orderId)
                    .get()
                    .await()

                val order = result.toObject(Order::class.java)
                _orders.value = listOfNotNull(order) // On ne garde que cette commande
                Log.d("DEBUG", "Dernière commande chargée : $order")
            } catch (e: Exception) {
                Log.e("DEBUG", "Erreur de chargement de la commande", e)
            }
        }
    }



}