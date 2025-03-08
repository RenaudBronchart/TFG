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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tfg.models.Producto
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.CartShoppingViewModel


@Composable
fun CardCheckoutShopping(navHostController: NavHostController, cartShoppingViewModel: CartShoppingViewModel, producto: Producto
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
                model = producto.imagen,
                contentDescription = producto.nombre,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))

            // info sobre el producto
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = producto.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "${producto.precio} €",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                val stockRestante = producto.stock - producto.quantity
                val stockMessage = if (stockRestante > 0) {
                    "Stock disponible: $stockRestante"
                } else {
                    "No hay suficiente stock!"
                }

                // Muestra el mensaje de stock con el color adecuado
                if (producto.stock <= producto.quantity) {

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
                        text = "Cantidad: ${producto.quantity}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    //button -
                    IconButton(
                        onClick = {
                            cartShoppingViewModel.decreaseQuantity(producto)
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
                            cartShoppingViewModel.increaseQuantity(producto)
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
                    cartShoppingViewModel.removeToCart(producto)
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
fun TotalToPay(navHostController: NavHostController, cartShoppingViewModel: CartShoppingViewModel, productos: List<Producto>, authViewModel: AuthViewModel){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text(
            text = "Total: ${cartShoppingViewModel.calcularTotal()} €",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Button(
            onClick = {
                // creamos el order
                cartShoppingViewModel.createOrder(authViewModel) {
                    // pedido hecho, poner a 0
                    cartShoppingViewModel.clearCart()
                    // rederigmos el usuario a la pagina que los datos del pedido
                    navHostController.navigate("OrderDoneScreen")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Pagar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}