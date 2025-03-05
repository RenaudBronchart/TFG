package com.example.tfg.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tfg.models.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartShoppingViewModel : ViewModel() {

    private val _CartShopping = MutableStateFlow<List<Producto>>(emptyList())
    val CartShopping : StateFlow<List<Producto>> = _CartShopping

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

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

    fun removeToCart(producto: Producto){
        val newList = _CartShopping.value.toMutableList()
        newList.remove(producto)
        _CartShopping.value = newList
        Log.d("CartShoping", " Producto eleminado: ${producto.nombre}")
    }

    fun clearCart() {
        _CartShopping.value = emptyList()
        Log.d("CartShopping", "vacio")
    }

    fun increaseQuantity(producto: Producto){
        val productosActualizados = CartShopping.value.map { // map va a recorer toda  la lista
            if( it.id == producto.id) {

                if(it.quantity < it.stock) { // ver si cantidad menor o no
                    it.copy (quantity = it.quantity + 1 )  // si es menor se augmenta
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