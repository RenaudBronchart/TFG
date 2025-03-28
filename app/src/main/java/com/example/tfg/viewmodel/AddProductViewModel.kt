package com.example.tfg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.Product
import com.example.tfg.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddProductViewModel : ViewModel() {
    private val productRepository: ProductRepository = ProductRepository()


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> = _messageConfirmation

    // Añadir un producto
    fun addProduct(product: Product) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = productRepository.addProduct(product)
                if (success) {
                    _messageConfirmation.value = "Producto añadido correctamente"
                } else {
                    _messageConfirmation.value = "Error al añadir producto"
                }
            } catch (e: Exception) {
                _messageConfirmation.value = "Error al añadir producto: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}