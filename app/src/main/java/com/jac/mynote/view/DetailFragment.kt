package com.jac.mynote.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jac.mynote.R
import com.jac.mynote.model.Note
import com.jac.mynote.model.SingleContentNote
import com.jac.mynote.viewmodel.MyNoteViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailFragment : Fragment() {

    private lateinit var detailTitleText: TextView
    private lateinit var detailContentText: TextView
    private lateinit var detailSaveButton: Button
    private lateinit var detailCancelButton: Button
    private val myNoteViewModel: MyNoteViewModel by activityViewModels()
    private val observer: Observer<Int> = Observer {
        if (it == MyNoteViewModel.DEFAULT_POSITION) {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        } else {
            val note = myNoteViewModel.getNote(it) ?: SingleContentNote("", "")
            detailTitleText.text = note.title
            if (note is SingleContentNote) detailContentText.text = note.content
        }
    }
    private val onBackPressedCallback = object:OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            myNoteViewModel.setPosition(MyNoteViewModel.DEFAULT_POSITION)
        }
    }
    private val saveButtonOnClickListener = View.OnClickListener {
        val note = myNoteViewModel.getCurrentNote() ?: SingleContentNote("", "")
        note.title = detailTitleText.text.toString()
        if (note is SingleContentNote) note.content = detailContentText.text.toString()
        if (note.id == Note.NEW_INSTANCE_ID) myNoteViewModel.addNote(note)
        myNoteViewModel.setPosition(MyNoteViewModel.DEFAULT_POSITION)
    }
    private val cancelButtonOnClickListener = View.OnClickListener {
        onBackPressedCallback.handleOnBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        detailSaveButton = view.findViewById(R.id.detail_save_button)
        detailCancelButton = view.findViewById(R.id.detail_cancel_button)
        detailSaveButton.setOnClickListener(saveButtonOnClickListener)
        detailCancelButton.setOnClickListener(cancelButtonOnClickListener)

        myNoteViewModel.position.observe(viewLifecycleOwner, observer)
    }

    override fun onDestroy() {
        myNoteViewModel.position.removeObserver(observer)
        super.onDestroy()
    }

}