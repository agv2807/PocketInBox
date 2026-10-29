package com.vazdautsan.pocketinbox.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vazdautsan.pocketinbox.core.database.model.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY created_at DESC, id DESC")
    fun observeAll(): Flow<List<NoteEntity>>

    @Insert
    suspend fun insert(note: NoteEntity): Long
}
