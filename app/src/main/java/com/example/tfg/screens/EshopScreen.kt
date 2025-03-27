package com.example.tfg.screens

import android.util.Log
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import com.example.tfg.components.BottomBarComponent
import com.example.tfg.components.CardProductDetail
import com.example.tfg.components.CardProduct
import com.example.tfg.components.TopBarComponent
import com.example.tfg.models.Product
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.CartShoppingViewModel




@Composable
fun EshopScreen(navHostController: NavHostController, authViewModel : AuthViewModel, productViewModel: ProductViewModel, cartShoppingViewModel: CartShoppingViewModel) {
    val productos by productViewModel.products.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()
    val cartItems by cartShoppingViewModel.CartShopping.collectAsState()
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
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
        bottomBar = { BottomBarComponent(navHostController, cartItems) },

    ) { innerPadding -> //
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Le contenu de la grille
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize()
            ) {
                items(productos, key = { it.id }) { producto ->
                    CardProduct(
                        product = producto,
                        isAdmin = isAdmin,
                        navHostController = navHostController,
                        productViewModel = productViewModel,
                        cartShoppingViewModel = cartShoppingViewModel,
                        onProductClick = {
                            selectedProduct = producto
                            showSheet = true
                        }
                    )
                }
            }

            // si es admin , button floating para llegar a la pagina de adminAddProduct
            if (isAdmin) {
                FloatingActionButton(
                    onClick = { navHostController.navigate("AdminAddProduct") },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Product")
                }
            }

        }
        // si hay productos, vamos a mostrar el producto
        // función let {} para ejecutar la funcion solo si el  producto si no es nulo
        selectedProduct?.let { product ->
            if (showSheet) {
                CardProductDetail(
                    navController = navHostController,
                    product = product,
                    cartShoppingViewModel = cartShoppingViewModel,
                    productViewModel = productViewModel,
                    onAddToCart = {
                        cartShoppingViewModel.addToCart(product)
                    },
                    onDismiss = {
                        showSheet = false
                        selectedProduct = null
                    }
                )
            }
        }
    }
}





