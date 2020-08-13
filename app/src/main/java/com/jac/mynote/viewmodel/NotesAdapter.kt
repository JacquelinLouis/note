package com.jac.mynote.viewmodel

import com.jac.mynote.model.Note
import com.jac.mynote.data.NoteEntity

class NotesAdapter {
    companion object {
        fun fromModelToView(modelNotes: List<NoteEntity>): List<Note> {
            val viewNotes: ArrayList<Note> = ArrayList()
            for (modelNote in modelNotes) {
                viewNotes.add(NoteAdapter.fromModelToView(modelNote))
            }
            return viewNotes
        }
    }
}