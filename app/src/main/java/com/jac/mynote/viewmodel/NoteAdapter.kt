package com.jac.mynote.viewmodel

import com.jac.mynote.model.Note
import com.jac.mynote.data.NoteEntity
import com.jac.mynote.model.SingleContentNote

class NoteAdapter {
    companion object {
        fun fromModelToView(modelNote: NoteEntity): Note {
            return when(modelNote.type) {
                else -> SingleContentNote(modelNote.uid, modelNote.title, modelNote.content)
            }
        }

        fun fromViewToModel(viewNote: Note): NoteEntity {
            return when(viewNote) {
                is SingleContentNote -> NoteEntity(viewNote.title, SingleContentNote::class.simpleName.toString() ,viewNote.content)
                else -> NoteEntity(0, "title", "type","content")
            }
        }
    }
}