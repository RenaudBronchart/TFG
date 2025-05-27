package com.example.tfg.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.Product
import com.example.tfg.repository.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// ViewModel encargado de la edición de productos.
// Permite cargar un producto, modificar sus campos y actualizarlo en Firestore.
class EditProductViewModel(
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _product = MutableStateFlow(Product())
    val product: StateFlow<Product> = _product

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> get() =  _messageConfirmation

    private var currentProductId: String? = null  // Evitar recargar datos innecesariamente

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Carga todos los productos desde Firestore (útil para listas de selección o verificación)
    fun getProductsFromFirestore() {
        viewModelScope.launch {
            _products.value = productRepository.getProducts()
        }
    }
    // Carga un producto por su ID si aún no está cargado
    fun loadProduct(productId: String) {
        if (currentProductId == productId) return // Evitar recargar si ya tenemos los datos correctos
        currentProductId = productId

        viewModelScope.launch {
            try {
                val product = productRepository.getProductById(productId)
                if (product != null) {
                    _product.value = product
                } else {
                    _messageConfirmation.value = "Producto no encontrado"
                }
            } catch (e: Exception) {
                _messageConfirmation.value = "Error al cargar producto: ${e.message}"
            }
        }
    }
    // Actualiza un producto en Firestore con los nuevos valores del formulario
    fun updateProduct(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val product = _product.value.copy(id = productId) // Asegúrate de tener el id correcto en el producto
                val success = productRepository.updateProduct(product.id, product)
                if (success) {
                    _messageConfirmation.value = "Producto actualizado correctamente"
                } else {
                    _messageConfirmation.value = "Error al actualizar producto"
                }
            } catch (e: Exception) {
                _messageConfirmation.value = "Error al actualizar producto: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Métodos para actualizar los valores del producto
    fun updateProductField(field: String, value: Any) {
        _product.update { currentProduct ->
            currentProduct?.let {

                when (field) {
                    "name" -> it.copy(name = value as String)
                    "price" -> it.copy(price = value as Double)
                    "description" -> it.copy(description = value as String)
                    "category" -> it.copy(category = value as String)
                    "image" -> it.copy(image = value as String)
                    "stock" -> it.copy(stock = value as Int)
                    "brand" -> it.copy(brand = value as String)
                    else -> it // Si no se encuentra el campo, no hace nada
                }
            } ?: currentProduct
        }
    }

    fun setMessageConfirmation(message: String) {
        _messageConfirmation.value = message
    }

    fun resetMessage() {
        _messageConfirmation.value = ""
    }



}