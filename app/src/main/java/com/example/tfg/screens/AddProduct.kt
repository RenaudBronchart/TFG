package com.example.tfg.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.components.SelectProductCategory
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.ProductoViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProduct (navController: NavHostController,authViewModel : AuthViewModel , productoviewModel: ProductoViewModel)    {


    val selectedCategory: String by productoviewModel.categoria.observeAsState("Selecciona una categoría")
    val nombre:String by productoviewModel.nombre.observeAsState("")
    val precio:Double by productoviewModel.precio.observeAsState(0.0)
    val descripcion:String by productoviewModel.descripcion.observeAsState("")
    val categoria:String by productoviewModel.categoria.observeAsState("")
    val imagen:String by productoviewModel.imagen.observeAsState("")
    val stock:Int by productoviewModel.stock.observeAsState(0)
    val marca:String by productoviewModel.marca.observeAsState("")

    // verificar si los datos estan rellandos
    val isButtonEnable:Boolean by productoviewModel.isButtonEnable.observeAsState(initial = false)
    val snackbarHostState = remember { SnackbarHostState() }
    val message by productoviewModel.messageConfirmation.collectAsState()

    LaunchedEffect(Unit) {
        productoviewModel.setMessageConfirmation("")
    }

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)  // Afficher le Snackbar
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                title = { Text("Agregar producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("Home") }) {
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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            SelectProductCategory(
                selectedCategory = selectedCategory,
                onCategorySelected = { newCategory ->
                    productoviewModel.onCompletedFields(nombre, precio, descripcion, newCategory, imagen, stock, marca)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = nombre,
                onValueChange = {
                    productoviewModel.onCompletedFields(it, precio, descripcion, categoria, imagen, stock, marca)
                },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            TextField(
                value = if (precio == 0.0) "" else precio.toString(),
                onValueChange = {
                    val newPrecio = it.toDoubleOrNull() ?: 0.0 // si entrada no es valida(abc) devuelve 0
                    productoviewModel.onCompletedFields(nombre, newPrecio, descripcion, categoria, imagen, stock, marca)
                },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) // teclado numerico
            )

            TextField(
                value = descripcion,
                onValueChange = { productoviewModel.onCompletedFields(nombre = nombre, precio = precio, descripcion = it, categoria = categoria, imagen = imagen, stock = stock, marca = marca) },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            TextField(
                value = imagen,
                onValueChange = { productoviewModel.onCompletedFields(nombre = nombre, precio = precio, descripcion = descripcion, categoria = categoria, imagen = it, stock = stock, marca = marca) },
                label = { Text("Imagen") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            TextField(
                value = if (stock == 0) "" else stock.toString(),
                onValueChange = {
                    val newStock = it.toIntOrNull() ?: 0
                    productoviewModel.onCompletedFields(nombre, precio, descripcion, categoria, imagen, newStock, marca)
                },
                label = { Text("Stock") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            TextField(
                value = marca,
                onValueChange = { productoviewModel.onCompletedFields(nombre = nombre, precio = precio, descripcion = descripcion, categoria = categoria, imagen = imagen, stock = stock, marca = it) },
                label = { Text("Marca") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    productoviewModel.addProduct(nombre, precio, descripcion, categoria, imagen, stock, marca) { message ->
                        productoviewModel.setMessageConfirmation(message)
                            productoviewModel.resetFields()

                    }
                },
                enabled = isButtonEnable,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Añadir producto")
            }
        }
    }
}

fun updateField(update: (String) -> Unit, value: String) {
    update(value)
}

