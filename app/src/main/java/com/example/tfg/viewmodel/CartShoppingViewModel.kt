package com.example.tfg.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.Order
import com.example.tfg.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CartShoppingViewModel : ViewModel() {

    private val _CartShopping = MutableStateFlow<List<Product>>(emptyList())
    val CartShopping : StateFlow<List<Product>> = _CartShopping

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val name_collection = "orders"
    private val name_collection2 = "productos"

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
    private val _isOrderInProgress = MutableStateFlow(false)
    val isOrderInProgress: StateFlow<Boolean> = _isOrderInProgress


    fun addToCart(product: Product) {
        // _CartShopping.value contiente la lista de productos y hacemos una copia para modifiar
        val newList = _CartShopping.value.toMutableList()
        // ver si hay un producto que existe o no
        val existingProduct = newList.find { it.id == product.id }
    // si  existe un producto seguimos sino vamos al else y anademos
        if (existingProduct != null) {
            // verificar la cantidad menos que stock sino vamos a else y indicamos outstock del producto
            if (existingProduct.quantity < product.stock) {
                // cantidad ok, podemos copiar el producto y anadir uno mas
                val updatedProduct = existingProduct.copy(quantity = existingProduct.quantity + 1)
                // vamos a poder reemplzar el producto actual con el nuevo y la nueva cantidad
                val index = newList.indexOf(existingProduct)
                newList[index] = updatedProduct
            } else {
                // cantidad igual o mayor, no se anade
                _errorMessage.value = "El stock está limitado a ${product.stock} unidades."
                return
            }
        } else {
            // se anade normalmente
            newList.add(product.copy(quantity = 1))
        }
        // actualizamos la newlist
        _CartShopping.value = newList
        Log.d("CartShopping", "Producto agregado: ${product.name}")
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

        // operacion asincronoa con firestore
        viewModelScope.launch {
            _isLoading.value = true
            try {
                db.collection(name_collection)
                    .document(order.id)
                    .set(order)
                    .await()
                Log.d("CartShopping", "Mise à jour du stock après la commande.")
                 // actualizamos el stock de los productos en firebase gracias a la funcion
                updateStockAfterOrder()

                _messageConfirmation.value  = "Compra realizada con éxito!!"
                _lastOrderId.value = order.id // se asigna id de order creado
                onOrderCreated() // para ejecutar el calllback y navigar a una pagina
            } catch (e: Exception) {
                _messageConfirmation.value = "Error al realizar la compra: ${e.message ?: "Desconocido"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateStockAfterOrder() {
        //sin viewModelScope.launch, el stock no se actualiza correctemente, problema con await
        //  viewModelScope es como " un hilo ",   viewModelScope.launch crea una coroutine
        // que espera que la bdd termine ac actualizar antes de continuar el codigo
        viewModelScope.launch {
            CartShopping.value.forEach { producto ->
                try {
                    val newStock = producto.stock - producto.quantity
                    Log.d("CartShopping", "Produit: ${producto.name}, Stock avant mise à jour: ${producto.stock}, Quantité commandée: ${producto.quantity}, Nouveau stock calculé: $newStock")
                    if (newStock >= 0 ) {
                        db.collection(name_collection2)
                            .document(producto.id)
                            .update("stock", newStock)
                            .await()
                        Log.d("CartShopping", "Stock mis à jour pour ${producto.name}: $newStock")
                    } else {
                        _messageConfirmation.value = "Stock insuficiente para ${producto.name}"
                    }
                } catch (e: Exception) {
                    _messageConfirmation.value = "Error al actualizar stock: ${e.message}"
                }

            }

        }

    }

        fun removeToCart(product: Product) {
            val newList = _CartShopping.value.toMutableList()
            newList.remove(product)
            _CartShopping.value = newList
            Log.d("CartShoping", " Producto eleminado: ${product.name}")
        }

        fun clearCart() {
            _CartShopping.value = emptyList()
            Log.d("CartShopping", "vacio")
        }

        fun increaseQuantity(product: Product) {
            val productosActualizados = CartShopping.value.map { // map va a recorer toda  la lista
                if (it.id == product.id) {

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


    fun decreaseQuantity(product: Product){
        val productosActualizados = CartShopping.value.map { // map va a recorer toda  la lista
            if( it.id == product.id && it.quantity > 1) { //.it representa cada producto  para ver si mismo producto
                it.copy(quantity = it.quantity - 1 ) // metodo para copiar un objeto y solo unas propiedades
            } else  {
                it // si el ID no corresponde, guardamos lo que tenemos ahora
            }
        }
        _CartShopping.value = productosActualizados
    }

    fun calcularTotal(): Double {
        return CartShopping.value.sumOf { it.price * it.quantity }
    }

}