// HomeScreen.kt
package com.lairoflair.pictureurl.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onNewNoteClick: () -> Unit = {},
    onNoteClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val files = context.filesDir.listFiles()?.filter { it.extension == "txt" } ?: emptyList()

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
        ) {
            Text("📓 Past Notes", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))

            files.forEach { file ->
                Button(
                    onClick = { onNoteClick(file.name) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(file.nameWithoutExtension)
                }
            }
        }

        Button(
            onClick = onNewNoteClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("+")
        }
    }
}

