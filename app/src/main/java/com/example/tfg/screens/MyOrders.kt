package com.example.tfg.screens

import android.annotation.SuppressLint
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.test.espresso.util.filter
import com.example.tfg.components.CardOrderProfile
import com.example.tfg.components.FilterButton

import com.example.tfg.components.TopBarComponent
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.OrderViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MyOrders(navHostController: NavHostController, authViewModel: AuthViewModel, orderViewModel: OrderViewModel) {
    val userId = authViewModel.currentUserId.value ?: ""
    var filter by remember { mutableStateOf("fecha") }
    var isAscending by remember { mutableStateOf(true)}

    val sortedOrders = when (filter) {
        "fecha" -> if (isAscending) { orderViewModel.orders.value.sortedBy { it.createdAt }
                     } else { orderViewModel.orders.value.sortedByDescending { it.createdAt } }

        "nombre" -> if (isAscending) { orderViewModel.orders.value.sortedBy { it.products.firstOrNull()?.nombre ?: "" }
                     } else { orderViewModel.orders.value.sortedByDescending { it.products.firstOrNull()?.nombre ?: "" } }

        "precio" -> if (isAscending) { orderViewModel.orders.value.sortedBy { it.totalAmount }
        }           else { orderViewModel.orders.value.sortedByDescending { it.totalAmount } }

        else -> orderViewModel.orders.value
    }

    // Cargar
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
            // creacion bara filtros
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