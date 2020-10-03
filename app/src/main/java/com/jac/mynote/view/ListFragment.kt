package com.jac.mynote.view

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.PopupMenu
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jac.mynote.R
import com.jac.mynote.model.Note
import com.jac.mynote.model.SingleContentNote
import com.jac.mynote.viewmodel.MyNoteViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListFragment : Fragment() {

    private val myNoteViewModel: MyNoteViewModel by activityViewModels()
    private val onNoteClickListener: (Int) -> Unit = {
        position -> myNoteViewModel.setPosition(position)
    }
    private val onNoteLongClickListener: (Int) -> Boolean = { position ->
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.menu_list_popup)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.action_info -> true
                R.id.action_delete -> { myNoteViewModel.deleteNote(position); true }
                else -> false
            }
        }
        popupMenu.show()
        true
    }
    private val notesObserver: Observer<List<Note>> = Observer {
        notesRecyclerView.adapter = NotesAdapter(it, onNoteClickListener, onNoteLongClickListener)
    }

    private val positionObserver: Observer<Int> = Observer {
        if (it != MyNoteViewModel.DEFAULT_POSITION) {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var addFloatingActionButton: FloatingActionButton
    private lateinit var addNoteFloatingActionButton: FloatingActionButton
    private lateinit var addPasswordFloatingActionButton: FloatingActionButton
    private lateinit var addLayout: ConstraintLayout

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
        addNoteFloatingActionButton.setOnClickListener{
            myNoteViewModel.setPosition(MyNoteViewModel.NEW_POSITION)
        }

        myNoteViewModel.notes.observe(this, notesObserver)
        myNoteViewModel.position.observe(this, positionObserver)
    }

    private fun changeAddButtonsVisibility() {
        when (addLayout.visibility) {
            View.GONE -> addLayout.visibility = View.VISIBLE
            else -> addLayout.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        myNoteViewModel.notes.removeObserver(notesObserver)
        myNoteViewModel.position.removeObserver(positionObserver)
        super.onDestroy()
    }
}