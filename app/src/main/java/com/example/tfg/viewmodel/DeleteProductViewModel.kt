package com.example.tfg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeleteProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> = _messageConfirmation

    // Eliminar un producto
    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = productRepository.deleteProduct(productId)
                if (success) {
                    _messageConfirmation.value = "Producto eliminado correctamente"
                } else {
                    _messageConfirmation.value = "Error al eliminar producto"
                }
            } catch (e: Exception) {
                _messageConfirmation.value = "Error al eliminar producto: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}