package com.lairoflair.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lairoflair.notes.ui.theme.PictureURLTheme
import androidx.compose.foundation.layout.systemBarsPadding
import com.lairoflair.notes.ui.navigation.NoteAppNavigation


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            PictureURLTheme {
                Scaffold( modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                )
                { innerPadding ->
                    NoteAppNavigation(
//                        modifier = Modifier.padding(innerPadding)
                    )
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PictureURLTheme {
        Greeting("Android")
    }
}