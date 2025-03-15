package com.example.tfg.screens
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
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

    val selectedCategory: String by productviewModel.categoria.observeAsState("Selecciona una categoría")
    val nombre:String by productviewModel.nombre.observeAsState("")
    val precio:Double by productviewModel.precio.observeAsState(0.0)
    val descripcion:String by productviewModel.descripcion.observeAsState("")
    val categoria:String by productviewModel.categoria.observeAsState("")
    val imagen:String by productviewModel.imagen.observeAsState("")
    val stock:Int by productviewModel.stock.observeAsState(0)
    val marca:String by productviewModel.marca.observeAsState("")
    val isButtonEnable:Boolean by productviewModel.isButtonEnable.observeAsState(initial = false)
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
        Triple("Nombre", nombre, "nombre"), // 1) Se usa para mostrar el texto en la UI.
        Triple("Precio", precio.toString(), "precio"),// 2
        Triple("Descripción", descripcion, "descripcion"),
        Triple("Imagen", imagen, "imagen"),
        Triple("Stock", stock.toString(), "stock"),
        Triple("Marca", marca, "marca")
    )
    // configurar TOPBAR
    Scaffold(
        topBar = { TopBarComponent("Agregar producto", navHostController) }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            SelectProductCategory(
                selectedCategory = categoria,
                onCategorySelected = { newCategory -> productviewModel.onCompletedFields(nombre, precio, descripcion, newCategory, imagen, stock, marca) }
            )
            // bucle para mostrar cada campos y poder ecribrir // design de los campos hecho con  la fucion de DataField
            fields.forEach { (label, value, key) ->
               DataField(
                    label = label,
                    value = value,
                    onValueChange = { newValue ->
                        when (key) {
                            "nombre" -> productviewModel.onCompletedFields(newValue, precio, descripcion, selectedCategory, imagen, stock, marca)
                            "precio" -> productviewModel.onCompletedFields(nombre, newValue.toDoubleOrNull() ?: 0.0, descripcion, selectedCategory, imagen, stock, marca)
                            "descripcion" -> productviewModel.onCompletedFields(nombre, precio, newValue, selectedCategory, imagen, stock, marca)
                            "imagen" -> productviewModel.onCompletedFields(nombre, precio, descripcion, selectedCategory, newValue, stock, marca)
                            "stock" -> productviewModel.onCompletedFields(nombre, precio, descripcion, selectedCategory, imagen, newValue.toIntOrNull() ?: 0, marca)
                            "marca" -> productviewModel.onCompletedFields(nombre, precio, descripcion, selectedCategory, imagen, stock, newValue
                            )
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            // buton para pinchar y guardar los datos // funcion de logica hecho en productoviwModel
            Button(
                onClick = {
                    productviewModel.addProduct(nombre, precio, descripcion, selectedCategory, imagen, stock, marca) { message ->
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




