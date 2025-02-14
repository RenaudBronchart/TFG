package com.example.tfg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.tfg.navigation.AppNavigation
import com.example.tfg.ui.theme.TFGTheme
import com.example.tfg.viewmodel.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // viewModelProvider para obtener la instancia de AuthViewModel
        //para poder recuperar el estado de la autenticación del usuario
        //esta instancia permite persistir durante toda la vida de la actividad
        // como por ejemplo la rotación de la pantalla
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        enableEdgeToEdge()
        setContent {
            TFGTheme {
                AppNavigation(authViewModel)
            }
        }
    }
}



