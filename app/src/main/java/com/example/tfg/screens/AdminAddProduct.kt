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

@Composable
fun AdminAddProduct (navHostController: NavHostController, authViewModel : AuthViewModel, productviewModel: ProductViewModel)    {

    val selectedCategory: String by productviewModel.category.collectAsState("Selecciona una categoría")
    val name:String by productviewModel.name.collectAsState("")
    val price:Double by productviewModel.price.collectAsState(0.0)
    val description:String by productviewModel.description.collectAsState("")
    val category:String by productviewModel.category.collectAsState("")
    val image:String by productviewModel.image.collectAsState("")
    val stock:Int by productviewModel.stock.collectAsState(0)
    val brand:String by productviewModel.brand.collectAsState("")
    val isButtonEnable:Boolean by productviewModel.isButtonEnable.collectAsState(initial = false)
    val snackbarHostState = remember { SnackbarHostState() }
    val message by productviewModel.messageConfirmation.collectAsState()

    LaunchedEffect(Unit) {
        productviewModel.setMessageConfirmation("")
    }

    // permite lanzar el message configurado
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)
            productviewModel.resetFields()
        }
    }
    // creamos una lista TRIPLE de los campos //
    // label → Se usa para mostrar el texto en la UI.
    // value → Se usa para llenar el campo con su valor actual.
    //key → Se usa en when para actualizar el campo correcto.
    val fields = listOf(
        Triple("Nombre", name, "nombre"), // 1) Se usa para mostrar el texto en la UI.
        Triple("Precio", price.toString(), "precio"),// 2
        Triple("Descripción", description, "descripcion"),
        Triple("Imagen", image, "imagen"),
        Triple("Stock", stock.toString(), "stock"),
        Triple("Marca", brand, "marca")
    )
    // configurar TOPBAR
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
                onCategorySelected = { newCategory -> productviewModel.onCompletedFields(name, price, description, newCategory, image, stock, brand) }
            )
            // bucle para mostrar cada campos y poder ecribrir // design de los campos hecho con  la fucion de DataField
            fields.forEach { (label, value, key) ->
               DataField(
                    label = label,
                    value = value,
                    onValueChange = { newValue ->
                        when (key) {
                            "nombre" -> productviewModel.onCompletedFields(newValue, price, description, selectedCategory, image, stock, brand)
                            "precio" -> productviewModel.onCompletedFields(name, newValue.toDoubleOrNull() ?: 0.0, description, selectedCategory, image, stock, brand)
                            "descripcion" -> productviewModel.onCompletedFields(name, price, newValue, selectedCategory, image, stock, brand)
                            "imagen" -> productviewModel.onCompletedFields(name, price, description, selectedCategory, newValue, stock, brand)
                            "stock" -> productviewModel.onCompletedFields(name, price, description, selectedCategory, image, newValue.toIntOrNull() ?: 0, brand)
                            "marca" -> productviewModel.onCompletedFields(name, price, description, selectedCategory, image, stock, newValue
                            )
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            // buton para pinchar y guardar los datos // funcion de logica hecho en productoviwModel
            Button(
                onClick = {
                    productviewModel.addProduct(name, price, description, selectedCategory, image, stock, brand) { message ->
                        productviewModel.setMessageConfirmation(message)
                        productviewModel.resetFields()
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




