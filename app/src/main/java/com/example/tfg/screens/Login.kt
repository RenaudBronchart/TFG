@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tfg.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tfg.R
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.platform.LocalContext
import com.example.tfg.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException


@Composable
fun Login(navController: NavHostController,authViewModel : AuthViewModel) {
    val context = LocalContext.current // val porque no va cambiar y permite acceder al contexto
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) } // Estado para mostrar/ocultar la contraseña

    // Contenedor principal con fondo de color primario
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Logo del club
            Image(
                painter = painterResource(id = R.drawable.test3),
                contentDescription = "Logo Padel Club Shones",
                modifier = Modifier
                    .height(180.dp)
                    .padding(bottom = 20.dp)
            )

            // Campo de usuario
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.White) },
                textStyle = TextStyle(color = Color.White),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp), // Bordes redondeados
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color.White
                ),
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "Usuario", tint = Color.White)
                }
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espaciado entre los campos

            // Campo de contraseña con opción de mostrar/ocultar
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = Color.White) },
                textStyle = TextStyle(color = Color.White),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp), // Bordes redondeados
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color.White
                ),
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Contraseña", tint = Color.White)
                },
                trailingIcon = { // Botón para mostrar/ocultar contraseña
                    val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = Color.White
                        )
                    }
                },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None // Mostrar texto normal
                else
                    PasswordVisualTransformation() // Ocultar la contraseña con puntos
            )

            Spacer(modifier = Modifier.height(32.dp)) // Espaciado más grande

            // Botón "Iniciar Sesión"
            Button(onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context,"Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show()
                } else {
                    authViewModel.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            navController.navigate("Home")
                            Log.i("jc","inicio de sesion correcto")
                        } else {
                            val exception = task.exception
                            val errorMessage = when(exception) {
                                is FirebaseAuthInvalidUserException -> "Usuario o Contraseña incorrecta"
                                is FirebaseAuthInvalidCredentialsException -> "Usuario o Contraseña incorrecta"
                                else -> "Error de conexión, intenta más tarde"
                            }
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                            Log.i("jc", "Inicio de sesión fallado")
                        }
                    }
                }
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Iniciar sesión",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp)) // Espaciado entre botones

            // Botón "Registrarse"
            Button(
                onClick = { navController.navigate("signUp") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp) // Bordes redondeados
            ) {
                Text(
                    text = "Registrate",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )
            }
        }
    }
}



