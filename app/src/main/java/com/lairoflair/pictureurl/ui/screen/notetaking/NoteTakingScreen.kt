package com.lairoflair.pictureurl.ui.screen.notetaking

import androidx.compose.runtime.Composable

@Composable
fun NoteTakingScreen(){
    
    var fileName by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }
}