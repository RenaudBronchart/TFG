package com.example.tfg.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EditProductViewModel: ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products


    private val db = FirebaseFirestore.getInstance()
    private val name_collection = "productos"

    private val _product = MutableStateFlow(Product())
    val product: StateFlow<Product> = _product

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> get() =  _messageConfirmation

    private var currentProductId: String? = null  // Evitar recargar datos innecesariamente

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getProductosFromFirestore() {
        viewModelScope.launch {
            // almacenar el nombre de la colección

            val query = db.collection(name_collection).get().await()

            val products = mutableListOf<Product>()

            for (document in query.documents) {
                val product = document.toObject(Product::class.java)
                if (product != null) {
                    products.add(product)
                }
            }
            _products.value = products
        }
    }


    fun loadProduct(productId: String) {
        if (currentProductId == productId) return  // Evitar recargar si ya tenemos los datos correctos
        currentProductId = productId

        viewModelScope.launch {
            try {
                val document = db.collection(name_collection).document(productId).get().await()
                val product = document.toObject(Product::class.java)
                if (product != null) {
                    _product.value = product
                } else {
                    Log.e("EditProductViewModel", "Error: Producto no encontrado")
                }

            } catch (e: Exception) {
                Log.e("EditProductViewModel", "Error al cargar producto: ${e.message}")
            }
        }
    }

    fun updateProduct(productId: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val product = product.value
                val productUpdates = mapOf(
                    "name" to product.name,
                    "price" to product.price,
                    "description" to product.description,
                    "category" to product.category,
                    "image" to product.image,
                    "stock" to product.stock,
                    "brand" to product.brand
                )

                db.collection("productos").document(productId).update(productUpdates).await()
                val successMessage = "Producto actualizado correctamente"
                getProductosFromFirestore()
                _messageConfirmation.value = successMessage
                onSuccess(successMessage)
            } catch (exception: Exception) {
                val errorMessage = "Error al actualizar producto: ${exception.message}"
                _messageConfirmation.value = errorMessage
                onSuccess(errorMessage)
            } finally {
                _isLoading.value = false  // desactivamos el loading
            }
        }
    }


    // Métodos para actualizar los valores del producto
    fun setName(value: String) { _product.update { it.copy(name = value) } }
    fun setPrice(value: Double) { _product.update { it.copy(price = value) } }
    fun setDescription(value: String) { _product.update { it.copy(description = value) } }
    fun setCategory(value: String) { _product.update { it.copy(category = value) } }
    fun setImage(value: String) { _product.update { it.copy(image = value) } }
    fun setStock(value: Int) { _product.update { it.copy(stock = value) } }
    fun setBrand(value: String) { _product.update { it.copy(brand = value) } }

    fun setMessageConfirmation(message: String) {
        _messageConfirmation.value = message
    }

    fun resetMessage() {
        _messageConfirmation.value = ""
    }



}