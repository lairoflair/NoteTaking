package com.lairoflair.notes.model
import kotlinx.serialization.Serializable

@Serializable
data class NoteDto (
    val id: String,
    val title: String,
    val content: String,
//    val createdAt: String = "",
    val updatedAt: String = ""
)