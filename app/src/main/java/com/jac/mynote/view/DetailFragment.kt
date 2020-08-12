package com.jac.mynote.view

import android.os.Bundle
import android.text.InputType
import android.text.method.KeyListener
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jac.mynote.R
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
            val note = myNoteViewModel.notes.value?.get(myNoteViewModel.position.value!!)
            if (note != null) {
                detailTitleText.text = note.title
                if (note is SingleContentNote) detailContentText.text = note.content
            }
        }
    }
    private val onBackPressedCallback = object:OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            myNoteViewModel.position.value = MyNoteViewModel.DEFAULT_POSITION;
        }
    }
    private val saveButtonOnClickListener = View.OnClickListener {
        val note = myNoteViewModel.notes.value?.get(myNoteViewModel.position.value!!)
        if (note != null) {
            note.title = detailTitleText.text.toString()
            if (note is SingleContentNote) note.content = detailContentText.text.toString()
            myNoteViewModel.position.value = MyNoteViewModel.DEFAULT_POSITION
        }
    }
    private val cancelButtonOnClickListener = View.OnClickListener {
        myNoteViewModel.position.value = MyNoteViewModel.DEFAULT_POSITION
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