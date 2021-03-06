package com.jac.note.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jac.note.R
import com.jac.note.model.Note

class NotesAdapter(private val notes: List<Note>,
                   private val clickListener: (position: Int) -> Unit,
                   private val onLongClickListener: (position: Int) -> Boolean) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView = view.findViewById(R.id.note_list_item_title)
        private val contentView: TextView = view.findViewById(R.id.note_list_item_content)

        fun setValues(notes: List<Note>, position: Int, clickListener: (position: Int) -> Unit,
                      onLongClickListener: (position: Int) -> Boolean) {
            val note : Note = notes[position]
            titleTextView.text = note.title
            when(note.type) {
                Note.Type.TEXT -> contentView.text = note.content
                Note.Type.PASSWORD -> contentView.text = "******"
            }
            itemView.setOnClickListener{ clickListener(position) }
            itemView.setOnLongClickListener{ onLongClickListener(position) }
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
        holder.setValues(notes, position, clickListener, onLongClickListener)
    }
}