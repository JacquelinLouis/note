package com.jac.mynote.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jac.mynote.model.Note

class MyNoteViewModel : ViewModel() {
    companion object {
        const val DEFAULT_POSITION: Int = -1
    }

    var notes : MutableLiveData<ArrayList<Note>> = MutableLiveData()
    var position : MutableLiveData<Int> = MutableLiveData(DEFAULT_POSITION)

    fun addNote(note: Note) {
        var newNotes = notes.value
        if (newNotes == null) newNotes = ArrayList()
        newNotes.add(note)
        notes.value = newNotes
    }
}