package com.jac.mynote.view

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.jac.mynote.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}