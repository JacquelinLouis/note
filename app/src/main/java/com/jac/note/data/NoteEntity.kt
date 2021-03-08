package com.jac.note.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.jac.note.data.converter.NoteTypeConverter

/** Note entity representing a unique note in database. */
@Entity()
@TypeConverters(NoteTypeConverter::class)
data class NoteEntity (
    /** Generated primary identifier for this note, which never change.  */
    @PrimaryKey(autoGenerate = true)
    val uid: Int,

    /** Title of the note. */
    @ColumnInfo(name = "title")
    var title: String,

    /** Type of the note, which can be a text, a password, or something else like a list. */
    @ColumnInfo(name = "type")
    var type: NoteType,

    /** Content of the note, as a raw string. */
    @ColumnInfo(name = "content")
    var content: String
) {
    enum class NoteType {
        TEXT_TYPE,
        PASSWORD_TYPE
    }

    /**
     * Note entity constructor.
     * Identifier is generated, default value 0 is replaced by last created note's identifier + 1.
     * @param title note's title.
     * @param type note's type.
     * @param content note's content as a raw string.
     */
    constructor(title: String, type: NoteType, content: String) : this(0, title, type, content)
}