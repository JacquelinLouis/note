package com.jac.note.view

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
import com.jac.note.R
import com.jac.note.security.Crypt

class LoginFragment : Fragment() {

    companion object {
        const val PASSWORD_SHARED_PREFERENCE_KEY = "PASSWORD_SHARED_PREFERENCE_KEY"
    }

    private lateinit var loginTextView: TextView
    private lateinit var passwordTextView: TextView
    private lateinit var loginButton: Button
    private lateinit var errorTextView: TextView
    private lateinit var createAccountButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private fun setError(error: Int?) {
        if (error == null) {
            errorTextView.visibility = View.GONE
            return
        }
        errorTextView.setText(error)
        errorTextView.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginTextView = view.findViewById(R.id.login_login_text_view)
        passwordTextView = view.findViewById(R.id.login_password_text_view)
        loginButton = view.findViewById(R.id.login_login_button)
        errorTextView = view.findViewById(R.id.login_error_text_view)
        createAccountButton = view.findViewById(R.id.login_create_account_button)

        loginButton.setOnClickListener{
            val userLogin:  String = loginTextView.text.toString()
            val userPassword: String = passwordTextView.text.toString()
            val localEncryptedPassword = PreferenceManager.getDefaultSharedPreferences(activity)
                .getString(PASSWORD_SHARED_PREFERENCE_KEY, "")
            // TODO: check password nullity, emptiness, etc after debug
            if (localEncryptedPassword == null
                || !Crypt.match(userLogin, userPassword, localEncryptedPassword)) {
                setError(R.string.login_error_login_in)
                return@setOnClickListener
            }
            setError(null)
            findNavController().navigate(R.id.action_LoginFragment_to_ListFragment)
        }

        createAccountButton.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_CreateAccountFragment)
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