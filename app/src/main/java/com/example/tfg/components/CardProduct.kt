package com.example.tfg.components

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tfg.models.Product
import com.example.tfg.viewmodel.CartShoppingViewModel
import com.example.tfg.viewmodel.DeleteProductViewModel
import com.example.tfg.viewmodel.ProductViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CardProduct(product: Product, isAdmin:Boolean, navHostController : NavHostController, productViewModel: ProductViewModel, deleteProductViewModel: DeleteProductViewModel ,
                cartShoppingViewModel: CartShoppingViewModel, onProductClick: () -> Unit) {

    val context = androidx.compose.ui.platform.LocalContext.current
    var showDialog by remember { mutableStateOf(false) } // Variable para el diálogo
    val cartItems by cartShoppingViewModel.CartShopping.collectAsState()
    val existingItem = cartItems.find { it.id == product.id }
    val isOutOfStock = product.stock == 0 || (existingItem != null && existingItem.quantity >= product.stock)


    val cardBackgroundColor = if (isOutOfStock) Color.LightGray else Color.White


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(290.dp)
            .clickable(enabled = !isOutOfStock) { onProductClick() },
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = product.image, //
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product.name, fontWeight = FontWeight.Bold)
                Text(text = "${product.price} €", color = Color.Black)

                Spacer(modifier = Modifier.weight(1f))

                if (isAdmin) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            modifier = Modifier.padding(2.dp).height(38.dp).weight(1f),
                            onClick = {
                                navHostController.navigate("AdminEditProduct/${product.id}") // Redirige vers l'édition
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))

                        }

                        Button(
                            modifier = Modifier.padding(2.dp).height(38.dp).weight(1f),
                            onClick = {

                                showDialog = true
                            },
                            ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp)) // espacio icono texto


                        }
                    }
                } else { // no admin
                    Button(
                        modifier = Modifier.padding(2.dp).height(38.dp).fillMaxWidth(),
                        onClick = {
                            // ver si hay producto y la cantidad
                            if(!isOutOfStock) {
                                cartShoppingViewModel.addToCart(product) //
                                Toast.makeText(
                                    context,
                                    "${product.name} añadido a la cesta",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // Si el stock es limitado, mostramos un mensaje
                                Toast.makeText(
                                    context,
                                    "¡Stock limitado a ${product.stock} unidades!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        enabled = !isOutOfStock,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isOutOfStock) Color.Gray else MaterialTheme.colorScheme.primary, // Bleu sinon gris
                            disabledContainerColor = Color.Gray
                        )
                        ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Añadir")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Añadir", color = Color.White)
                    }
                }
            }
        }

    // como hemos definindos en el butotn de borrar true, se mostra, false = se cierra
    if (showDialog) {
        mostrarMessageConfirmation(
            message = "¿Seguro que quieres eliminar ${product.name}?",
            onConfirm = {
                showDialog = false
                deleteProductViewModel.deleteProduct(product.id) { message ->
                    productViewModel.setMessageConfirmation(message)
                }
            },
            // si pincha en cancelar, se cancela y nada sucede
            onDismiss = { showDialog = false }
        )
    }
}


