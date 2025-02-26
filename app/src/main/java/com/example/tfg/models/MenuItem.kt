package com.example.tfg.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Addchart
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(val icon: ImageVector, val text: String, val route: String, val category: MenuCategory)

enum class MenuCategory {HOME,PROFILE,ADMIN}

val menuItems = listOf(
    MenuItem(Icons.Default.SportsTennis, "Reservar pista", "BookingPadelScreen",MenuCategory.HOME),
    MenuItem(Icons.Default.ShoppingCart, "Tienda", "EshopScreen",MenuCategory.HOME),
    MenuItem(Icons.Default.Person, "Mi Perfil", "Profile",MenuCategory.HOME),
    MenuItem(Icons.Default.AdminPanelSettings, "Administrar", "AdminPanel",MenuCategory.HOME),
    MenuItem(Icons.Default.Addchart, "Agregar Producto", "AddProduct",MenuCategory.ADMIN),
    MenuItem(Icons.Default.Checklist, "Lista de usuarios", "ListUsers",MenuCategory.ADMIN),
    MenuItem(Icons.Default.Person, "Editar mi perfil", "EditUser", MenuCategory.PROFILE),
    MenuItem(Icons.Default.Book, "Mis reservas", "MyBookings", MenuCategory.PROFILE),
    MenuItem(Icons.Default.ShoppingCart, "Mis compras", "MyPurchases", MenuCategory.PROFILE),
    MenuItem(Icons.AutoMirrored.Filled.ExitToApp, "Desconectar", "Logout", MenuCategory.PROFILE)

)