package com.example.tfg.screens
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.components.CardItem
import com.example.tfg.components.TopBarComponent
import com.example.tfg.models.MenuCategory
import com.example.tfg.models.menuItems
import com.example.tfg.viewmodel.AuthViewModel
import com.example.tfg.viewmodel.UserViewModel


@Composable
fun AdminPage(navHostController: NavHostController, authViewModel: AuthViewModel, userViewModel: UserViewModel) {
    val usuarioData by userViewModel.usuario.collectAsState()
    val nombre = usuarioData?.nombre ?: "Usuario desconocido"
    val isAdmin by authViewModel.isAdmin.collectAsState()

    Scaffold(
        topBar = { TopBarComponent("Administrador pagina", navHostController) },
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
                items(menuItems.filter { it.category == MenuCategory.ADMIN})
                { item ->
                    CardItem(
                        icon = item.icon,
                        text = item.text,
                        onClick = { navHostController.navigate(item.route) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}






