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

//  Representa un ítem del menú de navegación lateral o inferior.
data class MenuItem(val icon: ImageVector, val text: String, val route: String, val category: MenuCategory)

//  Enum para categorizar los ítems del menú.
// enum es una clase especial en Kotlin que define un conjunto de constantes relacionadas.
enum class MenuCategory {HOME,PROFILE,ADMIN}

/**
 * Lista de ítems del menú que se muestran en la aplicación.
 * Cada ítem está asociado a una pantalla específica por su ruta.
 */
val menuItems = listOf(
    MenuItem(Icons.Default.SportsTennis, "Reservar pista", "BookingPadelScreen",MenuCategory.HOME),
    MenuItem(Icons.Default.ShoppingCart, "Tienda", "EshopScreen",MenuCategory.HOME),
    MenuItem(Icons.Default.Person, "Mi Perfil", "Profile",MenuCategory.HOME),
    MenuItem(Icons.Default.AdminPanelSettings, "Administrar", "AdminPanel",MenuCategory.HOME),
    MenuItem(Icons.Default.Addchart, "Agregar Producto", "AdminAddProduct",MenuCategory.ADMIN),
    MenuItem(Icons.Default.Checklist, "Lista de usuarios", "AdminListUsers",MenuCategory.ADMIN),
    MenuItem(Icons.Default.Person, "Editar mi perfil", "EditUser", MenuCategory.PROFILE),
    MenuItem(Icons.Default.Book, "Mis reservas", "ProfileMyBookings", MenuCategory.PROFILE),
    MenuItem(Icons.Default.ShoppingCart, "Mis compras", "ProfileMyOrders", MenuCategory.PROFILE),
    MenuItem(Icons.AutoMirrored.Filled.ExitToApp, "Desconectar", "Logout", MenuCategory.PROFILE)

)