package com.example.tfg.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.tfg.components.TopBarComponent
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tfg.components.CardEmptyShopping
import com.example.tfg.components.CardOrderCheckoutShopping
import com.example.tfg.components.TotalToPay
import com.example.tfg.models.Order
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.CartShoppingViewModel
import com.example.tfg.viewmodel.OrderViewModel

@Composable
fun CheckoutShopping(navHostController: NavHostController,authViewModel: AuthViewModel ,cartShoppingViewModel: CartShoppingViewModel) {
    val productos by cartShoppingViewModel.CartShopping.collectAsState()
    val order by cartShoppingViewModel.order.collectAsState()

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
            if(productos.isEmpty()) {
                CardEmptyShopping(navHostController)
            }else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    // con itemsIndexed, recogemos la lista con dos parametros
                    // index la posicion y producto
                    itemsIndexed(productos) {index, producto ->

                        CardOrderCheckoutShopping(navHostController, cartShoppingViewModel,producto)
                        if (index < productos.size - 1) { // se muestra la linea solo entre productos
                            HorizontalDivider(
                                thickness = 2.dp,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }
                    }
                }
            }

            TotalToPay(
                total = cartShoppingViewModel.calcularTotal(),
                onClickPay = {
                    val userId = authViewModel.currentUserId.value ?: ""
                    val totalAmount = cartShoppingViewModel.calcularTotal()
                    val order = Order(
                        userId = userId,
                        products = cartShoppingViewModel.CartShopping.value,
                        totalAmount = totalAmount,
                        createdAt = System.currentTimeMillis().toString()
                    )

                    // Llamar a createOrder con el único argumento 'order'
                    cartShoppingViewModel.createOrder(order)

                    // Vaciar el carrito (después de que la orden se ha creado)
                    cartShoppingViewModel.clearCart()

                    // Navegar a la pantalla de confirmación
                    navHostController.navigate("OrderDoneScreen")

                }
            )
        }
    }
}