package com.example.birdwatchapp_p2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class WelcomeFragment : Fragment() {

    private lateinit var loginButton: Button
    private lateinit var signupButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)
        loginButton = view.findViewById(R.id.btnLogin)
        signupButton = view.findViewById(R.id.btnSignup)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_login)
        }
        signupButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_signup)
        }
    }
}