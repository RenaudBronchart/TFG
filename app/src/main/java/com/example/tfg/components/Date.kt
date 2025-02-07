package com.example.tfg.components



import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun Date(selectedDate: String, onDateChange: (String) -> Unit) {

    val context = LocalContext.current

    val openDatePicker: () -> Unit = {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, monthOfYear, dayOfMonth ->
                val formattedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                onDateChange(formattedDate) // Update the selected date in the parent composable
            },
            year, month, dayOfMonth
        )

        datePickerDialog.show()
    }

        TextField(
            value = selectedDate,
            onValueChange = {},
            label = { Text("Fecha de Nacimiento") },
            readOnly = true, // Rendre le TextField en lecture seule
            modifier = Modifier
                .fillMaxWidth(),
            trailingIcon  = {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendario",
                    tint = Color.Gray, //
                    modifier = Modifier.clickable { openDatePicker() } // para abrir el calendario al hacer clic en el icono

                )
            }
        )


}

