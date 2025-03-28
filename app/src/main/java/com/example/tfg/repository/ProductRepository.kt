package com.example.tfg.repository

import com.example.tfg.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductRepository(    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
                            private val db: FirebaseFirestore = FirebaseFirestore.getInstance())  {

    private val collectionName = "productos"

     suspend fun getProducts(): List<Product> {
        return try {
            val querySnapshot = db.collection(collectionName).get().await()
            // Utilizamos mapNotNull para convertir los documentos y eliminar automáticamente los nulos
            querySnapshot.documents.mapNotNull { it.toObject(Product::class.java) }
        } catch (e: Exception) {
            emptyList() // Si hay error, devolvemos una lista vacía
        }
    }

     suspend fun getProductById(uid: String): Product? {
        return try {
            val document = db.collection(collectionName).document(uid).get().await()
            document.toObject(Product::class.java)
        } catch (e: Exception) {
            null
        }
    }

     suspend fun addProduct(product: Product): Boolean {
        return try {
            db.collection(collectionName).document(product.id).set(product).await()
            true
        } catch (e: Exception) {
            false
        }
    }

     suspend fun updateProduct(productId: String, product: Product): Boolean {
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

     suspend fun deleteProductById(productId: String): Boolean {
        return try {
            db.collection(collectionName).document(productId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}

