// HomeScreen.kt
package com.lairoflair.notes.ui.screen.home

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.File


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(
    onNewNoteClick: () -> Unit = {},
    onNoteClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val files = remember {
        mutableStateListOf<File>().apply {
            val fileList =
                context.filesDir.listFiles()?.filter { it.extension == "txt" } ?: emptyList()
            addAll(fileList)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
        ) {
            Text("📓 Past Notes", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))


            if (files.isEmpty()) {
//                    item {
                Text(text = "No notes found")
//                    }
            } else {
                LazyColumn {
                    items(files, key = { it.name }) { file ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            positionalThreshold = { it * 0.8f },
                            confirmValueChange = {
                                if (it == SwipeToDismissBoxValue.StartToEnd || it == SwipeToDismissBoxValue.EndToStart) {
                                    file.delete()
                                    files.remove(file)
                                    Toast.makeText(context, "${file.name} has been deleted", Toast.LENGTH_SHORT).show()
                                    true

                                }
                                else
                                    false
                            }

                        )
                        SwipeToDismissBox(
                            state = dismissState,
                            backgroundContent = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Deleting...", color = MaterialTheme.colorScheme.error)
                                }
                            },
                            content = {
                                val alpha = if (dismissState.progress > 0.99f)
                                    1f
                                else
                                    1f - dismissState.progress.coerceIn(0f, 1f)
                                Button(
                                    onClick = { onNoteClick(file.nameWithoutExtension) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .graphicsLayer { this.alpha = alpha }
                                ) {
                                    Text(file.nameWithoutExtension)
                                }
                            }
                        )
                    }
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