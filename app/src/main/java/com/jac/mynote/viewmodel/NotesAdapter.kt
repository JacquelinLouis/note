package com.jac.mynote.viewmodel

import com.jac.mynote.model.Note
import com.jac.mynote.data.NoteEntity

/** Adapter class to convert {@link NoteEntity} to {@link Note} and back. */
class NotesAdapter {

    companion object {

        /**
         * Adapter method to convert a list of {@link NoteEntity} to a list of {@link Note}.
         *
         * @param modelNotes the notes representation as model, to convert to view.
         *
         * @return the view notes corresponding to model ones.
         */
        fun fromModelToView(modelNotes: List<NoteEntity>): List<Note> {
            val viewNotes: ArrayList<Note> = ArrayList()
            for (modelNote in modelNotes) {
                viewNotes.add(NoteAdapter.fromModelToView(modelNote))
            }
            return viewNotes
        }

        /**
         * Adapter method to convert a list of {@link Note} to a list of {@link NoteEntity}.
         *
         * @param viewNotes the notes representation as view, to convert to model.
         *
         * @return the model notes corresponding to view ones.
         */
        fun fromViewToModel(viewNotes: List<Note>): List<NoteEntity> {
            val modelNotes: ArrayList<NoteEntity> = ArrayList()
            for(viewNote in viewNotes) {
                modelNotes.add(NoteAdapter.fromViewToModel(viewNote))
            }
            return modelNotes
        }
    }
}