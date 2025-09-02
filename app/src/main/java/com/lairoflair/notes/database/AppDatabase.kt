package com.lairoflair.notes.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.lairoflair.notes.dao.DeletedNoteDao
import com.lairoflair.notes.model.DeletedNote

@Database(entities = [DeletedNote::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deletedNoteDao(): DeletedNoteDao
}
