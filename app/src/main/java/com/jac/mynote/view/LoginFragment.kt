package com.jac.mynote.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.jac.mynote.R

class LoginFragment : Fragment() {

    private companion object {
        val PASSWORD_SHARED_PREFERENCE_KEY = "PASSWORD_SHARED_PREFERENCE_KEY"
    }

    private lateinit var passwordTextView: TextView
    private lateinit var loginButton: Button
    private lateinit var createAccountButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passwordTextView = view.findViewById(R.id.login_password_text_view)
        loginButton = view.findViewById(R.id.login_login_button)
        createAccountButton = view.findViewById(R.id.login_create_account_button)

        loginButton.setOnClickListener{
            val passwordText = passwordTextView.text
            // TODO: check password nullity, emptiness, etc after debug
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val storedPassword = sharedPreferences.getString(PASSWORD_SHARED_PREFERENCE_KEY, "")
            if (storedPassword?.equals(passwordText)!!) {
                findNavController().navigate(R.id.action_LoginFragment_to_ListFragment)
            }
        }

        createAccountButton.setOnClickListener {
            if (passwordTextView.text.isNotEmpty()) {
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
                val sharedPreferencesEditor = sharedPreferences.edit()
                sharedPreferencesEditor.putString(PASSWORD_SHARED_PREFERENCE_KEY,
                    passwordTextView.text.toString())
                // TODO store the hash, not the password itself
                sharedPreferencesEditor.apply()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.actionBar?.hide()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}