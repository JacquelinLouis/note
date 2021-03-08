package com.jac.note.json

import android.util.JsonReader
import android.util.JsonWriter
import com.jac.note.data.NoteEntity
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter

/** Serialize multiple {@link NoteEntity objects} as JSON. */
class Notes {

    /** Key values for {@link NoteEntity} instances. */
    interface Key {
        companion object {

            /** Notes key. */
            val NOTES = "NOTES"
        }
    }

    companion object {

        /**
         * Serialize the given {@link NoteEntity} instances to JSON to the given output.
         * @param noteEntities the note entities to serialize.
         * @param out the output stream to send the result to.
         */
        fun serialize(noteEntities: List<NoteEntity>, out: OutputStream) {
            val writer = JsonWriter(OutputStreamWriter(out))
            writer.beginArray()
            noteEntities.forEach{ noteEntity -> Note.serialize(noteEntity, writer) }
            writer.endArray()
            writer.close()
        }

        fun deserialize(inputStream: InputStream) : List<NoteEntity> {
            val noteEntities = ArrayList<NoteEntity>()
            val reader = JsonReader(InputStreamReader(inputStream))
            reader.beginArray()
            while (reader.hasNext()) {
                Note.deserialize(reader)?.let { noteEntities.add(it) }
            }
            reader.endArray()
            reader.close()
            return noteEntities
        }
    }
}