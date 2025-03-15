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
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tfg.models.Product
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.CartShoppingViewModel


@Composable
fun CardOrderCheckoutShopping(navHostController: NavHostController, cartShoppingViewModel: CartShoppingViewModel, product: Product
) {

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
            // imagen producto
            AsyncImage(
                model = product.imagen,
                contentDescription = product.nombre,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop // permite ajustar la imagen con el espacio disponible
            )
            Spacer(modifier = Modifier.width(16.dp))

            // info sobre el producto
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = product.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "${product.precio} €",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                // calculemos lo que queda, en relacion a la cantidad que se pone en la ceste y lo
                // que queda de stock
                val stockRestante = product.stock - product.quantity
                val stockMessage = if (stockRestante > 0) {
                    "Stock disponible: $stockRestante"
                } else {
                    "No hay suficiente stock!"
                }

                // Muestra el mensaje de stock con el color adecuado
                if (product.stock <= product.quantity) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Stock limitado",
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
                    // mensaje para mostrar
                    Text(
                        text = stockMessage,
                        fontSize = 14.sp,
                        color = if (stockRestante > 0) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                    )
                }

                // visual para buton cantidad + y -
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp), //
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Cantidad: ${product.quantity}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    //button - // gracias a viewmodel y mutable, al pinchar, decrease
                    IconButton(
                        onClick = {
                            cartShoppingViewModel.decreaseQuantity(product)
                        },
                        modifier = Modifier.size(24.dp) // talla button
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "RemoveQuantity",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Button +
                    IconButton(
                        onClick = {
                            cartShoppingViewModel.increaseQuantity(product)
                        },
                        modifier = Modifier.size(24.dp) // talla button
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "AddQuantity",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // button borrar
            IconButton(
                onClick = {
                    cartShoppingViewModel.removeToCart(product)
                },
                modifier = Modifier.align(Alignment.CenterVertically) //
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}





@Composable
fun TotalToPay(total: Double, onClickPay: () -> Unit){
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