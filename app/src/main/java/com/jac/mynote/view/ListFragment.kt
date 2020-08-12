package com.jac.mynote.view

import android.os.Bundle
import android.view.*
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
import com.jac.mynote.viewmodel.MyNoteViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListFragment : Fragment() {

    private val myNoteViewModel: MyNoteViewModel by activityViewModels()
    private val notesObserver: Observer<List<Note>> = Observer {
        notesRecyclerView.adapter = NotesAdapter(it, onNoteClickListener)
    }
    private val onNoteClickListener: (Int) -> Unit = {
        position -> myNoteViewModel.position.value = position
    }
    private val positionObserver: Observer<Int> = Observer {
        if (it != MyNoteViewModel.DEFAULT_POSITION) {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
    private lateinit var notesRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notesRecyclerView = view.findViewById(R.id.notes_recycler_view)
        notesRecyclerView.layoutManager = LinearLayoutManager(context)

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        myNoteViewModel.notes.observe(this, notesObserver)
        myNoteViewModel.position.observe(this, positionObserver)
    }

    override fun onDestroy() {
        myNoteViewModel.notes.removeObserver(notesObserver)
        myNoteViewModel.position.removeObserver(positionObserver)
        super.onDestroy()
    }
}