package com.jac.mynote.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jac.mynote.R
import com.jac.mynote.model.Note
import com.jac.mynote.model.PasswordContentNote
import com.jac.mynote.model.TextContentNote
import com.jac.mynote.viewmodel.MyNoteViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListFragment : Fragment() {

    private val myNoteViewModel: MyNoteViewModel by activityViewModels()
    private val onNoteClickListener: (Int) -> Unit = {
        position -> myNoteViewModel.setCurrentNote(position)
    }
    private val onNoteLongClickListener: (Int) -> Boolean = { position ->
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.menu_list_popup)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.action_info -> true
                R.id.action_delete -> deleteNote(position)
                else -> false
            }
        }
        popupMenu.show()
        true
    }
    private val notesObserver: Observer<List<Note>> = Observer {
        notesRecyclerView.adapter = NotesAdapter(it, onNoteClickListener, onNoteLongClickListener)
    }

    private val currentNoteObserver: Observer<Note?> = Observer {
        if (it != null) {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var addFloatingActionButton: FloatingActionButton
    private lateinit var addNoteFloatingActionButton: FloatingActionButton
    private lateinit var addPasswordFloatingActionButton: FloatingActionButton
    private lateinit var addLayout: ConstraintLayout

    private fun deleteNote(position: Int): Boolean {
        myNoteViewModel.deleteNote(position)
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addFloatingActionButton = view.findViewById(R.id.fab_add)
        addNoteFloatingActionButton = view.findViewById(R.id.fab_note)
        addPasswordFloatingActionButton = view.findViewById(R.id.fab_lock)
        addLayout = view.findViewById(R.id.fab_add_layout)
        notesRecyclerView = view.findViewById(R.id.notes_recycler_view)
        notesRecyclerView.layoutManager = LinearLayoutManager(context)

        addFloatingActionButton.setOnClickListener { changeAddButtonsVisibility() }
        addNoteFloatingActionButton.setOnClickListener {
            myNoteViewModel.setCurrentNote(TextContentNote())
        }
        addPasswordFloatingActionButton.setOnClickListener {
            myNoteViewModel.setCurrentNote(PasswordContentNote())
        }

        myNoteViewModel.notes.observe(this, notesObserver)
        myNoteViewModel.currentNote.observe(this, currentNoteObserver)
    }

    private fun changeAddButtonsVisibility() {
        when (addLayout.visibility) {
            View.GONE -> addLayout.visibility = View.VISIBLE
            else -> addLayout.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        myNoteViewModel.notes.removeObserver(notesObserver)
        myNoteViewModel.currentNote.removeObserver(currentNoteObserver)
        super.onDestroy()
    }
}