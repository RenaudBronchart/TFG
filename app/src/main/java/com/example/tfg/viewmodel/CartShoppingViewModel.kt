package com.example.tfg.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.tfg.models.Order
import com.example.tfg.models.Producto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CartShoppingViewModel : ViewModel() {

    private val _CartShopping = MutableStateFlow<List<Producto>>(emptyList())
    val CartShopping : StateFlow<List<Producto>> = _CartShopping

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val name_collection = "orders"

    // emptyList() se usa para inicializar la lista como vacía
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _lastOrderId = MutableStateFlow<String?>(null)
    val lastOrderId: StateFlow<String?> = _lastOrderId

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> = _messageConfirmation

    fun setMessageConfirmation(message: String) {
        _messageConfirmation.value = message
    }

    fun addToCart(producto: Producto) {
        val newList = _CartShopping.value.toMutableList()
        // ver si hay un producto que existe o no
        val existingProduct = newList.find { it.id == producto.id }

        if (existingProduct != null) { // si ya existe
            if (existingProduct.quantity < producto.stock) { // verificar la cantidad menos que stock
                val updatedProduct = existingProduct.copy(quantity = existingProduct.quantity + 1)
                val index = newList.indexOf(existingProduct)
                newList[index] = updatedProduct
            } else {
                // cantidad igual o mayor, no se anade
                _errorMessage.value = "El stock está limitado a ${producto.stock} unidades."
                return
            }
        } else {
            // se anade normalmente
            newList.add(producto.copy(quantity = 1))
        }
        _CartShopping.value = newList
        Log.d("CartShopping", "Producto agregado: ${producto.nombre}")
    }

    fun createOrder(authViewModel: AuthViewModel, onOrderCreated: () -> Unit) {
        val userId = authViewModel.currentUserId.value ?: ""
        val totalAmount = calcularTotal()

        if (_CartShopping.value.isEmpty()) {
            _messageConfirmation.value = "No se puede comprar sin productos"
            return
        }

        // creamos order
        val order = Order(
            userId = userId,
            products = CartShopping.value, // lista productos del carrito
            totalAmount = totalAmount,
            createdAt = System.currentTimeMillis().toString()
        )

        viewModelScope.launch {
            _isLoading.value = true
            try {
                db.collection(name_collection)
                    .document(order.id)
                    .set(order)
                    .await()
                _messageConfirmation.value  = "Compra realizada con éxito!!"
                _lastOrderId.value = order.id // se asigna id de order creado
                onOrderCreated() // para ejecutar el calllbacka
            } catch (e: Exception) {
                _messageConfirmation.value = "Error al realizar la compra: ${e.message ?: "Desconocido"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

        fun removeToCart(producto: Producto) {
            val newList = _CartShopping.value.toMutableList()
            newList.remove(producto)
            _CartShopping.value = newList
            Log.d("CartShoping", " Producto eleminado: ${producto.nombre}")
        }

        fun clearCart() {
            _CartShopping.value = emptyList()
            Log.d("CartShopping", "vacio")
        }

        fun increaseQuantity(producto: Producto) {
            val productosActualizados = CartShopping.value.map { // map va a recorer toda  la lista
                if (it.id == producto.id) {

                    if (it.quantity < it.stock) { // ver si cantidad menor o no
                        it.copy(quantity = it.quantity + 1)  // si es menor se augmenta
                    } else {
                        // se muestra un mensaje si no tetemos mas stock
                        _errorMessage.value = "Le stock est limité à ${it.stock} unités."
                        it // Ne se aumenta la cantidad
                    }
                } else {
                    it // si id no es el mismo, se deja asi sin cambios
                }
            }
            _CartShopping.value = productosActualizados // actualizamos la lista de productos
        }


    fun decreaseQuantity(producto: Producto){
        val productosActualizados = CartShopping.value.map { // map va a recorer toda  la lista
            if( it.id == producto.id && it.quantity > 1) { //.it representa cada producto  para ver si mismo producto
                it.copy(quantity = it.quantity - 1 ) // metodo para copiar un objeto y solo unas propiedades
            } else  {
                it // si el ID no corresponde, guardamos lo que tenemos ahora
            }
        }
        _CartShopping.value = productosActualizados
    }

    fun calcularTotal(): Double {
        return CartShopping.value.sumOf { it.precio * it.quantity }
    }

}