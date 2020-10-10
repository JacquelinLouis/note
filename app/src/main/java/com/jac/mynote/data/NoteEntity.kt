package com.jac.mynote.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/** Note entity representing a unique note in database. */
@Entity()
data class NoteEntity (
    /** Generated primary identifier for this note, which never change.  */
    @PrimaryKey(autoGenerate = true) val uid: Int,
    /** Title of the note. */
    @ColumnInfo(name = "title") var title: String,
    /** Type of the note, which can be a text, a password, or something else like a list. */
    @ColumnInfo(name = "type") var type: String,
    /** Content of the note, as a raw string. */
    @ColumnInfo(name = "content") var content: String
) {
    /**
     * Note entity constructor.
     * Identifier is generated, default value 0 is replaced by last created note's identifier + 1.
     * @param title note's title.
     * @param type note's type.
     * @param content note's content as a raw string.
     */
    constructor(title: String, type: String, content: String) : this(0, title, type, content)
}