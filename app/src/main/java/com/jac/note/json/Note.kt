package com.jac.note.json

import android.util.JsonReader
import android.util.JsonWriter
import com.jac.note.data.NoteEntity
import com.squareup.moshi.JsonClass

/** Moshi version of [NoteEntity] for JSON serialization. */
@JsonClass(generateAdapter = true)
data class Note(val uid: Int, val title: String, val type: String, val content: String) {
    /**
     * Constructor from [NoteEntity] instance.
     * @param noteEntity the note instance.
     */
    constructor(noteEntity: NoteEntity): this(noteEntity.uid, noteEntity.title, noteEntity.type.toString(), noteEntity.content)

    /**
     * Generate a [NoteEntity] from this instance.
     * @return the note with this object's fields.
     */
    fun toNoteEntity(): NoteEntity = NoteEntity(uid, title, NoteEntity.NoteType.valueOf(type), content)
}