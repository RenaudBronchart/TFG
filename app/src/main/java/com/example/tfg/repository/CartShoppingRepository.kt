package com.example.tfg.repository

import com.example.tfg.models.Order
import com.example.tfg.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CartShoppingRepository (private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
                              private val db: FirebaseFirestore = FirebaseFirestore.getInstance())  {


    // Función suspend para agregar un producto al carrito

    suspend fun addProductToCart(product: Product, currentCart: List<Product>): List<Product> {
        // Creamos una copia del carrito actual y agregamos el producto
        val newCartList = currentCart.toMutableList()

        // Verificamos si el producto ya existe en el carrito
        val existingProduct = newCartList.find { it.id == product.id }

        if (existingProduct != null) {
            // Si el producto ya existe y hay stock suficiente, incrementamos la cantidad
            if (existingProduct.quantity < existingProduct.stock) {
                val updatedProduct = existingProduct.copy(quantity = existingProduct.quantity + 1)
                val index = newCartList.indexOf(existingProduct)
                newCartList[index] = updatedProduct
            } else {
                // Si no hay suficiente stock, lanzamos una excepción
                throw Exception("No hay suficiente stock para el producto ${product.name}")
            }
        } else {
            // Si el producto no existe en el carrito, lo agregamos con cantidad 1
            newCartList.add(product.copy(quantity = 1))
        }

        // Retornamos el carrito actualizado
        return newCartList
    }

    // Eliminar producto del carrito
    suspend fun removeProductFromCart(product: Product, currentCart: List<Product>): List<Product> {
        val newCartList = currentCart.toMutableList()
        newCartList.removeIf { it.id == product.id }
        return newCartList
    }

    // Vaciar el carrito
    suspend fun clearCart(): List<Product> {
        return emptyList()
    }

    // Aumentar cantidad de un producto en el carrito
    suspend fun increaseQuantity(product: Product, currentCart: List<Product>): List<Product> {
        val newCartList = currentCart.toMutableList()
        val existingProduct = newCartList.find { it.id == product.id }

        if (existingProduct != null && existingProduct.quantity < existingProduct.stock) {
            val updatedProduct = existingProduct.copy(quantity = existingProduct.quantity + 1)
            val index = newCartList.indexOf(existingProduct)
            newCartList[index] = updatedProduct
        }

        return newCartList
    }

    // Disminuir cantidad de un producto en el carrito
    suspend fun decreaseQuantity(product: Product, currentCart: List<Product>): List<Product> {
        val newCartList = currentCart.toMutableList()
        val existingProduct = newCartList.find { it.id == product.id }

        if (existingProduct != null && existingProduct.quantity > 1) {
            val updatedProduct = existingProduct.copy(quantity = existingProduct.quantity - 1)
            val index = newCartList.indexOf(existingProduct)
            newCartList[index] = updatedProduct
        }

        return newCartList
    }


}