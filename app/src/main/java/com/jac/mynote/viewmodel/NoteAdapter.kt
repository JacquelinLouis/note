package com.jac.mynote.viewmodel

import com.jac.mynote.model.Note
import com.jac.mynote.data.NoteEntity
import com.jac.mynote.model.SingleContentNote

class NoteAdapter {
    companion object {
        fun fromModelToView(modelNote: NoteEntity): Note {
            when(modelNote.type) {
                else -> return SingleContentNote(modelNote.title, modelNote.content)
            }
        }
    }
}