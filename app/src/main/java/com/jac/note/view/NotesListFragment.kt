package com.jac.note.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jac.note.R
import com.jac.note.model.Note
import com.jac.note.viewmodel.MyNoteViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NotesListFragment : Fragment() {

    private val myNoteViewModel: MyNoteViewModel by activityViewModels()
    private val onNoteClickListener: (Int) -> Unit = {
        position -> myNoteViewModel.setCurrentNote(position)
    }
    private val onNoteLongClickListener: (Int) -> Boolean = { position ->
        val popupMenu = PopupMenu(context, notesRecyclerView[position])
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

    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var addFloatingActionButton: FloatingActionButton

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
        notesRecyclerView = view.findViewById(R.id.notes_recycler_view)
        notesRecyclerView.layoutManager = LinearLayoutManager(context)

        addFloatingActionButton.setOnClickListener {
            myNoteViewModel.setCurrentNote(Note())
        }

        myNoteViewModel.notes.observe(this, Observer {
            notesRecyclerView.adapter = NotesAdapter(it, onNoteClickListener, onNoteLongClickListener)
        })
        myNoteViewModel.currentNote.observe(this, Observer {
            if (it != null) {
                findNavController().navigate(R.id.action_ListFragment_to_TextDetailFragment)
            }
        })
    }
}