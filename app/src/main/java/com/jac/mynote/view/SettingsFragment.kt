package com.jac.mynote.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.jac.mynote.R
import com.jac.mynote.viewmodel.MyNoteViewModel

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        private const val INTENT_SELECT_INPUT_FILE_REQUEST_CODE = 0
        private const val INTENT_SELECT_OUTPUT_FOLDER_REQUEST_CODE = 1
    }

    private val myNoteViewModel: MyNoteViewModel by activityViewModels()

    private val exportPreferenceListener: Preference.OnPreferenceClickListener =
        Preference.OnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/octet-stream"
                putExtra(Intent.EXTRA_TITLE, "notes_save.json")
            }
            startActivityForResult(intent, INTENT_SELECT_OUTPUT_FOLDER_REQUEST_CODE)
            true
        }

    private val importPreferenceListener: Preference.OnPreferenceClickListener =
        Preference.OnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/octet-stream"
            }
            startActivityForResult(intent, INTENT_SELECT_INPUT_FILE_REQUEST_CODE)
            true
        }

    private val deleteAllPreferenceListener: Preference.OnPreferenceClickListener =
        Preference.OnPreferenceClickListener {
            myNoteViewModel.deleteNotes()
            true
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            INTENT_SELECT_INPUT_FILE_REQUEST_CODE -> {
                data?.data?.also { uri ->
                    context?.let { myNoteViewModel.import(it, uri) }
                }
            }
            INTENT_SELECT_OUTPUT_FOLDER_REQUEST_CODE -> {
                data?.data?.also { uri ->
                    context?.let { myNoteViewModel.export(it, uri) }
                }
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onStart() {
        super.onStart()
        findPreference<Preference>("export")?.onPreferenceClickListener = exportPreferenceListener
        findPreference<Preference>("import")?.onPreferenceClickListener = importPreferenceListener
        findPreference<Preference>("delete_all")?.onPreferenceClickListener = deleteAllPreferenceListener
    }

    override fun onPause() {
        super.onPause()
        findPreference<Preference>("export")?.onPreferenceClickListener = null
        findPreference<Preference>("import")?.onPreferenceClickListener = null
        findPreference<Preference>("delete_all")?.onPreferenceClickListener = null
    }
}