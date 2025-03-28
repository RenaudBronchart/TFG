package com.example.tfg.repository

import com.example.tfg.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductRepositoryImpl(private val auth: FirebaseAuth, private val db: FirebaseFirestore) : ProductRepository {

    private val collectionName = "productos"

    override suspend fun getProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val querySnapshot = db.collection(collectionName).get().await()

        for (document in querySnapshot.documents) {
            val product = document.toObject(Product::class.java)
            if (product != null) {
                products.add(product)
            }
        }
        return products
    }

    override suspend fun getProductById(uid: String): Product? {
        return try {
            val document = db.collection(collectionName).document(uid).get().await()
            document.toObject(Product::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addProduct(product: Product): Boolean {
        return try {
            db.collection(collectionName).document(product.id).set(product).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun updateProduct(productId: String, product: Product): Boolean {
        return try {
            val updates = mapOf(
                "name" to product.name,
                "price" to product.price,
                "description" to product.description,
                "category" to product.category,
                "image" to product.image,
                "stock" to product.stock,
                "brand" to product.brand
            )
            db.collection(collectionName).document(productId).update(updates).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteProduct(productId: String): Boolean {
        return try {
            db.collection(collectionName).document(productId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}

