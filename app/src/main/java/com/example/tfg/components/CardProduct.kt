package com.example.tfg.components

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tfg.models.Producto
import com.example.tfg.viewmodel.CartShoppingViewModel
import com.example.tfg.viewmodel.ProductViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProductCard(producto: Producto, isAdmin:Boolean, navController : NavHostController, productViewModel: ProductViewModel, cartShoppingViewModel: CartShoppingViewModel) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val existingItem = cartShoppingViewModel.CartShopping.value.find { it.id == producto.id }
    val isOutOfStock = producto.stock == 0

    val cardBackgroundColor = if (isOutOfStock) Color.Gray else Color.White
    val isClickable = !isOutOfStock

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(290.dp)
            .clickable(enabled = isClickable, onClick = {
                if (!isOutOfStock) {

                }
            }),
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
                    model = producto.imagen, //
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = producto.nombre, fontWeight = FontWeight.Bold)
                Text(text = "${producto.precio} €", color = Color.Black)

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
                                navController.navigate("EditProduct/${producto.id}") // Redirige vers l'édition
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp) // Taille plus grande
                            )
                            Spacer(modifier = Modifier.width(4.dp)) // Espacement entre icône et texte
                            /* Text("Editar", color = Color.White, fontSize = 11.sp)*/
                        }

                        Button(
                            modifier = Modifier.padding(2.dp).height(38.dp).weight(1f),
                            onClick = {
                                productViewModel.deleteProduct(producto.id) { message ->
                                    productViewModel.setMessageConfirmation(message)

                                }
                            },

                            ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp)) // Espacement entre icône et texte
                            /*Text("Eliminar", color = Color.White, fontSize = 11.sp)*/
                        }
                    }
                } else { // no admin
                    Button(
                        modifier = Modifier.padding(2.dp).height(38.dp).fillMaxWidth(),
                        onClick = {
                            // ver si hay producto y la cantidad
                            if (existingItem == null || existingItem.quantity < producto.stock) {
                                cartShoppingViewModel.addToCart(producto) //
                                Toast.makeText(
                                    context,
                                    "${producto.nombre} añadido a la cesta",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // Si el stock es limitado, mostramos un mensaje
                                Toast.makeText(
                                    context,
                                    "¡Stock limitado a ${producto.stock} unidades!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },

                        ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Añadir")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Añadir", color = Color.White)
                    }
                }
            }
        }
    }
