package com.example.tfg.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import com.example.tfg.components.TopBarComponent
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tfg.components.CardCheckoutShopping
import com.example.tfg.components.TotalToPay
import com.example.tfg.viewmodel.CartShoppingViewModel

@Composable
fun CheckoutShopping(navHostController: NavHostController, cartShoppingViewModel: CartShoppingViewModel) {
    val productos by cartShoppingViewModel.CartShopping.collectAsState()

    Scaffold(
        topBar = { TopBarComponent ("Tu pedido", navHostController )},
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(productos) {index, producto ->
                    CardCheckoutShopping(navHostController, cartShoppingViewModel,producto)
                    if (index < productos.size - 1) {
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }
            }
            TotalToPay(navHostController, cartShoppingViewModel,productos)
        }
    }
}