package com.jac.mynote.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jac.mynote.R
import com.jac.mynote.model.Note
import com.jac.mynote.model.SingleContentNote

class NotesAdapter(private val notes: List<Note>,
                   private val clickListener: (position: Int) -> Unit) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView = view.findViewById(R.id.note_list_item_title)
        private val contentView: TextView = view.findViewById(R.id.note_list_item_content)

        fun setValues(notes: List<Note>, position: Int, clickListener: (position: Int) -> Unit) {
            val note : Note = notes[position]
            titleTextView.text = note.title
            if (note is SingleContentNote) contentView.text = note.content
            itemView.setOnClickListener{ clickListener(position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val listItemView = LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false)
        return NotesViewHolder(listItemView)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.setValues(notes, position, clickListener)
    }
}