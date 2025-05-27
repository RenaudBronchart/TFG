package com.example.tfg.repository

import android.net.Uri
import com.example.tfg.models.Product
// Interfaz que define las operaciones relacionadas con productos.
// Es implementada por clases como ProductRepository para acceder a Firestore o Storage.
// Permite abstraer la lógica de datos del ViewModel, facilitando pruebas y cambios de fuente de datos.
interface IProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun getProductById(uid: String): Product?
    suspend fun addProduct(product: Product): Result<String>
    suspend fun updateProduct(productId: String, product: Product): Boolean
    suspend fun deleteProductById(productId: String): Boolean
    suspend fun uploadImageToStorage(imageUri: Uri): Result<String>
}