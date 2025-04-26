package com.example.tfg.screens

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.components.SelectProductCategory
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.ProductViewModel
import com.example.tfg.components.DataField
import com.example.tfg.components.PickImageFromGallery
import com.example.tfg.components.TopBarComponent
import com.example.tfg.models.Product
import com.example.tfg.viewmodel.AddProductViewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.tfg.components.ImagePickerField

@OptIn(UnstableApi::class)
@Composable
fun AdminAddProduct(
    navHostController: NavHostController,
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel,
    addProductViewModel: AddProductViewModel
) {
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
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        productViewModel.setMessageConfirmation("")
    }

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)
            productViewModel.resetFields()
        }
    }

    val pickImageLauncher = PickImageFromGallery { uri ->
        uri?.let {
            addProductViewModel.uploadImageAndSetUrl(it,productViewModel)

        }
    }

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
            // Catégorie
            SelectProductCategory(
                selectedCategory = category,
                onCategorySelected = { newCategory ->
                    productViewModel.onCompletedFields(name, price, description, newCategory, image, stock, brand)
                }
            )

            // Champs normaux (SANS imagen pour l'instant)
            listOf(
                Triple("Nombre", name, "nombre"),
                Triple("Precio", price.toString(), "precio"),
                Triple("Descripción", description, "descripcion"),
                Triple("Stock", stock.toString(), "stock"),
                Triple("Marca", brand, "marca")
            ).forEach { (label, value, key) ->
                DataField(
                    label = label,
                    value = value,
                    onValueChange = { newValue ->
                        when (key) {
                            "nombre" -> productViewModel.onCompletedFields(newValue, price, description, selectedCategory, image, stock, brand)
                            "precio" -> productViewModel.onCompletedFields(name, newValue.toDoubleOrNull() ?: 0.0, description, selectedCategory, image, stock, brand)
                            "descripcion" -> productViewModel.onCompletedFields(name, price, newValue, selectedCategory, image, stock, brand)
                            "stock" -> productViewModel.onCompletedFields(name, price, description, selectedCategory, image, newValue.toIntOrNull() ?: 0, brand)
                            "marca" -> productViewModel.onCompletedFields(name, price, description, selectedCategory, image, stock, newValue)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { pickImageLauncher() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Seleccionar imagen")
            }

            if (image.isNotEmpty()) {
                Text(
                    text = "Imagen seleccionada ️",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    Log.d("AdminAddProduct", "Image URL avant création produit: $image")
                    val product = Product(
                        name = name,
                        price = price,
                        description = description,
                        category = selectedCategory,
                        image = image, // ici l'URL de l'image Firebase !
                        stock = stock,
                        brand = brand
                    )

                    addProductViewModel.addProduct(product) { message ->
                        productViewModel.setMessageConfirmation(message)
                        productViewModel.resetFields()
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
