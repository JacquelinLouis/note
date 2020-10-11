package com.jac.mynote.viewmodel

import com.jac.mynote.data.NoteEntity
import com.jac.mynote.model.Note
import com.jac.mynote.model.PasswordContentNote
import com.jac.mynote.model.TextContentNote

/** Adapter class to convert {@link NoteEntity} to {@link Note} and back. */
class NoteAdapter {

    companion object {

        /**
         * Adapter method to convert {@link NoteEntity} to {@link Note}.
         *
         * @param modelNote the note representation as model, to convert to view.
         *
         * @return the view note corresponding to model one.
         */
        fun fromModelToView(modelNote: NoteEntity): Note {
            return when(modelNote.type) {
                NoteEntity.NoteType.TEXT_TYPE -> TextContentNote(modelNote.uid, modelNote.title, modelNote.content)
                NoteEntity.NoteType.PASSWORD_TYPE -> PasswordContentNote(modelNote.uid, modelNote.title, modelNote.content)
            }
        }

        /**
         * Adapter method to convert {@link Note} to {@link NoteEntity}.
         *
         * @param viewNote the note representation as view, to convert to model.
         *
         * @return the model note corresponding to view one.
         */
        fun fromViewToModel(viewNote: Note): NoteEntity {
            return when(viewNote) {
                is TextContentNote -> NoteEntity(viewNote.title, NoteEntity.NoteType.TEXT_TYPE, viewNote.content)
                is PasswordContentNote -> NoteEntity(viewNote.title, NoteEntity.NoteType.PASSWORD_TYPE, viewNote.content)
                else -> NoteEntity(viewNote.title, NoteEntity.NoteType.TEXT_TYPE, "")
            }
        }

    }
}