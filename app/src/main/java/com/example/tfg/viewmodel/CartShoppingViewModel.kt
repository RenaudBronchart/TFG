package com.example.tfg.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.Order
import com.example.tfg.models.Product
import com.example.tfg.repository.CartShoppingRepository
import com.example.tfg.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CartShoppingViewModel : ViewModel() {

    private val cartShoppingRepository: CartShoppingRepository = CartShoppingRepository()
    private val orderRepository: OrderRepository = OrderRepository()

    private val _CartShopping = MutableStateFlow<List<Product>>(emptyList())
    val CartShopping : StateFlow<List<Product>> = _CartShopping

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _order = MutableStateFlow(Order())
    val order: StateFlow<Order> get() = _order

    // emptyList() se usa para inicializar la lista como vacía
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _lastOrderId = MutableStateFlow<String?>(null)
    val lastOrderId: StateFlow<String?> = _lastOrderId

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> = _messageConfirmation

    private val _orderSuccess = MutableStateFlow(false)
    val orderSuccess: StateFlow<Boolean> = _orderSuccess


    private val _isOrderInProgress = MutableStateFlow(false)
    val isOrderInProgress: StateFlow<Boolean> = _isOrderInProgress



    // Agregar producto al carrito
    fun addToCart(product: Product) {
        viewModelScope.launch {
            try {
                _CartShopping.value = cartShoppingRepository.addProductToCart(product, _CartShopping.value)
                _messageConfirmation.value = "Producto agregado: ${product.name}"
                Log.d("CartShopping", "Producto agregado: ${product.name}")
            } catch (e: Exception) {
                _errorMessage.value = "Error al agregar el producto: ${e.message}"
            }
        }
    }

    // Eliminar producto del carrito
    fun removeFromCart(product: Product) {
        viewModelScope.launch {
            _CartShopping.value = cartShoppingRepository.removeProductFromCart(product, _CartShopping.value)
        }
    }

    // Vaciar el carrito
    fun clearCart() {
        viewModelScope.launch {
            _CartShopping.value = cartShoppingRepository.clearCart()
        }
    }

    // Aumentar cantidad de un producto
    fun increaseQuantity(product: Product) {
        viewModelScope.launch {
            _CartShopping.value = cartShoppingRepository.increaseQuantity(product, _CartShopping.value)
        }
    }

    // Disminuir cantidad de un producto
    fun decreaseQuantity(product: Product) {
        viewModelScope.launch {
            _CartShopping.value = cartShoppingRepository.decreaseQuantity(product, _CartShopping.value)
        }
    }

    // Calcular el total de la compra
    fun calcularTotal(): Double {
        return CartShopping.value.sumOf { it.price * it.quantity }
    }

    fun createOrder(order: Order, onOrderCreated: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val success = orderRepository.createOrder(order)

            if (success) {
                _messageConfirmation.value = "Compra realizada con éxito!"
                _CartShopping.value = emptyList() // ✅ Vacía el carrito
                _orderSuccess.value = true // ✅ Notifica a la UI para navegar
                onOrderCreated() // ✅ Llamamos el callback
            } else {
                _messageConfirmation.value = "Error al realizar la compra"
            }

            _isLoading.value = false
        }
    }
}
