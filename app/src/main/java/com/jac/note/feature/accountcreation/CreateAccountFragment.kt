package com.jac.note.feature.accountcreation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.jac.note.R
import com.jac.note.viewmodel.MyNoteViewModel

class CreateAccountFragment: Fragment() {

    private val myNoteViewModel: MyNoteViewModel by activityViewModels()

    private lateinit var loginTextView: TextView
    private lateinit var passwordTextView: TextView
    private lateinit var confirmPasswordTextView: TextView
    private lateinit var createAccountButton: Button
    private lateinit var errorTextView: TextView
    private lateinit var cancelButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_account, container, false)
    }

    private fun setError(error: Int?) {
        if (error == null) {
            errorTextView.visibility = View.GONE
            return
        }
        errorTextView.setText(error)
        errorTextView.visibility = View.VISIBLE
    }

    private fun setErrorIf(predicate: Boolean, error: Int): Boolean {
        if (predicate) {
            setError(error)
            return true
        }
        return false
    }

    private fun createAccount(userLogin: String,
                              userPassword: String,
                              userConfirmPassword: String) {
        if (setErrorIf(userLogin.isEmpty(), R.string.create_account_invalid_login)) return

        if (setErrorIf(userPassword.isEmpty() || userPassword != userConfirmPassword,
                R.string.create_account_invalid_password))
            return

        if (setErrorIf(!myNoteViewModel.createAccount(userLogin, userPassword),
                R.string.create_account_error)) return

        setError(null)
        findNavController().navigate(R.id.action_CreateAccountFragment_to_LoginFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginTextView = view.findViewById(R.id.create_account_login_text_view)
        passwordTextView = view.findViewById(R.id.create_account_password_text_view)
        confirmPasswordTextView = view.findViewById(R.id.create_account_confirm_password_text_view)
        createAccountButton = view.findViewById(R.id.create_account_create_account_button)
        errorTextView = view.findViewById(R.id.create_account_error_text_view)
        cancelButton = view.findViewById(R.id.create_account_cancel_button)

        createAccountButton.setOnClickListener {
            createAccount(loginTextView.text.toString(),
                passwordTextView.text.toString(),
                confirmPasswordTextView.text.toString())
        }
        cancelButton.setOnClickListener {
            findNavController().navigate(R.id.action_CreateAccountFragment_to_LoginFragment)
        }
    }

}