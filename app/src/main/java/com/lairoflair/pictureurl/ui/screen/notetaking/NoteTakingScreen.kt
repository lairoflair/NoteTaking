package com.lairoflair.pictureurl.ui.screen.notetaking

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.File

@Composable
fun NoteTakingScreen(){
    val context = LocalContext.current

    var fileName by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }

Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = fileName,
            onValueChange = { fileName = it },
            label = { Text("File Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = noteContent,
            onValueChange = { noteContent = it },
            label = { Text("Note Content") },
            modifier = Modifier.fillMaxWidth().height(200.dp),
            maxLines = 10
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            // Handle save action
        }) {
            Text("Save Note")
        }
    }
}