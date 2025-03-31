package com.example.tfg.repository

import android.util.Log
import com.example.tfg.models.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

class OrderRepository (private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
                       private val db: FirebaseFirestore = FirebaseFirestore.getInstance())  {

    private val nameCollection = "orders"

    suspend fun createOrderWithStockUpdate(order: Order): String {
        try {
            // Iniciar un batch para realizar varias escrituras de forma atómica
            val batch = db.batch()

            // Primero, realizamos las lecturas de stock de los productos
            for (product in order.products) {
                val productRef = db.collection("products").document(product.id)
                val productSnapshot = productRef.get().await()  // Leer el documento de producto

                val currentStock = productSnapshot.getLong("stock")?.toInt() ?: 0
                val newStock = currentStock - product.quantity

                // Verificar si hay suficiente stock
                if (newStock < 0) {
                    throw FirebaseFirestoreException("Stock insuficiente para ${product.name}", FirebaseFirestoreException.Code.ABORTED)
                }

                // Agregar la operación de actualización de stock al batch
                batch.update(productRef, "stock", newStock)
            }

            // Crear la orden en la colección de órdenes
            val orderRef = db.collection("orders").document(order.id)
            batch.set(orderRef, order)

            // Ejecutar todas las operaciones en el batch
            batch.commit().await() // Aquí es donde se guardan todos los cambios de manera atómica

            // Devolver el ID de la orden
            return order.id
        } catch (e: Exception) {
            throw Exception("Error al crear la orden y actualizar el stock: ${e.message}")
        }
    }


    suspend fun getOrdersFromFirestore(userId: String): List<Order> {
        return try {
            Log.d("OrderRepository", "Obteniendo órdenes para el usuario: $userId")
            val querySnapshot = db.collection(nameCollection)
                .whereEqualTo("userId", userId)
                .get()
                .await()
            val orders = querySnapshot.documents.mapNotNull { it.toObject(Order::class.java) }
            Log.d("OrderRepository", "Órdenes obtenidas: ${orders.size}")
            orders
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error al obtener órdenes: ${e.message}")
            throw Exception("Error fetching orders from Firestore: ${e.message}")
        }
    }

    suspend fun getAllOrdersFromFirestore(): List<Order> {
        return try {
            val querySnapshot = db.collection(nameCollection)
                .get()
                .await()
            querySnapshot.documents.mapNotNull { it.toObject(Order::class.java) }
        } catch (e: Exception) {
            throw Exception("Error fetching all orders from Firestore: ${e.message}")
        }
    }

    suspend fun getOrderById(orderId: String): Order? {
        return try {
            val snapshot = db.collection("orders").document(orderId).get().await()
            snapshot.toObject(Order::class.java)
        } catch (e: Exception) {
            throw Exception("Error fetching order: ${e.message}")
        }
    }

}


