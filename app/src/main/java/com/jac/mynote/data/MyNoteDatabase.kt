package com.jac.mynote.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jac.mynote.MainActivity

/**
 * Main database access class.
 */
@Database(entities = [NoteEntity::class], version = 1)
abstract class MyNoteDatabase: RoomDatabase() {

    companion object {

        /** Database name, for internal use only. */
        private const val DATABASE_NAME = "MyNoteDatabase"

        /** Database instance, unique for the whole application. */
        private var myNoteDatabaseInstance: MyNoteDatabase? = null

        /**
         * Unique entry point to retrieve the database instance.
         *
         * @param context the Android context to build database with on first call.
         *
         * @return existing database instance or a new one on first call.
         */
        fun getInstance(context: Context): MyNoteDatabase {
            if (myNoteDatabaseInstance == null) {
                myNoteDatabaseInstance = Room.databaseBuilder(context, MyNoteDatabase::class.java, DATABASE_NAME).build()
            }
            return myNoteDatabaseInstance!!
        }
    }

    /**
     * Getter for notes data object instance.
     *
     * @return the note data object used to get, add, remove and update notes.
     */
    abstract fun getNotesDao(): NotesDao
}