// HomeScreen.kt
package com.lairoflair.notes.ui.screen.home


import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lairoflair.notes.database.DatabaseProvider
import com.lairoflair.notes.model.DeletedNote
import com.lairoflair.notes.model.SyncRequest
import com.lairoflair.notes.network.RetrofitInstance
import com.lairoflair.notes.util.getDeletedNotes
import com.lairoflair.notes.util.getLastSync
import com.lairoflair.notes.util.getLocalNotes
import com.lairoflair.notes.util.saveNotesToLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(

    onNewNoteClick: () -> Unit = {},
    onNoteClick: (String) -> Unit = {}
) {

    val context = LocalContext.current
    val db = DatabaseProvider.getDatabase(context)
    val scope = rememberCoroutineScope()
    val files = remember {
        mutableStateListOf<File>().apply {
            val fileList =
                context.filesDir.listFiles()?.filter { it.extension == "txt" } ?: emptyList()
            addAll(fileList)
        }
    }

    scope.launch(Dispatchers.IO) {
        db.deletedNoteDao().getAll().forEach { note ->
            Log.d("TEST", "Notes in DeletedDB Start: $note")
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📓 Past Notes", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(24.dp))

                Spacer(modifier = Modifier.weight(1f)) // pushes button to the right
                Button(
                    onClick = {
                        scope.launch(Dispatchers.IO) { // ✅ safe coroutine launch
                            try {
                                val response = RetrofitInstance.api.ping()
                                Log.d("TEST", "Server responded: $response")

                                val localNotes = getLocalNotes(context)
                                val lastSync = getLastSync(context)
                                val deletedNotes = getDeletedNotes(context)

                                val syncReq = SyncRequest(
                                    notes = localNotes,
                                    lastSync = lastSync,
                                    deletedNotes = deletedNotes
                                )

                                val updatedNotes = RetrofitInstance.api.syncNotes(syncReq)
                                saveNotesToLocal(context, updatedNotes.notes ?: emptyList())
                                for (note in updatedNotes.notes ?: emptyList()) {
                                    Log.d("TEST", "Updated note: $note")
                                }
                                db.deletedNoteDao().deleteAll()
                                db.deletedNoteDao().getAll().forEach { note ->
                                    Log.d("TEST", "Notes in DeletedDB After Sync: $note")
                                }
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Notes synced", Toast.LENGTH_SHORT)
                                        .show()
                                }
                                    val fileList = context.filesDir.listFiles()?.filter { it.extension == "txt" } ?: emptyList()
                                files.clear()
                                files.addAll(fileList)
//
//                                Log.d("SYNC", "Local notes: ${localNotes.size}, Notes returned from server: ${updatedNotes.size}")
                            } catch (e: Exception) {
//                                Log.e("TEST", "Error connecting to server", e)
                                e.printStackTrace()
                            }
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Sync") // change text/icon as needed
                }
            }

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
                                    val parts = file.nameWithoutExtension.split("-")
                                    val title = parts.first()
                                    val id = if (parts.size > 1) parts.subList(1, parts.size).joinToString("-") else ""
                                    val note = DeletedNote(id, title)
                                    scope.launch(Dispatchers.IO) {
                                        db.deletedNoteDao().getAll().forEach { note ->
                                            Log.d("TEST", "Notes in DeletedDB Before Swipe: $note")
                                        }
                                        db.deletedNoteDao().insert(note)
                                        db.deletedNoteDao().getAll().forEach { note ->
                                            Log.d("TEST", "Notes in DeletedDB After Swipe: $note")
                                        }
                                    }
                                    files.remove(file)
                                    file.delete()
                                    files.remove(file)
                                    Toast.makeText(context, "${file.nameWithoutExtension.substringBefore("-")}.txt has been deleted", Toast.LENGTH_SHORT).show()
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
                                    Text(file.nameWithoutExtension.substringBefore("-"))
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