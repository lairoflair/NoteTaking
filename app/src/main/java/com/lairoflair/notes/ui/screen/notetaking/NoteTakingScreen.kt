package com.lairoflair.notes.ui.screen.notetaking

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.File
import java.util.UUID

@Composable
fun NoteTakingScreen(
    onBack: () -> Unit = {},
    backHome: () -> Unit = {},
    givenFileName: String? = null,
) {
    val context = LocalContext.current

    var fileName by remember { mutableStateOf(givenFileName ?: "") }
    var noteContent by remember { mutableStateOf("") }

    LaunchedEffect(givenFileName) {
        if (givenFileName != null) {
            val file = File(context.filesDir, "$givenFileName.txt")
            noteContent = file.readText()
        } else {
            noteContent = ""
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = if (givenFileName == null) fileName else fileName.substringBefore("-"),
            onValueChange = { fileName = it },
            label = { Text("File Name") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = (givenFileName != null)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = noteContent,
            onValueChange = { noteContent = it },
            label = { Text("Note Content") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            maxLines = 10
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            // Handle save action
            if (givenFileName == null && fileName.isEmpty()) {
                Toast.makeText(context, "No file name", Toast.LENGTH_SHORT).show()
                return@Button
            }
            if (givenFileName == null && fileName.contains('-') || fileName.contains('\n')) {
                Toast.makeText(context, "File cannot contain '-' or '\\n'", Toast.LENGTH_SHORT).show()
                return@Button
            }
            if (givenFileName == null && fileName.isBlank()) {
                Toast.makeText(context, "Please enter a file name", Toast.LENGTH_SHORT).show()
                return@Button
            }

            if (givenFileName == null && fileExists(context, fileName)) {
                Toast.makeText(context, "File already exists", Toast.LENGTH_SHORT).show()
                return@Button
            }
            saveNote(context, fileName, noteContent)
            backHome()
        }) {
            Text("Save Note")
        }
    }
}

fun saveNote(context: Context, fileName: String, noteContent: String) {
    val file = if (fileName.contains("-")) {
        // Already has UUID in fileName, overwrite that file
        File(context.filesDir, "$fileName.txt")
    } else {
        // New note, generate UUID
        val uuid = UUID.randomUUID().toString()
        File(context.filesDir, "$fileName-$uuid.txt")
    }
    file.writeText(noteContent)
}

fun fileExists(context: Context, fileName: String): Boolean {
    // find the first "-" and strip everything after it
    val baseName = if ("-" in fileName) {
        fileName.substringBefore("-")
    } else {
        fileName
    }

    // match against any file starting with that baseName + "-"
    val files = context.filesDir.listFiles() ?: return false
    return files.any { it.name.startsWith("$baseName-") && it.name.endsWith(".txt") }
}
