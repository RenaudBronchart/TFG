package com.example.tfg.viewmodel


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tfg.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.*


class EditUserViewModel : ViewModel() {


    private val _nombre = mutableStateOf("")
    val nombre: State<String> get() = _nombre

    private val _apellido = mutableStateOf("")
    val apellido: State<String> get() = _apellido

    private val _email = mutableStateOf("")
    val email: State<String> get() = _email

    private val _telefono = mutableStateOf("")
    val telefono: State<String> get() = _telefono

    private val _dni = mutableStateOf("")
    val dni: State<String> get() = _dni

    private val _genero = mutableStateOf("")
    val genero: State<String> get() = _genero

    private val _fechaNacimiento = mutableStateOf("")
    val fechaNacimiento: State<String> get() = _fechaNacimiento

    private val _messageConfirmation = MutableStateFlow("")
    val mensajeConfirmacion: StateFlow<String> get() = _messageConfirmation

    // 🛠 Fonctions pour mettre à jour les champs (au lieu d'accéder aux variables privées)
    fun setNombre(value: String) { _nombre.value = value }
    fun setApellido(value: String) { _apellido.value = value }
    fun setEmail(value: String) { _email.value = value }
    fun setTelefono(value: String) { _telefono.value = value }
    fun setDni(value: String) { _dni.value = value }
    fun setGenero(value: String) { _genero.value = value }
    fun setFechaNacimiento(value: String) { _fechaNacimiento.value = value }

    // Permet de charger les données utilisateur depuis Usuario
    fun loadDataUser(usuario: User?) {
        usuario?.let {
            setNombre(it.nombre ?: "")
            setApellido(it.apellido ?: "")
            setEmail(it.email ?: "")
            setTelefono(it.telefono ?: "")
            setDni(it.dni ?: "")
            setGenero(it.genero ?: "")
            setFechaNacimiento(it.fechaNacimiento ?: "")
        }
    }

    fun saveUsuario(uid: String, onSuccess: (String) -> Unit) {
        val usuario = User(
            nombre = _nombre.value,
            apellido = _apellido.value,
            email = _email.value,
            telefono = _telefono.value,
            dni = _dni.value,
            genero = _genero.value,
            fechaNacimiento = _fechaNacimiento.value
        )

        FirebaseFirestore.getInstance()
            .collection("usuarios")
            .document(uid)
            .set(usuario)
            .addOnSuccessListener {
                // On envoie un message de succès lorsque la sauvegarde réussit
                _messageConfirmation.value = "Usuario actualizado correctamente"
                onSuccess(_messageConfirmation.value) // Appeler la fonction `onSuccess` et passer le message
            }
            .addOnFailureListener { exception ->
                // En cas d'erreur, on envoie un message d'échec
                _messageConfirmation.value = "Error al actualizar el usuario: ${exception.message}"
                onSuccess(_messageConfirmation.value) // Appeler `onSuccess` avec le message d'erreur
            }
    }    // Fonction pour mettre à jour les données de l'utilisateur

    fun setMensajeConfirmacion(mensaje: String) {
        _messageConfirmation.value = mensaje
    }
}

