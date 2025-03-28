package com.example.tfg.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.Product
import com.example.tfg.repository.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

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


    fun onCompletedFields(name: String, price: Double, description: String, category: String, image: String, stock: Int, brand: String) {
        _name.value = name
        _price.value = price
        _description.value = description
        _category.value = category
        _image.value = image
        _stock.value = stock
        _brand.value = brand
        _isButtonEnable.value = enableButton(name, price, description, category, image, stock, brand)

        Log.d("ButtonEnabled", "isButtonEnable: ${_isButtonEnable.value}")
    }

    fun enableButton(name: String, price: Double, description: String, category: String, image: String, stock: Int, brand: String) =
        name.isNotEmpty() && price > 0 && description.isNotEmpty() && category.isNotEmpty() && image.isNotEmpty() && stock > 0 && brand.isNotEmpty()

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




}