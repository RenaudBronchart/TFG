package com.example.tfg

import com.example.tfg.models.Product
import com.example.tfg.repository.IProductRepository

class FakeProductRepository : IProductRepository {
    override suspend fun getProducts(): List<Product> = emptyList()
    override suspend fun getProductById(uid: String): Product? = null
    override suspend fun addProduct(product: Product): Result<String> = Result.success("Produit ajouté (fake)")
    override suspend fun updateProduct(productId: String, product: Product): Boolean = true
    override suspend fun deleteProductById(productId: String): Boolean = true
    override suspend fun uploadImageToStorage(imageUri: android.net.Uri): Result<String> = Result.success("url_fausse")
}
