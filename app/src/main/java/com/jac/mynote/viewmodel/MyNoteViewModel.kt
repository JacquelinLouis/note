package com.jac.mynote.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jac.mynote.data.NoteEntity
import com.jac.mynote.model.Note

class MyNoteViewModel : ViewModel() {
    companion object {
        const val DEFAULT_POSITION: Int = -1
        const val NEW_POSITION: Int = -2
    }

    var notes : MutableLiveData<ArrayList<Note>> = MutableLiveData()
    var position : MutableLiveData<Int> = MutableLiveData(DEFAULT_POSITION)

    private fun updateNotes() {
        var newNotes = notes.value
        if (newNotes == null) newNotes = ArrayList()
        notes.value = newNotes
    }

    fun setPosition(position: Int) {
        this.position.value = position
    }

    fun getPosition(): Int? {
        return this.position.value
    }

    fun addNote(note: Note) {
        if (notes.value == null) {
            val list = ArrayList<Note>()
            list.add(note)
            notes.value = list
        } else {
            notes.value?.add(note)
            updateNotes()
        }
    }

    fun getNote(position: Int): Note? {
        if (position < 0 || notes.value?.size ?: 0 <= position) return null
        return notes.value?.get(position)
    }

    fun setCurrentNote(note: Note) {
        val position = getPosition()
        if (position == null || notes.value == null) return
        notes.value?.set(position, note)
        updateNotes()
    }

    fun setNotes(notesEntities: List<NoteEntity>) {
        val notes = NotesAdapter.fromModelToView(notesEntities)
        this.notes.postValue(ArrayList(notes))
    }

    fun getCurrentNote(): Note? {
        return if (getPosition() == null) null else getNote(position.value!!)
    }

    fun deleteNote(position: Int) {
        val note = getNote(position)
        if (note != null) {
            note.id = Note.OLD_INSTANCE_ID
            updateNotes()
            
        }
    }
}