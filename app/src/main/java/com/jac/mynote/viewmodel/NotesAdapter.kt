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

        fun fromViewToModel(viewNotes: List<Note>): List<NoteEntity> {
            val modelNotes: ArrayList<NoteEntity> = ArrayList()
            for(viewNote in viewNotes) {
                modelNotes.add(NoteAdapter.fromViewToModel(viewNote))
            }
            return modelNotes
        }
    }
}