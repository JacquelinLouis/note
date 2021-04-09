package com.jac.note.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.jac.note.data.MyNoteDatabase
import com.jac.note.data.NoteEntity
import com.jac.note.json.Notes
import com.jac.note.model.Note
import com.jac.note.security.Crypt
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.FileOutputStream

/** View-model entry point, store view-related data and update database accordingly. */
class MyNoteViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val LOGIN_SHARED_PREFERENCE_KEY = "LOGIN_SHARED_PREFERENCE_KEY"
        private const val PASSWORD_SHARED_PREFERENCE_KEY = "PASSWORD_SHARED_PREFERENCE_KEY"
        private const val LOGIN_IN_SHARED_PREFERENCE_KEY = "LOGIN_IN_SHARED_PREFERENCE_KEY"
    }

    /** Database storing application's data. */
    private var myNoteDatabase: MyNoteDatabase = MyNoteDatabase.getInstance(application)

    /** Application's shared preferences. */
    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(getApplication())

    /** Live data of items list view. */
    var notes : LiveData<List<Note>>

    /** Live data of current note (creation or update). */
    var currentNote : MutableLiveData<Note?> = MutableLiveData()

    /** Class initialization with constructor parameters. */
    init {
        notes = Transformations.map<List<NoteEntity>, List<Note>>(
            myNoteDatabase.getNotesDao().getAll()) {
                noteEntities -> NotesAdapter.fromModelToView(noteEntities)
            }
    }

    /**
     * Save the new note in the database.
     *
     * @param note the view note to convert to model and insert in the database.
     */
    fun addNote(note: Note) {
        viewModelScope.launch {
            myNoteDatabase.getNotesDao().insertNoteEntities(NoteAdapter.fromViewToModel(note))
        }
    }

    /**
     * Get the note from notes live data/database list.
     * @param position the position to get data at in notes list.
     */
    fun getNote(position: Int): Note? {
        if (position < 0 || notes.value?.size ?: 0 <= position) return null
        return notes.value?.get(position)
    }

    /**
     * Update or create the given view note in model notes list, depending on it identifier.
     * @param note the note to create or update.
     * If the note identifier is {@link Note.NEW_INSTANCE_ID}, the note will be created, else it
     * will replace the one with the same identifier (if exist).
     */
    fun setNote(note: Note) {
        when (note.id) {
            Note.NEW_INSTANCE_ID -> addNote(note)
            else -> viewModelScope.launch {
                myNoteDatabase.getNotesDao().updateNoteEntity(NoteAdapter.fromViewToModel(note))
            }
        }
    }

    /**
     * Delete the note at the given position in notes list.
     * @param position the position, in the list, of the note to delete.
     */
    fun deleteNote(position: Int) {
        val note = getNote(position)
        if (note != null) deleteNote(note)
    }

    /**
     * Delete the given note in the list.
     * @param note the note to delete, identified by its identifier.
     */
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            myNoteDatabase.getNotesDao().deleteNoteEntity(note.id)
        }
    }

    /**
     * Delete all notes.
     */
    fun deleteNotes() {
        viewModelScope.launch { myNoteDatabase.getNotesDao().deleteNoteEntities() }
    }

    /**
     * Get the current note (creation or update) if exist.
     * @return the current note if exist. Else return null.
     */
    fun getCurrentNote(): Note? {
        return currentNote.value
    }

    /**
     * Set the current note (creation for a note with {@link Note.NEW_INSTANCE_ID} identifier,
     * update for an other).
     * @param note  the note to set as current. Can be null.
     */
    fun setCurrentNote(note: Note?) {
        currentNote.value = note
    }

    /**
     * Set the current note with existing one in the list.
     * @param position the position of the note, in the list, to set as current one.
     * If the position is invalid, null will be set as current note.
     */
    fun setCurrentNote(position: Int) {
        currentNote.value = getNote(position)
    }

    /**
     * Export database to the given directory.
     * @param context the Android context to get contentResolver from.
     * @param uri the uri to export data to.
     */
    fun export(context: Context, uri: Uri) {
        val notes = notes.value
        if (notes != null) {
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "w")
            val fileDescriptor = parcelFileDescriptor?.fileDescriptor
            val fileOutputStream = FileOutputStream(fileDescriptor)
            Notes.serialize(NotesAdapter.fromViewToModel(notes), fileOutputStream)
            fileOutputStream.close()
        }
    }

    /**
     * Import database from the given file.
     * @param context the Android context to get contentResolver from.
     * @param uri the uri to export data to.
     */
    fun import(context: Context, uri: Uri) {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor = parcelFileDescriptor?.fileDescriptor
        val fileInputStream = FileInputStream(fileDescriptor)
        val noteEntities = Notes.deserialize(fileInputStream)
        viewModelScope.launch {
            noteEntities.forEach{ myNoteDatabase.getNotesDao().insertNoteEntities(it) }
        }
        fileInputStream.close()
    }

    /**
     * Create a new account for the given pair of login/password.
     * @param login user's login.
     * @param password user's password.
     * @return true if the account has been created successfully, false else.
     */
    fun createAccount(login: String, password: String): Boolean {
        val sharedPreferencesEditor = sharedPreferences.edit()
        Crypt.encrypt(login, password)?.let {
            sharedPreferencesEditor.putString(LOGIN_SHARED_PREFERENCE_KEY, login)
            sharedPreferencesEditor.putString(PASSWORD_SHARED_PREFERENCE_KEY, it)
            sharedPreferencesEditor.apply()
            return true
        }
        return false
    }

    /**
     * Get user's login.
     * @return user's login, or null if no account has been created yet.
     */
    fun getLogin(): String? {
        return sharedPreferences.getString(LOGIN_SHARED_PREFERENCE_KEY, null)
    }

    /**
     * Check if given pair of login and password match saved ones.
     * @param password password to check.
     * @return true if given pair of login/password match saved ones, false else.
     */
    fun matchAccount(password: String): Boolean {
        val login = sharedPreferences.getString(LOGIN_SHARED_PREFERENCE_KEY, null)
        val localEncryptedPassword =
            sharedPreferences.getString(PASSWORD_SHARED_PREFERENCE_KEY, null)
        // TODO: check password nullity, emptiness, etc after debug
        return login != null && localEncryptedPassword != null
                && !Crypt.match(login, password, localEncryptedPassword)
    }

    /**
     * Check is login-in is enable and required to access to application's data.
     * @return true if login is enable, false else.
     */
    fun isLoginEnable(): Boolean {
        return sharedPreferences.getBoolean(LOGIN_IN_SHARED_PREFERENCE_KEY, false)
    }

    /**
     * Enable/disable login-in requirement to access to application's data.
     * @param newValue the new value to set.
     * @return the new value set.
     */
    fun enableLogin(newValue: Boolean): Boolean {
        val sharedPreferencesEditor = sharedPreferences.edit()
        sharedPreferencesEditor.putBoolean(LOGIN_IN_SHARED_PREFERENCE_KEY, newValue)
        sharedPreferencesEditor.apply()
        return newValue
    }
}