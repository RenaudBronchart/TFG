package com.example.tfg.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DisplayDate(date: String, fontSize: TextUnit, fontWeight : FontWeight, color : Color,
                modifier: Modifier = Modifier
) {
    Text(
        text = formatDate(date),
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = color,
        modifier = modifier.padding(bottom = 8.dp)
    )
}

fun formatDate(timeStamp: String):String {

    return try {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = Date(timeStamp.toLong())
        return simpleDateFormat.format((date))
    } catch (e: Exception) {
    return "Fecha desconocida"
    }

}