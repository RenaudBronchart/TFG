package com.example.tfg.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.viewmodel.ProductViewModel
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.CartShoppingViewModel
import com.example.tfg.viewmodel.DeleteProductViewModel
import com.example.tfg.models.Product
import com.example.tfg.components.BottomBarComponent
import com.example.tfg.components.CardProduct
import com.example.tfg.components.CardProductDetail
import com.example.tfg.components.TopBarComponent

@Composable
fun EshopScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel,
    cartShoppingViewModel: CartShoppingViewModel,
    deleteProductViewModel: DeleteProductViewModel
) {
    val productos by productViewModel.products.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()
    val cartItems by cartShoppingViewModel.CartShopping.collectAsState()
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var showSheet by remember { mutableStateOf(false) }

    // Cargamos los productos una vez cuando se abre la pantalla
    LaunchedEffect(Unit) {
        productViewModel.getProducts()
    }

    // Observamos si cambia el estado de administrador
    LaunchedEffect(isAdmin) {
        Log.d("EshopScreen", "isAdmin: $isAdmin")
    }

    Scaffold(
        topBar = { TopBarComponent("Tienda", navHostController) },
        bottomBar = { BottomBarComponent(navHostController, cartItems) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Lista de productos en formato grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize()
            ) {
                items(productos, key = { it.id }) { producto ->
                    CardProduct(
                        product = producto,
                        isAdmin = isAdmin,
                        navHostController = navHostController,
                        cartShoppingViewModel = cartShoppingViewModel,
                        onProductClick = {
                            selectedProduct = producto
                            showSheet = true
                        },
                        onDeleteClick = { product ->
                            deleteProductViewModel.deleteProduct(product.id) { message ->
                                productViewModel.setMessageConfirmation(message)
                                productViewModel.getProducts() // Refrescamos la lista después de borrar
                            }
                        }
                    )
                }
            }

            // Si el usuario es admin, mostramos el botón flotante para añadir productos
            if (isAdmin) {
                FloatingActionButton(
                    onClick = { navHostController.navigate("AdminAddProduct") },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir Producto")
                }
            }
        }

        // Mostrar el detalle del producto si se ha seleccionado uno
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
