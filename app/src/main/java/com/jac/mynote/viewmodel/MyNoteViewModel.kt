package com.jac.mynote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jac.mynote.model.Note
import com.jac.mynote.model.SingleContentNote

class MyNoteViewModel : ViewModel() {
    companion object {
        const val DEFAULT_POSITION: Int = -1
    }

    var notes : LiveData<List<Note>> = MutableLiveData<List<Note>>(
        listOf(
            SingleContentNote("Title1", "Content1"),
            SingleContentNote("Title2", "Content2"),
            SingleContentNote("Title3", "Content3")
        )
    )
    var position : MutableLiveData<Int> = MutableLiveData(DEFAULT_POSITION)
}