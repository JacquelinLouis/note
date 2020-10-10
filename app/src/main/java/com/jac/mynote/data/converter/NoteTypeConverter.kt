package com.jac.mynote.data.converter

import androidx.room.TypeConverter
import com.jac.mynote.data.NoteEntity

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