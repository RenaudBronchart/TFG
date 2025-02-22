package com.example.tfg.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.tfg.viewmodel.EditProductViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProduct(navController: NavHostController, authViewModel: AuthViewModel, editProductViewModel: EditProductViewModel, productId: String) {
    val isLoading by editProductViewModel.isLoading.collectAsState()
    val producto by editProductViewModel.producto.collectAsState() // Manejar el estado de usuario
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
        }
    }

    // para poder configurar TOPBAR
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                title = { Text("Editar producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "volver",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SelectProductCategory(selectedCategory = producto.categoria, onCategorySelected = editProductViewModel::setCategoria)
            DataField(label = "Nombre", value = producto.nombre, onValueChange = editProductViewModel::setNombre)
            DataField(label = "Precio", value = producto.precio.toString(), onValueChange = { editProductViewModel.setPrecio(it.toDoubleOrNull() ?: 0.0) }, keyboardType = KeyboardType.Number)
            DataField(label = "Descripción", value = producto.descripcion, onValueChange = editProductViewModel::setDescripcion)
            DataField(label = "Imagen URL", value = producto.imagen, onValueChange = editProductViewModel::setImagen)
            DataField(label = "Stock", value = producto.stock.toString(), onValueChange = { editProductViewModel.setStock(it.toIntOrNull() ?: 0) }, keyboardType = KeyboardType.Number)
            DataField(label = "Marca", value = producto.marca, onValueChange = editProductViewModel::setMarca)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    editProductViewModel.updateProduct(productId) { message ->
                        editProductViewModel.setMessageConfirmation(message)
                        coroutineScope.launch {
                            delay(500) // ponemos delay
                            navController.popBackStack() // para volver a la pagina de Etienda
                        }
                    }
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
}}


