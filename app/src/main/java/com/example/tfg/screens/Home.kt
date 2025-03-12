package com.example.tfg.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.components.BottomBarComponent
import com.example.tfg.components.CardItemProfile
import com.example.tfg.models.MenuCategory
import com.example.tfg.models.menuItems
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.CartShoppingViewModel
import com.example.tfg.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavHostController, authViewModel: AuthViewModel, userViewModel: UserViewModel, cartShoppingViewModel: CartShoppingViewModel) {

    val usuarioData by userViewModel.usuario.collectAsState()
    val currentUser by authViewModel.user.collectAsState()
    val nombre = usuarioData?.nombre ?: "Usuario desconocido"
    val isAdmin by authViewModel.isAdmin.collectAsState()
    val cartItems by cartShoppingViewModel.CartShopping.collectAsState()

    authViewModel.fetchCurrentUser()

    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { uid ->
            userViewModel.loadUser(uid)  // cargar usuario
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                title = { Text("¡Hola, $nombre! 👋") },
            )
        },
        bottomBar = { BottomBarComponent(navController, cartItems) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),

            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(menuItems.filter { it.category == MenuCategory.HOME && (isAdmin || it.text != "Administrar")} )
                { item ->
                    CardItemProfile(
                        icon = item.icon,
                        text = item.text,
                        onClick = { navController.navigate(item.route) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}






