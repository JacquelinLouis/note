package com.jac.mynote.viewmodel

import com.jac.mynote.data.NoteEntity
import com.jac.mynote.model.Note
import com.jac.mynote.model.PasswordContentNote
import com.jac.mynote.model.TextContentNote

class NoteAdapter {
    companion object {
        fun fromModelToView(modelNote: NoteEntity): Note {
            return when(modelNote.type) {
                NoteEntity.NoteType.TEXT_TYPE -> TextContentNote(modelNote.uid, modelNote.title, modelNote.content)
                NoteEntity.NoteType.PASSWORD_TYPE -> PasswordContentNote(modelNote.uid, modelNote.title, modelNote.content)
            }
        }

        fun fromViewToModel(viewNote: Note): NoteEntity {
            return when(viewNote) {
                is TextContentNote -> NoteEntity(viewNote.title, NoteEntity.NoteType.TEXT_TYPE, viewNote.content)
                is PasswordContentNote -> NoteEntity(viewNote.title, NoteEntity.NoteType.PASSWORD_TYPE, viewNote.content)
                else -> NoteEntity(viewNote.title, NoteEntity.NoteType.TEXT_TYPE, "")
            }
        }
    }
}