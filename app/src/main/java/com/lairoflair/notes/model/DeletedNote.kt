package com.lairoflair.notes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_notes")
data class DeletedNote(
    @PrimaryKey val id: String,
    val fileName: String,
    val deletedAt: Long = System.currentTimeMillis()
)
