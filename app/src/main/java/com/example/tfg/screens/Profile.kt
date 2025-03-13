package com.example.tfg.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tfg.components.BottomBarComponent
import com.example.tfg.components.CardItemProfile
import com.example.tfg.components.TopBarComponent
import com.example.tfg.models.MenuCategory
import com.example.tfg.models.menuItems
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.CartShoppingViewModel
import com.example.tfg.viewmodel.UserViewModel

@Composable
fun Profile(navHostController: NavHostController, authViewModel: AuthViewModel, userViewModel: UserViewModel, cartShoppingViewModel: CartShoppingViewModel) {

    val usuarioData by userViewModel.usuario.collectAsState()
    Log.d("DEBUG", "Usuario Data: $usuarioData")
    val nombre = usuarioData?.nombre ?: "Usuario desconocido"
    val firebaseUser = authViewModel.user.collectAsState().value
    val userId = authViewModel.currentUserId.value ?: ""
    val cartItems by cartShoppingViewModel.CartShopping.collectAsState()

    LaunchedEffect(firebaseUser?.uid) {
        firebaseUser?.let {
            userViewModel.loadUser(it.uid)
            Log.d("DEBUG", "Firebase User ID: ${it.uid}")
        }
    }
    Scaffold(
        topBar = { TopBarComponent("Perfil",navHostController) },
        bottomBar = { BottomBarComponent(navHostController, cartItems) }
    )  { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(50.dp),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "¡Hola, $nombre! 👋",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(menuItems.filter { it.category == MenuCategory.PROFILE }) { item ->
                    CardItemProfile(
                        icon = item.icon,
                        text = item.text,
                        onClick = {
                            when (item.route) {
                                "Logout" -> {
                                    authViewModel.signOut()
                                    navHostController.navigate("Login") {
                                        popUpTo("Login") { inclusive = true }
                                    }
                                }
                                "EditUser" -> {
                                    navHostController.navigate("editUser/$userId")
                                }
                                else -> {
                                    navHostController.navigate(item.route)
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

