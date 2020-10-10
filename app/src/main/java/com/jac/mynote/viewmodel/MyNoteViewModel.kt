package com.jac.mynote.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.jac.mynote.data.MyNoteDatabase
import com.jac.mynote.data.NoteEntity
import com.jac.mynote.model.Note
import kotlinx.coroutines.launch

class MyNoteViewModel(application: Application) : AndroidViewModel(application) {

    private var myNoteDatabase: MyNoteDatabase = MyNoteDatabase.getInstance(application)

    var notes : LiveData<List<Note>>
    var currentNote : MutableLiveData<Note?> = MutableLiveData()

    init {
        notes = Transformations.map<List<NoteEntity>, List<Note>>(
            myNoteDatabase.getNotesDao().getAll()) {
                noteEntities -> NotesAdapter.fromModelToView(noteEntities)
            }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            myNoteDatabase.getNotesDao().insertNoteEntities(NoteAdapter.fromViewToModel(note))
        }
    }

    fun getNote(position: Int): Note? {
        if (position < 0 || notes.value?.size ?: 0 <= position) return null
        return notes.value?.get(position)
    }

    fun setNote(note: Note) {
        when (note.id) {
            Note.NEW_INSTANCE_ID -> addNote(note)
            else -> viewModelScope.launch {
                myNoteDatabase.getNotesDao().updateNoteEntity(NoteAdapter.fromViewToModel(note))
            }
        }
    }

    fun deleteNote(position: Int) {
        val note = getNote(position)
        if (note != null) deleteNote(note)
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            myNoteDatabase.getNotesDao().deleteNoteEntity(note.id)
        }
    }


    fun getCurrentNote(): Note? {
        return currentNote.value
    }

    fun setCurrentNote(note: Note?) {
        currentNote.value = note
    }

    fun setCurrentNote(position: Int) {
        currentNote.value = getNote(position)
    }
}