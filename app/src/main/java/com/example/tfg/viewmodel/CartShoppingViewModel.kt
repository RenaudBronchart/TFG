package com.example.tfg.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tfg.models.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartShoppingViewModel : ViewModel() {

    private val _CartShopping = MutableStateFlow<List<Producto>>(emptyList())
    val CartShopping : StateFlow<List<Producto>> = _CartShopping

    fun addToCart(producto: Producto){
        val newList = _CartShopping.value.toMutableList()
        newList.add(producto)
        _CartShopping.value = newList
        Log.d("CartShoping", " Producto agregado: ${producto.nombre}")
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

}