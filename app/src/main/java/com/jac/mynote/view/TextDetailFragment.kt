package com.jac.mynote.view

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jac.mynote.R
import com.jac.mynote.model.Note
import com.jac.mynote.viewmodel.MyNoteViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TextDetailFragment : Fragment() {

    private lateinit var detailTitleText: TextView

    private lateinit var detailContentText: TextView

    private val myNoteViewModel: MyNoteViewModel by activityViewModels()

    private val onBackPressedCallback = object:OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            myNoteViewModel.currentNote.value = null
        }
    }

    private fun save(): Boolean {
        val note: Note = myNoteViewModel.getCurrentNote() ?: return false

        note.title = detailTitleText.text.toString()
        note.content = detailContentText.text.toString()

        myNoteViewModel.setNote(note)
        myNoteViewModel.setCurrentNote(null)
        return true
    }
    private fun cancel(): Boolean {
        onBackPressedCallback.handleOnBackPressed()
        return true
    }
    private fun delete(): Boolean {
        val note = myNoteViewModel.getCurrentNote() ?: return false
        myNoteViewModel.deleteNote(note)
        myNoteViewModel.setCurrentNote(null)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> save()
            R.id.action_info -> true
            R.id.action_cancel -> cancel()
            R.id.action_delete -> delete()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailTitleText = view.findViewById(R.id.detail_title_text)
        detailContentText = view.findViewById(R.id.detail_content_text)

        myNoteViewModel.currentNote.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                findNavController().navigate(R.id.action_TextDetailFragment_to_ListFragment)
            } else {
                detailTitleText.text = it.title
                detailContentText.text = it.content
            }
        })
    }
}