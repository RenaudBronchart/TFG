package com.example.tfg.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.viewmodel.ProductViewModel
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import com.example.tfg.components.BottomBarComponent
import com.example.tfg.components.ProductCard
import com.example.tfg.components.TopBarComponent
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.CartShoppingViewModel


@Composable
fun EshopScreen(navHostController: NavHostController, authViewModel : AuthViewModel, productViewModel: ProductViewModel, cartShoppingViewModel: CartShoppingViewModel) {
   var selectedCategory by remember { mutableStateOf("All") }
    val productos by productViewModel.productos.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()
    val cartItems by cartShoppingViewModel.CartShopping.collectAsState()

    authViewModel.fetchCurrentUser()
    LaunchedEffect(navHostController) {
        productViewModel.getProductosFromFirestore()
    }
    LaunchedEffect(isAdmin) {
        Log.d("EshopScreen", "isAdmin: $isAdmin")
    }
    LaunchedEffect(productos) {
        Log.d("EshopScreen", "Productos mis a jour!")
    }
    Scaffold(
        topBar = { TopBarComponent("Tienda", navHostController) },
        bottomBar = { BottomBarComponent(navHostController, cartItems) }
    ) { innerPadding -> //
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            /*// Filtres
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CategoryIcon("Pala", Icons.Default.SportsTennis, selectedCategory) {
                    selectedCategory = "Pala"
                }
                CategoryIcon("Pelotas", Icons.Default.SportsBaseball, selectedCategory) {
                    selectedCategory = "Pelotas"
                }
            }*/

            // Lazy de Grid
            LazyVerticalGrid(

                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize()
            ) {
                items(productos, key = { it.id }) { producto ->
                    ProductCard(producto, isAdmin, navHostController, productViewModel, cartShoppingViewModel)

                }
            }
        }
    }
}

/*@Composable
fun CategoryIcon(name: String, icon: ImageVector, selectedCategory: String, onClick: () -> Unit) {
  Card(
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = if (selectedCategory == name) Color.LightGray else Color.White),
      modifier = Modifier
          .size(60.dp)  // size caja
          .shadow(8.dp, RoundedCornerShape(12.dp)) // shape caja
          .clickable(onClick = onClick)
  ) {
      Column(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
      ) {
          Icon(
              imageVector = icon,
              contentDescription = name,
              tint = if (selectedCategory == name) Color.Blue else Color.Gray
          )
          Text(text = name, color = Color.Black)
      }
  }
}*/



