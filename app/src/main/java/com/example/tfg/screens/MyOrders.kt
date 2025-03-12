package com.example.tfg.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.components.CardOrderProfile

import com.example.tfg.components.TopBarComponent
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.OrderViewModel

@Composable
fun MyOrders(navHostController: NavHostController, authViewModel: AuthViewModel, orderViewModel: OrderViewModel) {
    val userId = authViewModel.currentUserId.value ?: ""

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
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // On affiche chaque commande ici
                items(orderViewModel.orders.value) { order ->
                    // Affichage de l'ordre
                    CardOrderProfile(order = order)
                }
            }
        }
    }
}