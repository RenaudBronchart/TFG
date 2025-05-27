package com.example.tfg.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.Product
import com.example.tfg.repository.IProductRepository
import com.example.tfg.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel responsable de manejar la lógica de negocio relacionada con la creación de productos.
// Gestiona los estados de los campos del formulario, validaciones, subida de imágenes y comunicación con el repositorio.
class AddProductViewModel(
    private val productRepository: IProductRepository = ProductRepository() //
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
    val messageConfirmation: StateFlow<String> get() = _messageConfirmation

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Añadir un producto
    fun addProduct(product: Product, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            // Aquí solo pasas el objeto `Product` al repository sin tener que crear un objeto dentro del ViewModel
            val result = productRepository.addProduct(product)

            result.onSuccess {
                _messageConfirmation.value = it
                onSuccess(it)
            }.onFailure { exception ->
                _messageConfirmation.value = "Error: ${exception.message}"
                onSuccess("Error al agregar producto")
            }

            resetFields() // Limpiar los campos después de agregar el producto
        }
    }

    // Actualiza los campos con los valores ingresados y verifica si se puede habilitar el botón
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
    // Valida que todos los campos estén correctamente completados para habilitar el botón
    fun enableButton(name: String, price: Double, description: String, category: String, image: String, stock: Int, brand: String) =
        name.isNotEmpty() && price > 0 && description.isNotEmpty() && category.isNotEmpty() && image.isNotEmpty() && stock > 0 && brand.isNotEmpty()

    // Resetear los campos después de agregar un producto
    fun resetFields() {
        _name.value = ""
        _price.value = 0.0
        _description.value = ""
        _category.value = "Selecciona una categoría"
        _image.value = ""
        _stock.value = 0
        _brand.value = ""
        _isButtonEnable.value = false // Desactivar el botón después del envío
    }

    // Función para confirmacion de mensaje
    fun setMessageConfirmation(message: String) {
        _messageConfirmation.value = message
    }

    // Llama al ViewModel principal del producto para actualizar la imagen mostrada en pantalla
    fun uploadImageAndSetUrl(imageUri: Uri, productViewModel: ProductViewModel) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = productRepository.uploadImageToStorage(imageUri)
                result.onSuccess { downloadUrl ->
                    _image.value = downloadUrl
                    _messageConfirmation.value = "Imagen subida correctamente"

                    productViewModel.updateImage(downloadUrl)
                }.onFailure { exception ->
                    _messageConfirmation.value = "Error al subir la imagen: ${exception.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}