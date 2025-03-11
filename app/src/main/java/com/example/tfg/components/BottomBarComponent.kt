package com.example.tfg.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tfg.models.Producto

@Composable // carItems :list-> para obtener la lista de productos en la cesta
// lista objetos de productos
fun BottomBarComponent(navController: NavHostController, cartItems: List<Producto>) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.height(80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            // funcion bottombarIcon que permite tener icon,Stringlabel,route,navcontroller
            BottomBarIcon(Icons.Filled.Home, "Inicio", "Home", navController)
            Spacer(modifier = Modifier.width(32.dp))

            BottomBarIcon(Icons.Default.Event , "Reservas", "BookingPadelScreen", navController)
            Spacer(modifier = Modifier.width(32.dp))

            BottomBarIcon(Icons.Filled.ShoppingBag , "Tienda", "EshopScreen", navController)
            Spacer(modifier = Modifier.width(32.dp))

            BottomBarIcon(Icons.Filled.Person, "Perfil", "Profile", navController)
            Spacer(modifier = Modifier.width(32.dp))

            // BadgedBox se utiliza para mostrar contador o notificacion
            BadgedBox(
                badge = {
                    val totalQuantity = cartItems.sumOf { it.quantity } // total cantidad
                    if (totalQuantity > 0) {
                        Badge(containerColor = Color.Red) {
                            Text("$totalQuantity", color = Color.White)
                        }
                    }
                },
                modifier = Modifier.clickable {
                    navController.navigate("CheckoutShopping")
                }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Cesta", tint = Color.White)
                    Text("Cesta", color = Color.White, fontSize = 14.sp)
                }

            }
        }
    }
}

@Composable
fun BottomBarIcon(icon: ImageVector, label: String, route: String, navHostController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable { navHostController.navigate(route) }
    ) {
        Icon(icon, contentDescription = label, tint = Color.White)
        Text(label, color = Color.White, fontSize = 14.sp)
    }
}