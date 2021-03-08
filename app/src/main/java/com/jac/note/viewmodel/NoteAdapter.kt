package com.jac.note.viewmodel

import com.jac.note.data.NoteEntity
import com.jac.note.model.Note
import com.jac.note.security.DeCrypt

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
            return Note(
                modelNote.uid,
                DeCrypt.decrypt(modelNote.title),
                DeCrypt.decrypt(modelNote.content),
                fromTypeModelToView(modelNote.type))
        }

        /**
         * Adapter method to convert [NoteEntity.NoteType] to [Note.Type].
         *
         * @param modelNoteType the model note type, to convert to view.
         *
         * @return the view note type corresponding to the model note type.
         */
        private fun fromTypeModelToView(modelNoteType: NoteEntity.NoteType): Int {
            return when(modelNoteType) {
                NoteEntity.NoteType.PASSWORD_TYPE -> Note.Type.PASSWORD
                else -> Note.Type.TEXT
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
            return NoteEntity(DeCrypt.encrypt(viewNote.title),
                fromTypeViewToModel(viewNote.type),
                DeCrypt.encrypt(viewNote.content))
        }

        /**
         * Adapter method to convert [Note.Type] to [NoteEntity.NoteType].
         *
         * @param viewNoteType the view note type, to convert to model.
         *
         * @return the model note corresponding to view one.
         */
        private fun fromTypeViewToModel(viewNoteType: Int): NoteEntity.NoteType {
            return when(viewNoteType) {
                Note.Type.PASSWORD -> NoteEntity.NoteType.PASSWORD_TYPE
                else -> NoteEntity.NoteType.TEXT_TYPE
            }
        }
    }
}