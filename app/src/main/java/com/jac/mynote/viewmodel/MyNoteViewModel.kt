package com.jac.mynote.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jac.mynote.model.Note

class MyNoteViewModel : ViewModel() {
    companion object {
        const val DEFAULT_POSITION: Int = -1
        const val NEW_POSITION: Int = -2
    }

    var notes : MutableLiveData<ArrayList<Note>> = MutableLiveData()
    var position : MutableLiveData<Int> = MutableLiveData(DEFAULT_POSITION)

    fun setPosition(position: Int) {
        this.position.value = position
    }

    fun getPosition(): Int? {
        return this.position.value
    }

    fun addNote(note: Note) {
        var newNotes = notes.value
        if (newNotes == null) newNotes = ArrayList()
        newNotes.add(note)
        notes.value = newNotes
    }

    fun getNote(position: Int): Note? {
        if (position < 0 || notes.value?.size ?: 0 <= position) return null
        return notes.value?.get(position)
    }

    fun getCurrentNote(): Note? {
        return if (getPosition() == null) null else getNote(position.value!!)
    }

    fun deleteNote(position: Int) {
        val note = getNote(position)
        if (note != null) {
            val newNotes = notes.value
            newNotes?.remove(note)
            notes.value = newNotes
        }
    }
}