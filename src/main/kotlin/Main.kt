// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

interface Navigation {
    fun toApp()
    fun toApp2()
}

var navigation: Navigation? = null

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("") }
    var count by remember { mutableStateOf(0) }

    DesktopMaterialTheme {
        Column {
            Button(onClick = {
                navigation?.toApp2()
            }) {
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

@Composable
fun App2() {
    var text by remember { mutableStateOf("") }
    var count by remember { mutableStateOf(0) }

    DesktopMaterialTheme {
        Column {
            Text("Symbol-Count: $count")
            TextField(text, {
                text = it
                count = it.length
            })
            Button(onClick = {
                navigation?.toApp()
            }) {
                Text("Go back")
            }
        }
    }
}

enum class Windows {
    APP,
    APP2
}

fun main() = application {

    var screen by remember { mutableStateOf(Windows.APP) }
    navigation = object : Navigation {
        override fun toApp() {
            screen = Windows.APP
        }

        override fun toApp2() {
            screen = Windows.APP2
        }
    }

    Window(onCloseRequest = ::exitApplication) {
        when(screen){
            Windows.APP -> App()
            Windows.APP2 -> App2()
        }
    }
    navigation?.toApp()
}
