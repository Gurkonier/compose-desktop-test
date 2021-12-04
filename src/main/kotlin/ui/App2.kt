package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*


@Composable
fun App2(onBack: () -> Unit) {
    var text by remember { mutableStateOf("") }
    var count by remember { mutableStateOf(0) }

    MaterialTheme {
        Column {
            Text("Symbol-Count: $count")
            TextField(text, {
                text = it
                count = it.length
            })
            Button(onClick = onBack) {
                Text("Go back")
            }
        }
    }
}