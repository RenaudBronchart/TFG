package com.example.tfg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeleteProductViewModel : ViewModel() {
    private val productRepository: ProductRepository = ProductRepository()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> = _messageConfirmation

    // Eliminar un producto
    fun deleteProduct(productId: String, onSuccess: (String) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            val success = productRepository.deleteProductById(productId)
            val message = if (success) {
                "Producto eliminado correctamente"
            } else {
                "Error al eliminar el producto"
            }
            _messageConfirmation.value = message
            onSuccess(message)
            _isLoading.value = false
        }
    }
}