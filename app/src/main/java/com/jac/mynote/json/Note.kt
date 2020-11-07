package com.jac.mynote.json

import android.util.JsonReader
import android.util.JsonWriter
import com.jac.mynote.data.NoteEntity
import java.io.OutputStream
import java.io.OutputStreamWriter

/** Serialize the {@link NoteEntity} object. */
class Note {

    /** Key values for {@link NoteEntity}. */
    interface Key {

        companion object {

            /** Common key pre value. */
            private val NOTE = "NOTE";

            /** Common separator value. */
            private val SEPARATOR = "_";

            /** Common key post value. */
            private val KEY = "KEY";

            /** Generate the key for the given value */
            private fun getKey(key: String): String {
                return NOTE + SEPARATOR + key + SEPARATOR + KEY
            }

            /** UID key. */
            val UID = getKey("UID")

            /** Title key. */
            val TITLE = getKey("TITLE")

            /** Type key. */
            val TYPE = getKey("TYPE")

            /** Content key. */
            val CONTENT = getKey("CONTENT")
        }
    }

    companion object {

        /**
         * Serialize the given {@link NoteEntity} to JSON to the given output.
         * @param noteEntity the note to serialize.
         * @param writer the json writer to write to.
         */
        fun serialize(noteEntity: NoteEntity, writer: JsonWriter) {
            writer.beginObject()
            writer.name(Key.UID).value(noteEntity.uid)
            writer.name(Key.TITLE).value(noteEntity.title)
            writer.name(Key.TYPE).value(noteEntity.type.toString())
            writer.name(Key.CONTENT).value(noteEntity.content)
            writer.endObject()
        }

        fun deserialize(reader: JsonReader): NoteEntity? {

            var uid: Int? = null
            var title: String? = null
            var type: NoteEntity.NoteType? = null
            var content: String? = null

            reader.beginObject()
            while (reader.hasNext()) {
                when(reader.nextName()) {
                    Key.UID -> uid = reader.nextInt()
                    Key.TITLE -> title = reader.nextString()
                    Key.TYPE -> type = NoteEntity.NoteType.valueOf(reader.nextString())
                    Key.CONTENT -> content = reader.nextString()
                }
            }
            reader.endObject()

            if (uid == null || title == null || type == null || content == null) {
                // TODO: log error.
                return null
            }

            return NoteEntity(uid, title, type, content)
        }
    }
}