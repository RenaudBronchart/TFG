package com.example.tfg.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.tfg.viewmodel.AuthViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.tfg.components.SelectProductCategory
import com.example.tfg.components.DataField
import com.example.tfg.components.TopBarComponent
import com.example.tfg.viewmodel.EditProductViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEditProduct(navHostController: NavHostController, authViewModel: AuthViewModel, editProductViewModel: EditProductViewModel, productId: String) {
    val isLoading by editProductViewModel.isLoading.collectAsState()
    val producto by editProductViewModel.product.collectAsState() // Manejar el estado de usuario
    val snackbarHostState = remember { SnackbarHostState() }
    val message by editProductViewModel.messageConfirmation.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // cargar producto
    LaunchedEffect(productId) {
        editProductViewModel.loadProduct(productId)
    }


    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
            }
            editProductViewModel.resetMessage()
            if (message == "Producto actualizado correctamente") {
                // Si el producto fue actualizado correctamente, regresamos
                coroutineScope.launch {
                    delay(500) // ponemos delay para que se vea el mensaje
                    navHostController.popBackStack() // Regresamos a la página anterior
                }
            }
        }
    }

    // para poder configurar TOPBAR
    Scaffold(
        topBar = { TopBarComponent("Editar producto", navHostController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SelectProductCategory(selectedCategory = producto.category, onCategorySelected = { newCategory -> editProductViewModel.updateProductField("category", newCategory) })
            DataField(label = "Nombre", value = producto.name, onValueChange = { newName -> editProductViewModel.updateProductField("name", newName) })
            DataField(label = "Precio", value = producto.price.toString(), onValueChange = { newPrice -> newPrice.toDoubleOrNull()?.let { editProductViewModel.updateProductField("price", it) } }, keyboardType = KeyboardType.Number)
            DataField(label = "Descripción", value = producto.description, onValueChange = { newDescription -> editProductViewModel.updateProductField("description", newDescription) })
            DataField(label = "Imagen URL", value = producto.image, onValueChange = { newImage -> editProductViewModel.updateProductField("image", newImage) })
            DataField(label = "Stock", value = producto.stock.toString(), onValueChange = { newStock -> newStock.toIntOrNull()?.let {editProductViewModel.updateProductField("stock", it) } }, keyboardType = KeyboardType.Number)
            DataField(label = "Marca", value = producto.brand, onValueChange = { newBrand -> editProductViewModel.updateProductField("brand", newBrand) })

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    editProductViewModel.updateProduct(productId)
                },
                enabled = !isLoading,  //
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 8.dp)
                        )
                        Text("Actualizando...")
                    }
                } else {
                    Text("Guardar cambios")
                }
            }
    }
}
}


