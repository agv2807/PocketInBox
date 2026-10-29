package com.vazdautsan.pocketinbox.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    /** Epoch milliseconds, UTC. */
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
)
