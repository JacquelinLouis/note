package com.jac.mynote.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jac.mynote.R
import com.jac.mynote.model.SingleContentNote
import com.jac.mynote.viewmodel.MyNoteViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailFragment : Fragment() {

    private lateinit var detailTitleTextView : TextView
    private lateinit var detailContentTextView : TextView
    private val myNoteViewModel: MyNoteViewModel by activityViewModels()
    private val observer: Observer<Int> = Observer {
        if (it == MyNoteViewModel.DEFAULT_POSITION) {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        } else {
            val note = myNoteViewModel.notes.value?.get(myNoteViewModel.position.value!!)
            if (note != null) {
                detailTitleTextView.text = note.title
                if (note is SingleContentNote) detailTitleTextView.text = note.content
            }
        }
    }
    private val onBackPressedCallback = object:OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            myNoteViewModel.position.value = MyNoteViewModel.DEFAULT_POSITION;
        }
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
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true);
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailTitleTextView = view.findViewById(R.id.detail_title_text_view)
        detailContentTextView = view.findViewById(R.id.detail_content_text_view)

        myNoteViewModel.position.observe(viewLifecycleOwner, observer)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                myNoteViewModel.position.value = MyNoteViewModel.DEFAULT_POSITION;true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        myNoteViewModel.position.removeObserver(observer)
        super.onDestroy()
    }

}