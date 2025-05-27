package com.example.tfg.components

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.tfg.models.Product
import com.example.tfg.viewmodel.CartShoppingViewModel
import com.example.tfg.viewmodel.ProductViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardProductDetail(navController: NavHostController, product: Product, productViewModel: ProductViewModel,
                      cartShoppingViewModel: CartShoppingViewModel, onAddToCart: () -> Unit, onDismiss: () -> Unit
) {
    val context = LocalContext.current
    // Verifica si el producto ya está en el carrito y si está agotado
    val existingItem = cartShoppingViewModel.CartShopping.value.find { it.id == product.id }
    val isOutOfStock = product.stock == 0
    // Hoja modal inferior para mostrar los detalles del producto
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Cerrar con la Cruz
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(onClick = { onDismiss() }) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrqr")
                }
            }

            // Imagen con animación
            Image(
                painter = rememberAsyncImagePainter(model = product.image),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${product.price} €",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Botón Añadir
            Button(
                onClick = {
                    if (!isOutOfStock) {
                        if (existingItem == null || existingItem.quantity < product.stock) {
                            cartShoppingViewModel.addToCart(product)
                            Toast.makeText(
                                context,
                                "${product.name} Añadido ",
                                Toast.LENGTH_SHORT
                            ).show()
                            onDismiss() // Cerrar Sheet
                        } else {
                            Toast.makeText(
                                context,
                                "Stock Limitado a ${product.stock} unidades ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isOutOfStock) Color.Gray else MaterialTheme.colorScheme.primary
                ),
                enabled = !isOutOfStock
            ) {
                Text(text = if (isOutOfStock) "Agotado " else "Añadir a la Cesta ")
            }
        }
    }
}

