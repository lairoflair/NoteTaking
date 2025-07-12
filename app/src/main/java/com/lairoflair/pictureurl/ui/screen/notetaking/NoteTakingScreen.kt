package com.lairoflair.pictureurl.ui.screen.notetaking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

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