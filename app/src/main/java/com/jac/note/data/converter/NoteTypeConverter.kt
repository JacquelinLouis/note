package com.jac.note.data.converter

import androidx.room.TypeConverter
import com.jac.note.data.NoteEntity

class NoteTypeConverter {
    @TypeConverter
    fun stringToNoteType(name: String): NoteEntity.NoteType {
        return NoteEntity.NoteType.valueOf(name)
    }

    @TypeConverter
    fun noteTypeToString(noteType: NoteEntity.NoteType): String {
        return noteType.toString()
    }
}