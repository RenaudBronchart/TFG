package com.example.tfg.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.viewmodel.ProductoViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SportsBaseball
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.tfg.models.Producto
import com.example.tfg.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EshopScreen(navController: NavHostController, authViewModel : AuthViewModel, productoViewModel: ProductoViewModel) {
    var selectedCategory by remember { mutableStateOf("All") }
    val productos by productoViewModel.productos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                title = { Text("E-Shop") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("Login") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "volver",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding -> //
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            // Filtres
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CategoryIcon("Pala", Icons.Default.SportsTennis, selectedCategory) {
                    selectedCategory = "Pala"
                }
                CategoryIcon("Pelotas", Icons.Default.SportsBaseball, selectedCategory) {
                    selectedCategory = "Pelotas"
                }
            }

            // Affichage des produits
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize()
            ) {
                items(productos) { producto ->
                    ProductCard(producto,navController,productoViewModel)

                }
            }
        }
    }
}

@Composable
fun CategoryIcon(name: String, icon: ImageVector, selectedCategory: String, onClick: () -> Unit) {
  Card(
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = if (selectedCategory == name) Color.LightGray else Color.White),
      modifier = Modifier
          .size(60.dp)  // size caja
          .shadow(8.dp, RoundedCornerShape(12.dp)) // shape caja
          .clickable(onClick = onClick)
  ) {
      Column(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
      ) {
          Icon(
              imageVector = icon,
              contentDescription = name,
              tint = if (selectedCategory == name) Color.Blue else Color.Gray
          )
          Text(text = name, color = Color.Black)
      }
  }
}

@Composable
fun ProductCard(producto:Producto, /*isAdmin:Boolean,*/ navController : NavHostController, productoViewModel: ProductoViewModel ) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(270.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
      Column(
          modifier = Modifier
              .fillMaxSize()
              .padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
      ) {
          AsyncImage(
              model = producto.imagen, //
              contentDescription = producto.nombre,
              modifier = Modifier
                  .size(160.dp)
                  .clip(RoundedCornerShape(8.dp)),
              contentScale = ContentScale.Crop
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(text = producto.nombre, fontWeight = FontWeight.Bold)
          Text(text = "${producto.precio} €", color = Color.Black)

          /*if (isAdmin) {*/
            Spacer(modifier = Modifier.height(8.dp))
              Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceEvenly
              ) {
                  Button(
                      onClick = {
                          navController.navigate("EditProduct/${producto.id}") // Redirige vers l'édition
                      },
                      colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                  ) {
                      Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                      Text("Editar", color = Color.White)
                  }

                  Button(
                      onClick = {
                          productoViewModel.deleteProduct(producto.id) { message ->
                              productoViewModel.setMessageConfirmation(message)
                          }
                      },
                      colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                  ) {
                      Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                      Text("Eliminar", color = Color.White)
                  }
              }
          /*}*/
      }
    }
}


