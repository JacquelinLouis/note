package com.jac.mynote.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.jac.mynote.data.MyNoteDatabase
import com.jac.mynote.data.NoteEntity
import com.jac.mynote.model.Note
import kotlinx.coroutines.launch

class MyNoteViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val DEFAULT_POSITION: Int = -1
        const val NEW_POSITION: Int = -2

        const val TEXT_TYPE: Int = 0
        const val PASSWORD_TYPE: Int = 1
    }

    private var myNoteDatabase: MyNoteDatabase = MyNoteDatabase.getInstance(application)

    var notes : LiveData<List<Note>>
    var position : MutableLiveData<Int> = MutableLiveData(DEFAULT_POSITION)
    var type : MutableLiveData<Int> = MutableLiveData(TEXT_TYPE)

    init {
        notes = Transformations.map<List<NoteEntity>, List<Note>>(
            myNoteDatabase.getNotesDao().getAll()) {
                noteEntities -> NotesAdapter.fromModelToView(noteEntities)
            }
    }

    fun setPosition(position: Int) {
        this.position.value = position
    }

    fun getPosition(): Int? {
        return this.position.value
    }

    fun setType(type: Int) {
        this.type.value = type
    }

    fun getType(): Int? {
        return this.type.value
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
        viewModelScope.launch {
            myNoteDatabase.getNotesDao().updateNoteEntity(NoteAdapter.fromViewToModel(note))
        }
    }

    fun getCurrentNote(): Note? {
        return if (getPosition() == null) null else getNote(position.value!!)
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
}