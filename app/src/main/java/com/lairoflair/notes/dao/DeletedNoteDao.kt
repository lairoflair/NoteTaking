package com.lairoflair.notes.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lairoflair.notes.model.DeletedNote

@Dao
interface DeletedNoteDao {

    @Insert
    suspend fun insert(deletedNote: DeletedNote)

    @Query("SELECT * FROM deleted_notes")
    suspend fun getAll(): List<DeletedNote>

    @Query("DELETE FROM deleted_notes WHERE id = :noteId")
    suspend fun deleteById(noteId: String)

    @Query("DELETE FROM deleted_notes")
    suspend fun deleteAll()
}