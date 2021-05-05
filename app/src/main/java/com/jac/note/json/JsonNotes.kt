package com.jac.note.json

import com.jac.note.data.NoteEntity
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter


/** Serialize multiple [NoteEntity] from/to JSON from/to file. */
class JsonNotes {

    companion object {

        /**
         * Serialize the given {@link NoteEntity} instances to JSON to the given output.
         * @param noteEntities the note entities to serialize.
         * @param out the output stream to send the result to.
         */
        fun serialize(noteEntities: List<NoteEntity>, out: OutputStream) {
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter<List<JsonNote>>(Types.newParameterizedType(List::class.java, JsonNote::class.java))
            val jsonNoteEntities = jsonAdapter.toJson(noteEntities.map { noteEntity -> JsonNote(noteEntity) })
            OutputStreamWriter(out).use { writer -> writer.write(jsonNoteEntities) }
        }

        fun deserialize(inputStream: InputStream) : List<NoteEntity> {
            val noteEntities: List<NoteEntity>
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter<List<JsonNote>>(Types.newParameterizedType(List::class.java, JsonNote::class.java))
            InputStreamReader(inputStream).use { reader ->
                noteEntities = jsonAdapter.fromJson(reader.readText())?.map { note -> note.toNoteEntity() } ?: listOf()
            }
            return noteEntities
        }
    }
}