package com.example.tfg.repository

import android.util.Log
import com.example.tfg.models.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OrderRepository (private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
                       private val db: FirebaseFirestore = FirebaseFirestore.getInstance())  {

    private val nameCollection = "orders"

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
            val documentSnapshot = db.collection(nameCollection).document(orderId).get().await()
            documentSnapshot.toObject(Order::class.java)
        } catch (e: Exception) {
            throw Exception("Error fetching order with ID $orderId: ${e.message}")
        }
    }

    suspend fun createOrder(order: Order): String {
        return try {
            val orderId = order.id
            db.collection(nameCollection).document(orderId).set(order).await()
            orderId
        } catch (e: Exception) {
            throw Exception("Error creating order: ${e.message}")
        }
    }
}


