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
import com.example.tfg.components.CardProductDetail
import com.example.tfg.components.ProductCard
import com.example.tfg.components.TopBarComponent
import com.example.tfg.models.Producto
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.CartShoppingViewModel


@Composable
fun EshopScreen(navHostController: NavHostController, authViewModel : AuthViewModel, productViewModel: ProductViewModel, cartShoppingViewModel: CartShoppingViewModel) {
    val productos by productViewModel.productos.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()
    val cartItems by cartShoppingViewModel.CartShopping.collectAsState()

    var selectedProduct by remember { mutableStateOf<Producto?>(null) }
    var showSheet by remember { mutableStateOf(false) }

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
            // Lazy de Grid
            LazyVerticalGrid(

                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize()
            ) {
                items(productos, key = { it.id }) { producto ->
                    ProductCard(producto, isAdmin, navHostController, productViewModel, cartShoppingViewModel,
                        onProductClick = {
                            selectedProduct = producto
                            showSheet = true
                        }
                    )

                }
            }
        }
        if (showSheet && selectedProduct != null) {
            CardProductDetail(
                navController = navHostController,
                producto = selectedProduct!!,
                cartShoppingViewModel = cartShoppingViewModel,
                productViewModel = productViewModel,
                onAddToCart = {
                    cartShoppingViewModel.addToCart(selectedProduct!!)
                },
                onDismiss = {
                    showSheet = false
                    selectedProduct = null
                }
            )
        }
    }
}





