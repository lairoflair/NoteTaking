package com.lairoflair.pictureurl.ui.screen.notetaking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun NoteTakingScreen(){
    
    var fileName by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }
}