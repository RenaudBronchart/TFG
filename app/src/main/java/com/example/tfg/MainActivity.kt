package com.example.tfg

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tfg.navigation.AppNavigation
import com.example.tfg.repository.AuthRepository
import com.example.tfg.repository.AuthRepositoryImpl
import com.example.tfg.ui.theme.TFGTheme
import com.example.tfg.viewmodel.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    private lateinit var authRepository: AuthRepository
    private lateinit var authViewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crea una instancia de tu repositorio
        authRepository = AuthRepositoryImpl(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

        // Crea el ViewModel pasando el repositorio
        authViewModel = AuthViewModel(authRepository)

        enableEdgeToEdge()
        setContent {
            TFGTheme {
                AppNavigation(authViewModel)
            }
        }
    }
}



