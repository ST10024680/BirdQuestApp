package com.example.birdwatchapp_p2

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignupFragment : Fragment() {
    private lateinit var etEmail: EditText
    private lateinit var etConfPass: EditText
    private lateinit var etPass: EditText
    private lateinit var regBtn: Button
    private lateinit var regAcc: Button


    // Firebase authentication
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        etEmail = view.findViewById(R.id.inputEmail)
        etConfPass = view.findViewById(R.id.inputConfirmPassword)
        etPass = view.findViewById(R.id.inputPassword)
        regBtn = view.findViewById(R.id.btnRegister)
        regAcc = view.findViewById(R.id.btnswitch)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initializing auth and firebase objects
        auth = Firebase.auth

        regBtn.setOnClickListener {
            signUpUser()
        }
        regAcc.setOnClickListener {
            // Navigate to the login page
            findNavController().navigate(R.id.navigation_login)
        }


    }



    private fun signUpUser() {
        val email = etEmail.text.toString().trim()
        val pass = etPass.text.toString().trim()
        val confirmPassword = etConfPass.text.toString().trim()

        // Check if email and password fields are not blank
        if (email.isBlank() || pass.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(requireContext(), "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if email is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if password matches confirm password
        if (pass != confirmPassword) {
            Toast.makeText(requireContext(), "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show()
            return
        }

        // If all credentials are correct, attempt to create a new user account
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                // If user creation is successful, Navigate to login fragment
                findNavController().navigate(R.id.navigation_login)
            } else {
                Toast.makeText(requireContext(), "Sign Up Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun signInText(view: View) {

    }


}