package com.jac.mynote.data

import androidx.room.*
import com.jac.mynote.data.NoteEntity

@Dao
interface NotesDao {
    @Query("SELECT * FROM NoteEntity")
    fun getAll(): List<NoteEntity>

    @Insert
    fun insertNoteEntities(vararg noteEntity: NoteEntity)

    @Update
    fun updateNoteEntity(noteEntity: NoteEntity)

    @Delete
    fun deleteNoteEntity(noteEntity: NoteEntity)
}