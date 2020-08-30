package com.jac.mynote.data

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jac.mynote.model.Note
import com.jac.mynote.viewmodel.MyNoteViewModel
import com.jac.mynote.viewmodel.NoteAdapter

class DataManager(lifeCycleOwner: LifecycleOwner, noteDatabase: MyNoteDatabase, noteViewModel: MyNoteViewModel) {
    private val myNoteDatabase: MyNoteDatabase = noteDatabase
    private val myNoteViewModel: MyNoteViewModel = noteViewModel
    private val notesObserver: Observer<ArrayList<Note>> = Observer {
        Thread(Runnable {
            val notesDao = myNoteDatabase.getNotesDao()
            for (note in it) {
                val modelNote = NoteAdapter.fromViewToModel(note)
                when(note.id) {
                    Note.NEW_INSTANCE_ID -> notesDao.insertNoteEntities(modelNote)
                    Note.OLD_INSTANCE_ID -> notesDao.deleteNoteEntity(modelNote)
                    else -> notesDao.updateNoteEntity(modelNote)
                }
            }
        }).start()
    }

    init {
        myNoteViewModel.notes.observe(lifeCycleOwner, notesObserver)
    }

    fun loadDatabase() {
        Thread(Runnable {
            myNoteViewModel.setNotes(myNoteDatabase.getNotesDao().getAll())
        }).start()
    }
}