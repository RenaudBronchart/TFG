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

    private val _fields = MutableStateFlow(ProductFormState())  // Aquí defines el estado de los campos del producto
    val fields: StateFlow<ProductFormState> = _fields

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
    // Actualiza los campos del formulario del producto
    fun onCompletedFields(productForm: ProductFormState) {
        _fields.value = productForm
    }

    // Resetea los campos después de registrar el producto
    fun resetFields() {
        _fields.value = ProductFormState()  // Resetea todos los campos a valores predeterminados
    }

    // Establecer mensaje de confirmación
    fun setMessageConfirmation(message: String) {
        _messageConfirmation.value = message
    }
}