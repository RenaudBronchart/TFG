package com.example.tfg.repository

import com.example.tfg.models.Product


interface ProductRepository {

    suspend fun getProducts() :  List<Product>
    suspend fun getProductById(productId: String): Product?
    suspend fun addProduct(product: Product) :  Boolean
    suspend fun updateProduct(productId: String, product: Product): Boolean
    suspend fun deleteProduct(productId: String): Boolean
}