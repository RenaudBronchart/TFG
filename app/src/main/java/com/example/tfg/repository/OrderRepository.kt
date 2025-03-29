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
        return try {
            db.runTransaction { transaction ->
                val orderId = order.id

                // 1️⃣ Guardar la orden
                val orderRef = db.collection("orders").document(orderId)
                transaction.set(orderRef, order)

                // 2️⃣ Actualizar el stock de cada producto
                order.products.forEach { product ->
                    val productRef = db.collection("products").document(product.id)
                    val snapshot = transaction.get(productRef)
                    val currentStock = snapshot.getLong("stock")?.toInt() ?: 0

                    val newStock = currentStock - product.quantity
                    if (newStock >= 0) {
                        transaction.update(productRef, "stock", newStock)
                    } else {
                        throw FirebaseFirestoreException(
                            "Stock insuficiente para ${product.name}",
                            FirebaseFirestoreException.Code.ABORTED
                        )
                    }
                }

                orderId // Retornamos el ID de la orden si todo salió bien
            }.await()
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
            val documentSnapshot = db.collection(nameCollection).document(orderId).get().await()
            documentSnapshot.toObject(Order::class.java)
        } catch (e: Exception) {
            throw Exception("Error fetching order with ID $orderId: ${e.message}")
        }
    }

    suspend fun createOrder(order: Order): Boolean {
        return try {
            db.runTransaction { transaction ->
                for (product in order.products) {
                    val productRef = db.collection("products").document(product.id)
                    val snapshot = transaction.get(productRef)

                    val currentStock = snapshot.getLong("stock")?.toInt() ?: 0
                    val newStock = currentStock - product.quantity

                    if (newStock < 0) {
                        throw FirebaseFirestoreException(
                            "Stock insuficiente para ${product.name}",
                            FirebaseFirestoreException.Code.ABORTED
                        )
                    }

                    transaction.update(productRef, "stock", newStock)
                }

                val orderRef = db.collection("orders").document(order.id)
                transaction.set(orderRef, order)
            }.await()

            true // ✅ Pedido creado correctamente
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error al crear la orden: ${e.message}")
            false // ❌ Error en la compra
        }
    }

}


