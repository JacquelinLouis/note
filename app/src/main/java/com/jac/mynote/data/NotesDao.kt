package com.jac.mynote.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jac.mynote.data.NoteEntity

@Dao
interface NotesDao {
    @Query("SELECT * FROM NoteEntity")
    fun getAll(): LiveData<List<NoteEntity>>

    @Insert
    suspend fun insertNoteEntities(vararg noteEntity: NoteEntity)

    @Update
    suspend fun updateNoteEntity(noteEntity: NoteEntity)

    @Delete
    suspend fun deleteNoteEntity(noteEntity: NoteEntity)

    @Query("DELETE FROM NoteEntity WHERE uid = :id")
    suspend fun deleteNoteEntity(id: Int);
}