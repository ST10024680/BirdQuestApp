package com.example.birdwatchapp_p2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var user: EditText
    private lateinit var pass: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnreg: Button


    // Create Firebase authentication object
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // View Bindings
        user = view.findViewById(R.id.inputUsername)
        pass = view.findViewById(R.id.inputPassword)
        btnLogin = view.findViewById(R.id.btnLogin)
        btnreg = view.findViewById(R.id.btnreg)


        // Initializing auth object
        auth = FirebaseAuth.getInstance()

        // Button click listener for login
        btnLogin.setOnClickListener {
            login()
        }

        btnreg.setOnClickListener {
            // Navigate to the register page
            findNavController().navigate(R.id.navigation_signup)
        }


    }

    private fun login() {
        val email = user.text.toString().trim() // Trim whitespace
        val password = pass.text.toString()

        // Check if email and password are not empty
        if (email.isNotEmpty() && password.isNotEmpty()) {
            // Authenticate user with Firebase
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Login successful, navigate to HomePage
                        findNavController().navigate(R.id.navigation_settings)
                    } else {
                        // Login failed, display a toast message with error info
                        Toast.makeText(
                            requireContext(),
                            "Login failed: ${task.exception?.localizedMessage ?: "Unknown error"}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            // Email or password is empty, display a toast message
            Toast.makeText(requireContext(), "Please enter email and password.", Toast.LENGTH_SHORT).show()
        }
    }




}
