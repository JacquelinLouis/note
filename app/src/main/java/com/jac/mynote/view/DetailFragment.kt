package com.jac.mynote.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jac.mynote.R
import com.jac.mynote.viewmodel.MyNoteViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailFragment : Fragment() {

    private val myNoteViewModel: MyNoteViewModel by viewModels()
    private val observer: Observer<Int> = Observer{
        if (it == MyNoteViewModel.DEFAULT_POSITION) {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
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
        super.onCreateOptionsMenu(menu, inflater)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        myNoteViewModel.position.observe(this, observer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.detail_title_text_view)
        view.findViewById<TextView>(R.id.detail_content_text_view)
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