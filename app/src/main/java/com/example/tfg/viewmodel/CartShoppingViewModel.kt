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

// ViewModel encargado de gestionar el carrito de compras, incluyendo operaciones sobre productos,
// creación de pedidos y actualización del stock.
class CartShoppingViewModel(
    private val cartShoppingRepository: CartShoppingRepository = CartShoppingRepository(),
    private val orderRepository: OrderRepository = OrderRepository()
) : ViewModel() {
    // Lista de productos actualmente en el carrito
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

    // Crea una orden con los productos del carrito y actualiza el stock
    fun createOrder(order: Order) {
        Log.d("CartShoppingViewModel", "Iniciando creación de la orden...") // Esto debería aparecer en los logs.
        viewModelScope.launch {
            try {
                // Crear la orden
                val orderId = orderRepository.createOrderWithStockUpdate(order)
                Log.d("CartShoppingViewModel", "Orden creada con éxito: $orderId")

                // Actualizar el lastOrderId
                _lastOrderId.value = orderId
                Log.d("CartShoppingViewModel", "lastOrderId actualizado a: ${_lastOrderId.value}")
            } catch (e: Exception) {
                Log.e("CartShoppingViewModel", "Error al procesar la orden: ${e.message}")
                _messageConfirmation.value = "Error al procesar la orden: ${e.message}"
            }
        }
    }




    fun setLastOrderId(orderId: String) {
        _lastOrderId.value = orderId
    }
}
