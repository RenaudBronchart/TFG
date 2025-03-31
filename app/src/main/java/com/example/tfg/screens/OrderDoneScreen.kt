package com.example.tfg.screens


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.OrderViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tfg.components.CardOrderMessageConfirmation
import com.example.tfg.viewmodel.CartShoppingViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun OrderDoneScreen( navHostController: NavHostController,authViewModel: AuthViewModel,orderViewModel: OrderViewModel, cartShoppingViewModel: CartShoppingViewModel) {

    val lastOrderId by cartShoppingViewModel.lastOrderId.collectAsState()  // ID de la última orden
    val lastOrder by orderViewModel.lastOrder.collectAsState()  // La última orden cargada


    LaunchedEffect(lastOrderId) {
        if (!lastOrderId.isNullOrEmpty()) {
            orderViewModel.loadLastOrder(lastOrderId!!)  // Cargar la última orden usando el ID
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Contenido centrado verticalmente
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icono
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "confirm",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(64.dp)
                    .padding(bottom = 16.dp)
            )
            // Texto
            Text(
                text = "¡Tu pedido está confirmado!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Mostrar la última orden si existe
            lastOrder?.let { order ->
                CardOrderMessageConfirmation(
                    navHostController = navHostController,
                    product = order.products.firstOrNull() ?: return@let,
                    order = order
                )
            }

            Spacer(modifier = Modifier.height(32.dp)) // Espacio para la tarjeta y el buton

            // Botón para volver a la página principal
            Button(
                onClick = {
                    navHostController.navigate("Home") {
                        popUpTo("Home") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Home",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}



