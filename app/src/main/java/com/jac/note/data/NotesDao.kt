package com.jac.note.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jac.note.data.NoteEntity

/** Notes data object, used to get, add, update or delete notes. */
@Dao
interface NotesDao {
    /**
     * Getter to retrieve all {@link NoteEntity} at once.
     *
     * @return the whole list in a {@link LiveData} object.
     * Each note add, update or delete of a component of this list will be notified to listeners.
     */
    @Query("SELECT * FROM NoteEntity")
    fun getAll(): LiveData<List<NoteEntity>>

    /**
     * Creator for one or more {@link NoteEntity}.
     *
     * @param noteEntity variable number of {@link NoteEntity} to insert in the database.
     */
    @Insert
    suspend fun insertNoteEntities(vararg noteEntity: NoteEntity)

    /**
     * Update the given {@link NoteEntity}, retrieving the instance to update by its identifier.
     *
     * @param noteEntity the {@link NoteEntity} to update.
     */
    @Update
    suspend fun updateNoteEntity(noteEntity: NoteEntity)

    /**
     * Delete the given {@link NoteEntity} instance.
     * Check all it members before deletion.
     *
     * @param noteEntity the {@link NoteEntity} to delete.
     */
    @Delete
    suspend fun deleteNoteEntity(noteEntity: NoteEntity)

    /**
     * Delete the given {@link NoteEntity} instance.
     *
     * @param id the {@link NoteEntity} to delete identifier.
     */
    @Query("DELETE FROM NoteEntity WHERE uid = :id")
    suspend fun deleteNoteEntity(id: Int)

    @Query("DELETE FROM NoteEntity")
    suspend fun deleteNoteEntities()
}