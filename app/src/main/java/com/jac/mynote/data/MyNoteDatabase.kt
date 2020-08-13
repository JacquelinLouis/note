package com.jac.mynote.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jac.mynote.MainActivity

@Database(entities = [NoteEntity::class], version = 1)
abstract class MyNoteDatabase: RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "MyNoteDatabase"
        private var myNoteDatabaseInstance: MyNoteDatabase? = null
        fun getInstance(context: Context): MyNoteDatabase {
            if (myNoteDatabaseInstance == null) {
                myNoteDatabaseInstance = Room.databaseBuilder(context, MyNoteDatabase::class.java, DATABASE_NAME).build()
            }
            return myNoteDatabaseInstance!!
        }
    }

    abstract fun getNotesDao(): NotesDao
}