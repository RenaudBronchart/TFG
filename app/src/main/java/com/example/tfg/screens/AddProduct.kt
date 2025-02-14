package com.example.tfg.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.components.SelectProductCategory
import com.example.tfg.models.Producto
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.ProductoViewModel
import com.google.firebase.firestore.FirebaseFirestore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProduct (navController: NavHostController,authViewModel : AuthViewModel , viewModel: ProductoViewModel)    {

    val db = FirebaseFirestore.getInstance()
    val name_collection = "productos"

    var selectedCategory by remember { mutableStateOf("Selecciona una categoría") }
    val nombre:String by viewModel.nombre.observeAsState("")
    val precio:Double by viewModel.precio.observeAsState(0.0)
    val descripcion:String by viewModel.descripcion.observeAsState("")
    val categoria:String by viewModel.categoria.observeAsState("")
    val imagen:String by viewModel.imagen.observeAsState("")
    val stock:Int by viewModel.stock.observeAsState(0)
    val marca:String by viewModel.marca.observeAsState("")

    // verificar si los datos estan rellandos
    val isButtonEnable:Boolean by viewModel.isButtonEnable.observeAsState(initial = false)

    var mensaje_confirmacion by remember { mutableStateOf("") }



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
        }
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
                    viewModel.onCompletedFields(nombre, precio, descripcion, newCategory, imagen, stock, marca)
                    selectedCategory = newCategory // actualiza la categoria seleccionada
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = nombre,
                onValueChange = {
                    viewModel.onCompletedFields(it, precio, descripcion, categoria, imagen, stock, marca)
                },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            /* TextField(
                    value = nombre,
                    onValueChange = { viewModel.onCompletedFields(it, precio, descripcion, categoria, imagen, stock, marca) },
                    label = { Text("Nombre") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = Color.Gray
                    )
                )*/

            TextField(
                value = if (precio == 0.0) "" else precio.toString(),
                onValueChange = {
                    val newPrecio = it.toDoubleOrNull() ?: 0.0 // si entrada no es valida(abc) devuelve 0
                    viewModel.onCompletedFields(nombre, newPrecio, descripcion, categoria, imagen, stock, marca)
                },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) // teclado numerico
            )

            TextField(
                value = descripcion,
                onValueChange = { viewModel.onCompletedFields(nombre = nombre, precio = precio, descripcion = it, categoria = categoria, imagen = imagen, stock = stock, marca = marca) },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            TextField(
                value = imagen,
                onValueChange = { viewModel.onCompletedFields(nombre = nombre, precio = precio, descripcion = descripcion, categoria = categoria, imagen = it, stock = stock, marca = marca) },
                label = { Text("Imagen") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            TextField(
                value = if (stock == 0) "" else stock.toString(),
                onValueChange = {
                    val newStock = it.toIntOrNull() ?: 0
                    viewModel.onCompletedFields(nombre, precio, descripcion, categoria, imagen, newStock, marca)
                },
                label = { Text("Stock") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            TextField(
                value = marca,
                onValueChange = { viewModel.onCompletedFields(nombre = nombre, precio = precio, descripcion = descripcion, categoria = categoria, imagen = imagen, stock = stock, marca = it) },
                label = { Text("Marca") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // creacion objet producto de la data class Producto
            val producto = Producto(
                nombre = nombre,
                precio = precio,
                descripcion = descripcion,
                categoria = categoria,
                imagen = imagen,
                stock = stock,
                marca = marca
            )




            Button(
                onClick = {
                    db.collection(name_collection)
                        .document(producto.id)
                        .set(producto)
                       .addOnSuccessListener {
                           mensaje_confirmacion = "Producto añadido correctamente"
                           viewModel.resetFields()
                        }
                        .addOnFailureListener {
                            mensaje_confirmacion = "No se ha podido añadir un producto"
                        }

                },
                enabled = isButtonEnable,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Añadir producto")
            }
            Spacer(modifier = Modifier.size(5.dp))
            Text(text = mensaje_confirmacion)
        }
    }
}

