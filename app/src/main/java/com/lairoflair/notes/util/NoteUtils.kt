package com.lairoflair.notes.util

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import com.lairoflair.notes.database.DatabaseProvider
import com.lairoflair.notes.model.NoteDto
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
suspend fun getLocalNotes(context: Context): List<NoteDto> {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    return context.filesDir.listFiles()
        ?.filter { it.extension == "txt" }
        ?.map { file ->
            val parts = file.nameWithoutExtension.split("-")
            val title = parts.first()
            val id = if (parts.size > 1) parts.subList(1, parts.size).joinToString("-") else ""
            Log.d("TEST", "id: $id")
            NoteDto(
                id = id,
                title = title,
                content = file.readText(),
//                createdAt = Instant.ofEpochMilli(file.lastModified()) // fallback to lastModified
//                    .atZone(ZoneId.systemDefault())
//                    .format(formatter),
                updatedAt = Instant.ofEpochMilli(file.lastModified())
                    .atZone(ZoneId.systemDefault())
                    .format(formatter)
            )
        } ?: emptyList()
}

fun saveLastSync(context: Context, lastSync: String) {
    val prefs = context.getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)
    prefs.edit { putString("last_sync", lastSync) }
}

fun getLastSync(context: Context): String? {
    val prefs = context.getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)
    return prefs.getString("last_sync", null)
}


fun saveNotesToLocal(context: Context, notes: List<NoteDto>) {
    notes.forEach { note ->
        val fileName = "${note.title}-${note.id}.txt"  // stable, matches server ID
        val file = File(context.filesDir, fileName)
        file.writeText(note.content)  // overwrite existing content
    }
}


suspend fun getDeletedNotes(context: Context): List<String> {
    val db = DatabaseProvider.getDatabase(context)
    return db.deletedNoteDao().getAll().map { it.id }
}


