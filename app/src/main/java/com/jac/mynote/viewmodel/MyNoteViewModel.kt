package com.jac.mynote.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import com.jac.mynote.data.MyNoteDatabase
import com.jac.mynote.data.NoteEntity
import com.jac.mynote.json.Notes
import com.jac.mynote.model.Note
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/** View-model entry point, store view-related data and update database accordingly. */
class MyNoteViewModel(application: Application) : AndroidViewModel(application) {

    /** Database storing application's data. */
    private var myNoteDatabase: MyNoteDatabase = MyNoteDatabase.getInstance(application)

    /** Live data of items list view. */
    var notes : LiveData<List<Note>>

    /** Live data of current note (creation or update). */
    var currentNote : MutableLiveData<Note?> = MutableLiveData()

    /** Class initialization with constructor parameters. */
    init {
        notes = Transformations.map<List<NoteEntity>, List<Note>>(
            myNoteDatabase.getNotesDao().getAll()) {
                noteEntities -> NotesAdapter.fromModelToView(noteEntities)
            }
    }

    /**
     * Save the new note in the database.
     *
     * @param note the view note to convert to model and insert in the database.
     */
    fun addNote(note: Note) {
        viewModelScope.launch {
            myNoteDatabase.getNotesDao().insertNoteEntities(NoteAdapter.fromViewToModel(note))
        }
    }

    /**
     * Get the note from notes live data/database list.
     * @param position the position to get data at in notes list.
     */
    fun getNote(position: Int): Note? {
        if (position < 0 || notes.value?.size ?: 0 <= position) return null
        return notes.value?.get(position)
    }

    /**
     * Update or create the given view note in model notes list, depending on it identifier.
     * @param note the note to create or update.
     * If the note identifier is {@link Note.NEW_INSTANCE_ID}, the note will be created, else it
     * will replace the one with the same identifier (if exist).
     */
    fun setNote(note: Note) {
        when (note.id) {
            Note.NEW_INSTANCE_ID -> addNote(note)
            else -> viewModelScope.launch {
                myNoteDatabase.getNotesDao().updateNoteEntity(NoteAdapter.fromViewToModel(note))
            }
        }
    }

    /**
     * Delete the note at the given position in notes list.
     * @param position the position, in the list, of the note to delete.
     */
    fun deleteNote(position: Int) {
        val note = getNote(position)
        if (note != null) deleteNote(note)
    }

    /**
     * Delete the given note in the list.
     * @param note the note to delete, identified by its identifier.
     */
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            myNoteDatabase.getNotesDao().deleteNoteEntity(note.id)
        }
    }

    /**
     * Delete all notes.
     */
    fun deleteNotes() {
        viewModelScope.launch { myNoteDatabase.getNotesDao().deleteNoteEntities() }
    }

    /**
     * Get the current note (creation or update) if exist.
     * @return the current note if exist. Else return null.
     */
    fun getCurrentNote(): Note? {
        return currentNote.value
    }

    /**
     * Set the current note (creation for a note with {@link Note.NEW_INSTANCE_ID} identifier,
     * update for an other).
     * @param note  the note to set as current. Can be null.
     */
    fun setCurrentNote(note: Note?) {
        currentNote.value = note
    }

    /**
     * Set the current note with existing one in the list.
     * @param position the position of the note, in the list, to set as current one.
     * If the position is invalid, null will be set as current note.
     */
    fun setCurrentNote(position: Int) {
        currentNote.value = getNote(position)
    }

    /**
     * Export database to the given directory.
     * @param context the Android context to get contentResolver from.
     * @param uri the uri to export data to.
     */
    fun export(context: Context, uri: Uri) {
        val notes = notes.value
        if (notes != null) {
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "w")
            val fileDescriptor = parcelFileDescriptor?.fileDescriptor
            val fileOutputStream = FileOutputStream(fileDescriptor)
            Notes.serialize(NotesAdapter.fromViewToModel(notes), fileOutputStream)
            fileOutputStream.close()
        }
    }

    /**
     * Import database from the given file.
     * @param context the Android context to get contentResolver from.
     * @param uri the uri to export data to.
     */
    fun import(context: Context, uri: Uri) {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor = parcelFileDescriptor?.fileDescriptor
        val fileInputStream = FileInputStream(fileDescriptor)
        val noteEntities = Notes.deserialize(fileInputStream)
        viewModelScope.launch {
            noteEntities.forEach{ myNoteDatabase.getNotesDao().insertNoteEntities(it) }
        }
        fileInputStream.close()
    }
}