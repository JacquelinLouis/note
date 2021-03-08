package com.jac.note.view

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jac.note.R
import com.jac.note.model.Note
import com.jac.note.viewmodel.MyNoteViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TextDetailFragment : Fragment() {

    private lateinit var detailTitleText: TextView

    private lateinit var detailContentText: TextView

    private var detailType: Int = R.id.action_pick_type_text

    private val myNoteViewModel: MyNoteViewModel by activityViewModels()

    private val onBackPressedCallback = object:OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            myNoteViewModel.currentNote.value = null
        }
    }

    private fun setType(menuItem: MenuItem): Boolean {
        detailType = menuItem.itemId
        activity?.invalidateOptionsMenu()
        return true
    }

    private fun save(): Boolean {
        val note: Note = myNoteViewModel.getCurrentNote() ?: return false

        note.title = detailTitleText.text.toString()
        note.content = detailContentText.text.toString()
        note.type = if (detailType == R.id.action_pick_type_password) Note.Type.PASSWORD else Note.Type.TEXT

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
            R.id.action_pick_type_text -> setType(item)
            R.id.action_pick_type_password -> setType(item)
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

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_pick_type).title = menu.findItem(detailType).title
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
        detailType = R.id.action_pick_type_text

        myNoteViewModel.currentNote.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                findNavController().navigate(R.id.action_TextDetailFragment_to_ListFragment)
            } else {
                detailTitleText.text = it.title
                detailContentText.text = it.content
                detailType = if (it.type == Note.Type.PASSWORD) R.id.action_pick_type_password else R.id.action_pick_type_text
            }
        })
    }
}