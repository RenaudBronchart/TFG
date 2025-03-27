package com.example.tfg.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProductViewModel: ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val name_collection = "productos"

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

    init {
        getProductosFromFirestore()
    }



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



    fun addProduct(name: String, price: Double, description: String, category: String, image: String, stock: Int, brand: String, onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val product = Product(
                    name = name,
                    price = price,
                    description = description,
                    category = category,
                    image = image,
                    stock = stock,
                    brand = brand
                )

                // Utilisation de `await()` pour garder tout en coroutines
                db.collection(name_collection)
                    .document(product.id)
                    .set(product)
                    .await()

                val confirmationMessage = "Producto añadido correctamente"
                _messageConfirmation.value = confirmationMessage
                onSuccess(confirmationMessage)
            } catch (exception: Exception) {
                val confirmationMessage = "No se ha podido añadir un producto: ${exception.message}"
                _messageConfirmation.value = confirmationMessage
                onSuccess(confirmationMessage)
            }
        }
    }

    fun deleteProduct(productId: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true // Indique que l'opération est en cours
            try {
                db.collection(name_collection)
                    .document(productId)
                    .delete()
                    .await()
                val successMessage = "Producto eliminado correctamente"
                getProductosFromFirestore() // Rafraîchit la liste des produits
                _messageConfirmation.value = successMessage
                onSuccess(successMessage)
            } catch (exception: Exception) {
                _messageConfirmation.value = "No se ha podido eliminar el producto: ${exception.message}"
                onSuccess(_messageConfirmation.value)
            } finally {
                _isLoading.value = false // Désactive l'état de chargement
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