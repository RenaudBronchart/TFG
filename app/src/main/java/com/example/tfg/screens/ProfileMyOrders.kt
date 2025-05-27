package com.example.tfg.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.components.CardOrderProfile
import com.example.tfg.components.FilterButton

import com.example.tfg.components.TopBarComponent
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.OrderViewModel

/**
 * Pantalla que muestra todas las compras realizadas por el usuario actual.
 * Permite filtrar por fecha, nombre del producto o precio, y alternar el orden ascendente/descendente.
 */
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProfileMyOrders(navHostController: NavHostController, authViewModel: AuthViewModel, orderViewModel: OrderViewModel) {
    val orders by orderViewModel.orders.collectAsState()
    val userId = authViewModel.currentUserId.value ?: ""
    var filter by remember { mutableStateOf("fecha") }
    var isAscending by remember { mutableStateOf(true)}

    // Ordena las órdenes según el filtro y orden seleccionados (fecha, nombre, precio)

    val sortedOrders = when (filter) {
        "fecha" -> if (isAscending) { orderViewModel.orders.value.sortedBy { it.createdAt }
                     } else { orderViewModel.orders.value.sortedByDescending { it.createdAt } }

        "nombre" -> if (isAscending) { orderViewModel.orders.value.sortedBy { it.products.firstOrNull()?.name ?: "" }
                     } else { orderViewModel.orders.value.sortedByDescending { it.products.firstOrNull()?.name ?: "" } }

        "precio" -> if (isAscending) { orderViewModel.orders.value.sortedBy { it.totalAmount }
        }           else { orderViewModel.orders.value.sortedByDescending { it.totalAmount } }

        else -> orderViewModel.orders.value
    }
// Carga las órdenes del usuario solo una vez al obtener su ID
    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            orderViewModel.loadOrders(userId)
        }
    }
    Scaffold(
        topBar = { TopBarComponent("Mis compras", navHostController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Fila con botones para seleccionar el tipo de filtro y botón para alternar orden ascendente/descendente
            Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                FilterButton(text = "Fecha", onClick  = { filter = "fecha" })
                FilterButton(text = "Nombre", onClick  = { filter = "nombre" })
                FilterButton(text = "Precio", onClick  = { filter = "precio" })

                IconButton(onClick = { isAscending = !isAscending }) {
                    Icon(
                        imageVector = if (isAscending) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                        contentDescription = "Ordenar"
                    )
                }
            }
            // Lista de órdenes filtradas y ordenadas, mostradas como tarjetas
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {


                items(sortedOrders) { order ->
                    CardOrderProfile(order = order)
                }
            }
        }
    }
}