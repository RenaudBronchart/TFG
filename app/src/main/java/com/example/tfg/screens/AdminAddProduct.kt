package com.example.tfg.screens
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.components.SelectProductCategory
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.ProductViewModel
import com.example.tfg.components.DataField
import com.example.tfg.components.TopBarComponent
import com.example.tfg.models.Product
import com.example.tfg.viewmodel.AddProductViewModel

@Composable
fun AdminAddProduct(navHostController: NavHostController, authViewModel: AuthViewModel, productViewModel: ProductViewModel, addProductViewModel: AddProductViewModel) {

    val selectedCategory: String by productViewModel.category.collectAsState("Selecciona una categoría")
    val name: String by productViewModel.name.collectAsState("")
    val price: Double by productViewModel.price.collectAsState(0.0)
    val description: String by productViewModel.description.collectAsState("")
    val category: String by productViewModel.category.collectAsState("")
    val image: String by productViewModel.image.collectAsState("")
    val stock: Int by productViewModel.stock.collectAsState(0)
    val brand: String by productViewModel.brand.collectAsState("")
    val isButtonEnable: Boolean by productViewModel.isButtonEnable.collectAsState(false)
    val snackbarHostState = remember { SnackbarHostState() }
    val message by addProductViewModel.messageConfirmation.collectAsState()

    LaunchedEffect(Unit) {
        productViewModel.setMessageConfirmation("")
    }

    // permite lanzar el message configurado
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)
            productViewModel.resetFields()
        }
    }

    val fields = listOf(
        Triple("Nombre", name, "nombre"),
        Triple("Precio", price.toString(), "precio"),
        Triple("Descripción", description, "descripcion"),
        Triple("Imagen", image, "imagen"),
        Triple("Stock", stock.toString(), "stock"),
        Triple("Marca", brand, "marca")
    )

    Scaffold(
        topBar = { TopBarComponent("Agregar producto", navHostController) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            SelectProductCategory(
                selectedCategory = category,
                onCategorySelected = { newCategory ->
                    productViewModel.onCompletedFields(name, price, description, newCategory, image, stock, brand)
                }
            )

            fields.forEach { (label, value, key) ->
                DataField(
                    label = label,
                    value = value,
                    onValueChange = { newValue ->
                        when (key) {
                            "nombre" -> productViewModel.onCompletedFields(newValue, price, description, selectedCategory, image, stock, brand)
                            "precio" -> productViewModel.onCompletedFields(name, newValue.toDoubleOrNull() ?: 0.0, description, selectedCategory, image, stock, brand)
                            "descripcion" -> productViewModel.onCompletedFields(name, price, newValue, selectedCategory, image, stock, brand)
                            "imagen" -> productViewModel.onCompletedFields(name, price, description, selectedCategory, newValue, stock, brand)
                            "stock" -> productViewModel.onCompletedFields(name, price, description, selectedCategory, image, newValue.toIntOrNull() ?: 0, brand)
                            "marca" -> productViewModel.onCompletedFields(name, price, description, selectedCategory, image, stock, newValue)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Crear un objeto Product con los valores actuales
            val product = Product(
                name = name,
                price = price,
                description = description,
                category = selectedCategory,
                image = image,
                stock = stock,
                brand = brand
            )

            Button(
                onClick = {
                    addProductViewModel.addProduct(product)
                },
                enabled = isButtonEnable,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Añadir producto")
            }
        }
    }
}




