package com.jac.mynote

import android.os.Bundle
import android.os.Debug
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jac.mynote.data.MyNoteDatabase
import com.jac.mynote.model.Note
import com.jac.mynote.viewmodel.MyNoteViewModel
import com.jac.mynote.viewmodel.NotesAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var myNoteDatabase: MyNoteDatabase
    private val myNoteViewModel: MyNoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        myNoteDatabase = MyNoteDatabase.getInstance(applicationContext)
        Thread(Runnable {
            val notes = NotesAdapter.fromModelToView(myNoteDatabase.getNotesDao().getAll())
            runOnUiThread { myNoteViewModel.notes.value = notes }
        }).start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}