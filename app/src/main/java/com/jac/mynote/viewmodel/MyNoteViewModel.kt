package com.jac.mynote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jac.mynote.model.Note
import com.jac.mynote.model.SingleContentNote

class MyNoteViewModel : ViewModel() {
    var notes : LiveData<List<Note>> = MutableLiveData<List<Note>>()

    init {
        notes = MutableLiveData<List<Note>>(
            listOf(
                SingleContentNote("Title1", "Content1"),
                SingleContentNote("Title2", "Content2"),
                SingleContentNote("Title3", "Content3")
            )
        )
    }
}