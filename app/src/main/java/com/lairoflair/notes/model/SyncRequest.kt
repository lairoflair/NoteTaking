package com.lairoflair.notes.model

data class SyncRequest(
    val notes: List<NoteDto>? = emptyList(),
    val lastSync: String? = null,
    val deletedNotes: List<String>? = emptyList()
)
