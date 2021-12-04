package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*


@Composable
@Preview
fun App(onForward: () -> Unit) {
    var text by remember { mutableStateOf("") }
    var count by remember { mutableStateOf(0) }

    MaterialTheme {
        Column {
            Button(onClick = onForward) {
                Text("Go to next Layout")
            }
            Text("Symbol-Count: $count")
            TextField(text, {
                text = it
                count = it.length
            })
        }
    }
}