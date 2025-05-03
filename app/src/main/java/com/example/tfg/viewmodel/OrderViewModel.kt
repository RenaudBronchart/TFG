package com.example.tfg.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.Order
import com.example.tfg.models.Product
import com.example.tfg.repository.CartShoppingRepository
import com.example.tfg.repository.OrderRepository
import com.example.tfg.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class OrderViewModel(
    private val orderRepository: OrderRepository = OrderRepository(),
    private val cartShoppingRepository: CartShoppingRepository = CartShoppingRepository()
) : ViewModel() {

    // emptyList() se usa para inicializar la lista como vacía
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _lastOrder = MutableStateFlow<Order?>(null)
    val lastOrder: StateFlow<Order?> = _lastOrder

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> = _messageConfirmation

    fun setMessageConfirmation(message: String) {
        _messageConfirmation.value = message
    }

    private val _CartShopping = MutableStateFlow<List<Product>>(emptyList())
    val CartShopping : StateFlow<List<Product>> = _CartShopping

    // Cargar todas las órdenes desde Firestore
    fun getOrdersFromFirestore() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val orders = orderRepository.getAllOrdersFromFirestore()
                _orders.value = orders
            } catch (e: Exception) {
                _messageConfirmation.value = "Error fetching orders: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getOrdersFromFirestore(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val orders = orderRepository.getOrdersFromFirestore(userId)
                _orders.value = orders
            } catch (e: Exception) {
                _messageConfirmation.value = "Error fetching orders: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Cargar las órdenes de un usuario específico
    fun loadOrders(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val orders = orderRepository.getOrdersFromFirestore(userId) // Pasa el userId al repositorio
                _orders.value = orders
            } catch (e: Exception) {
                _messageConfirmation.value = "Error loading orders: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadLastOrder(orderId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Aquí deberías obtener la orden por su ID del repositorio
                val order = orderRepository.getOrderById(orderId)
                if (order != null) {
                    _lastOrder.value = order  // Guardamos la última orden en el estado
                } else {
                    _messageConfirmation.value = "Order not found"
                }
            } catch (e: Exception) {
                _messageConfirmation.value = "Error loading order: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }




}