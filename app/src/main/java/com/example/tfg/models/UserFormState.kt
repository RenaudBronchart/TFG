package com.example.tfg.models
/**
 * Clase que representa el estado del formulario de registro de usuario.
 * Contiene los campos necesarios y una función de validación.
 */
data class UserFormState(
    val name: String = "",
    val firstname: String = "",
    val dni: String = "",
    val email: String = "",
    val phone: String = "",
    val gender: String = "",
    val birthday: String = "",
    val password: String = ""
) {

    // Método para validar si todos los campos son válidos
    fun isValid(): Boolean =
        name.isNotEmpty() && firstname.isNotEmpty() && dni.isNotEmpty() && email.isNotEmpty() &&
                phone.isNotEmpty() && gender.isNotEmpty() && birthday.isNotEmpty() && password.isNotEmpty()

}