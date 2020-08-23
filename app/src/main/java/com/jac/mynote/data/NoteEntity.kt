package com.jac.mynote.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class NoteEntity (
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "content") var content: String
) {
    constructor(title: String, type: String, content: String) : this(0, title, type, content)
}