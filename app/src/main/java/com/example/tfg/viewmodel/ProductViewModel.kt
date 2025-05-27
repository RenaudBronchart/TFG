package com.example.tfg.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.Product
import com.example.tfg.repository.IProductRepository
import com.example.tfg.repository.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// ViewModel responsable de gestionar la visualización y edición de productos.
// Maneja el estado del formulario, la validación y la carga de productos desde el repositorio.
class ProductViewModel(
    private val productRepository: IProductRepository = ProductRepository()
) : ViewModel() {

    private val _name = MutableStateFlow<String>("")
    val name: StateFlow<String> = _name //

    private val _price = MutableStateFlow<Double>(0.0)
    val price: MutableStateFlow<Double> = _price

    private val _description = MutableStateFlow<String>("")
    val description: StateFlow<String> = _description

    private val _category = MutableStateFlow<String>("")
    val category: StateFlow<String> = _category

    private val _image = MutableStateFlow<String>("")
    val image: StateFlow<String> = _image

    private val _stock = MutableStateFlow<Int>(0)
    val stock: StateFlow<Int> = _stock

    private val _brand = MutableStateFlow<String>("")
    val brand: StateFlow<String> = _brand

    // MutableStateFlow es un flujo reactivo de Kotlin que siempre mantiene un valor actual
    // A diferencia de LiveData, está diseñado para funcionar de forma óptima con coroutines.
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    // StateFlow es más eficiente para manejar  cambios de manera reactiva
    val products: StateFlow<List<Product>> = _products

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _isButtonEnable = MutableStateFlow(false)
    val isButtonEnable: StateFlow<Boolean> = _isButtonEnable

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> = _messageConfirmation

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    // Cargar todos los productos desde Firestore a través del repositorio
    fun getProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _products.value = productRepository.getProducts()
            } catch (e: Exception) {
                _messageConfirmation.value = "Error al cargar productos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }


    // Actualiza los campos del formulario con los valores dados y valida si se puede habilitar el botón
    fun onCompletedFields(name: String, price: Double, description: String, category: String, image: String, stock: Int, brand: String) {
        _name.value = name
        _price.value = price
        _description.value = description
        _category.value = category
        _image.value = image
        _stock.value = stock
        _brand.value = brand
        _isButtonEnable.value = enableButton(name, price, description, category, image, stock, brand)

    }
    // Valida si todos los campos están completos y correctos
    fun enableButton(name: String, price: Double, description: String, category: String, image: String, stock: Int, brand: String) =
        name.isNotEmpty() && price > 0 && description.isNotEmpty() && category.isNotEmpty() && image.isNotEmpty() && stock > 0 && brand.isNotEmpty()
    // Reinicia todos los campos del formulario a sus valores por defecto
    fun resetFields() {
        _name.value = ""
        _price.value = 0.0
        _description.value = ""
        _category.value = "Selecciona una categoría"
        _image.value = ""
        _stock.value = 0
        _brand.value = ""
        _isButtonEnable.value = false // Pour désactiver le bouton après la soumission
    }

    fun setMessageConfirmation(message: String) {
        _messageConfirmation.value = message
    }
    // Actualiza solo la imagen y vuelve a validar los campos
    fun updateImage(newImageUrl: String) {
        _image.value = newImageUrl
        _isButtonEnable.value = enableButton(
            _name.value,
            _price.value,
            _description.value,
            _category.value,
            _image.value,
            _stock.value,
            _brand.value
        )
    }


}