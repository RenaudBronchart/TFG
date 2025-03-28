package com.example.tfg.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tfg.models.Product
import com.example.tfg.viewmodel.CartShoppingViewModel


@Composable
fun CardOrderCheckoutShopping(navHostController: NavHostController, cartShoppingViewModel: CartShoppingViewModel, product: Product) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Imagen del producto
            AsyncImage(
                model = product.image,
                contentDescription = product.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))

            // Información sobre el producto
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "${product.price} €",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                val stockRemaining = product.stock - product.quantity
                val stockMessage = if (stockRemaining > 0) {
                    "Stock disponible: $stockRemaining"
                } else {
                    "No hay suficiente stock!"
                }

                // Mostrar mensaje de stock con color adecuado
                if (product.stock <= product.quantity) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Stock limit",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "¡Stock limitado!",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                } else {
                    Text(
                        text = stockMessage,
                        fontSize = 14.sp,
                        color = if (stockRemaining > 0) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                    )
                }

                // Visualización para botones de cantidad + y -
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Cantidad: ${product.quantity}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    // Botón - para disminuir la cantidad
                    IconButton(
                        onClick = {
                            cartShoppingViewModel.decreaseQuantity(product)
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "RemoveQuantity",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Botón + para aumentar la cantidad
                    IconButton(
                        onClick = {
                            cartShoppingViewModel.increaseQuantity(product)
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "AddQuantity",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Botón de eliminar
            IconButton(
                onClick = {
                    showDialog = true // Mostrar el cuadro de confirmación cuando se intenta eliminar
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        // Mostrar el cuadro de confirmación si showDialog es verdadero
        if (showDialog) {
            mostrarMessageConfirmation(
                message = "Estás seguro de que deseas eliminar este producto del carrito?",
                onConfirm = {
                    showDialog = false // Cerrar el cuadro de diálogo
                    cartShoppingViewModel.removeFromCart(product) // Eliminar producto del carrito
                },
                onDismiss = { showDialog = false } // Si el usuario cancela, cerrar el cuadro de diálogo
            )
        }
    }
}




@Composable
fun TotalToPay(total: Double, onClickPay: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text(
            text = "Total: $total €",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Button(
            onClick = onClickPay,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Pagar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}
