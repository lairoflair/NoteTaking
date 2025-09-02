package com.lairoflair.notes.network

import com.lairoflair.notes.model.SyncRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NotesApi {
    @POST("/api/notes/sync")
    suspend fun syncNotes(@Body request: SyncRequest): SyncRequest

    @GET("/api/ping")
    suspend fun ping(): String
}
