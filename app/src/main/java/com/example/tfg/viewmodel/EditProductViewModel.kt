package com.example.tfg.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.Producto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EditProductViewModel: ViewModel() {




    private val db = FirebaseFirestore.getInstance()
    private val name_collection = "productos"

    private val _producto = MutableStateFlow(Producto())
    val producto: StateFlow<Producto> = _producto

    private val _messageConfirmation = MutableStateFlow("")
    val messageConfirmation: StateFlow<String> get() =  _messageConfirmation

    private var currentProductId: String? = null  // Evitar recargar datos innecesariamente

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun loadProduct(productId: String) {
        if (currentProductId == productId) return  // Evitar recargar si ya tenemos los datos correctos
        currentProductId = productId

        viewModelScope.launch {
            try {
                val document = db.collection(name_collection).document(productId).get().await()
                val product = document.toObject(Producto::class.java)
                if (product != null) {
                    _producto.value = product
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
            _isLoading.value = true  // decimos que loading es verdad

            try {
                val product = producto.value
                val productUpdates = mapOf(
                    "nombre" to product.nombre,
                    "precio" to product.precio,
                    "descripcion" to product.descripcion,
                    "categoria" to product.categoria,
                    "imagen" to product.imagen,
                    "stock" to product.stock,
                    "marca" to product.marca
                )

                db.collection("productos").document(productId).update(productUpdates).await()
                delay(2000)
                val successMessage = "Producto actualizado correctamente"
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
    fun setNombre(value: String) { _producto.update { it.copy(nombre = value) } }
    fun setPrecio(value: Double) { _producto.update { it.copy(precio = value) } }
    fun setDescripcion(value: String) { _producto.update { it.copy(descripcion = value) } }
    fun setCategoria(value: String) { _producto.update { it.copy(categoria = value) } }
    fun setImagen(value: String) { _producto.update { it.copy(imagen = value) } }
    fun setStock(value: Int) { _producto.update { it.copy(stock = value) } }
    fun setMarca(value: String) { _producto.update { it.copy(marca = value) } }

    fun setMessageConfirmation(message: String) {
        _messageConfirmation.value = message
    }

    fun resetMessage() {
        _messageConfirmation.value = ""
    }



}