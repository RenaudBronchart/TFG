package com.example.tfg.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tfg.models.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductoViewModel: ViewModel() {

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> = _nombre // Live data en vez de MutableLiveData para que no se pueda modificar

    private val _precio = MutableLiveData<Double>()
    val precio: LiveData<Double> = _precio

    private val _descripcion = MutableLiveData<String>()
    val descripcion: LiveData<String> = _descripcion

    private val _categoria = MutableLiveData<String>()
    val categoria: LiveData<String> = _categoria

    private val _imagen = MutableLiveData<String>()
    val imagen: LiveData<String> = _imagen

    private val _stock = MutableLiveData<Int>()
    val stock: LiveData<Int> = _stock

    private val _marca = MutableLiveData<String>()
    val marca: LiveData<String> = _marca

    // MutableStateFlow es un flujo reactivo de Kotlin que siempre mantiene un valor actual
    // A diferencia de LiveData, está diseñado para funcionar de forma óptima con coroutines.
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())

    // StateFlow es más eficiente para manejar  cambios de manera reactiva
    val productos: StateFlow<List<Producto>> = _productos

    private val _isButtonEnable = MutableLiveData<Boolean>()
    val isButtonEnable: LiveData<Boolean> = _isButtonEnable

    fun onCompletedFields(nombre: String, precio: Double, descripcion: String, categoria: String, imagen: String, stock: Int, marca: String) {
        _nombre.value = nombre
        _precio.value = precio
        _descripcion.value = descripcion
        _categoria.value = categoria
        _imagen.value = imagen
        _stock.value = stock
        _marca.value = marca
        _isButtonEnable.value = enableButton(nombre, precio, descripcion, categoria, imagen, stock, marca)

        Log.d("ButtonEnabled", "isButtonEnable: ${_isButtonEnable.value}")
    }

    fun enableButton(nombre: String, precio: Double, descripcion: String, categoria: String, imagen: String, stock: Int, marca: String) =
         nombre.isNotEmpty() && precio > 0 && descripcion.isNotEmpty() && categoria.isNotEmpty() && imagen.isNotEmpty() && stock > 0 && marca.isNotEmpty()

    fun resetFields() {
        _nombre.value = ""
        _precio.value = 0.0
        _descripcion.value = ""
        _categoria.value = ""
        _imagen.value = ""
        _stock.value = 0
        _marca.value = ""
        _isButtonEnable.value = false // Pour désactiver le bouton après la soumission
    }
}