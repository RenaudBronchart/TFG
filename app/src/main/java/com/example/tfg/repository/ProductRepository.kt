package com.example.tfg.repository

import android.net.Uri
import com.example.tfg.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class ProductRepository(
private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : IProductRepository {

    private val collectionName = "products"
     override suspend fun getProducts(): List<Product> {
        return try {
            val querySnapshot = db.collection(collectionName).get().await()
            // Utilizamos mapNotNull para convertir los documentos y eliminar automáticamente los nulos
            querySnapshot.documents.mapNotNull { it.toObject(Product::class.java) }
        } catch (e: Exception) {
            emptyList() // Si hay error, devolvemos una lista vacía
        }
    }

     override suspend fun getProductById(uid: String): Product? {
        return try {
            val document = db.collection(collectionName).document(uid).get().await()
            document.toObject(Product::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addProduct(product: Product): Result<String> {
        return try {
            // Agregar el producto a Firestore, usando el id del producto como documento
            db.collection(collectionName).document(product.id).set(product).await()
            // Si la operación fue exitosa, devolvemos un resultado de éxito
            Result.success("Producto agregado correctamente")
        } catch (e: Exception) {
            // En caso de error, devolvemos un resultado de fallo con el mensaje de error
            Result.failure(e)
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

     override suspend fun deleteProductById(productId: String): Boolean {
        return try {
            db.collection(collectionName).document(productId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun uploadImageToStorage(imageUri: Uri): Result<String> {
        return try {
            val storageReference = FirebaseStorage.getInstance().reference
            val imageRef = storageReference.child("product_images/${System.currentTimeMillis()}.jpg")
            val uploadTask = imageRef.putFile(imageUri).await()
            val downloadUrl = imageRef.downloadUrl.await()
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}

