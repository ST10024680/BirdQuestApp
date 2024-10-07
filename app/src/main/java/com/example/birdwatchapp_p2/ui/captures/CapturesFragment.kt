package com.example.birdwatchapp_p2.ui.captures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.birdwatchapp_p2.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CapturesFragment : Fragment() {

    private lateinit var inputBirdName: EditText
    private lateinit var displayLocation: EditText
    private lateinit var displayDateTime: TextView
    private lateinit var inputNotes: EditText
    private lateinit var saveButton: Button

    // Firebase Database reference
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_captures, container, false)

        // Initialize views
        inputBirdName = view.findViewById(R.id.inputBirdName)
        displayLocation = view.findViewById(R.id.displayLocation)
        displayDateTime = view.findViewById(R.id.displayDateTime)
        inputNotes = view.findViewById(R.id.inputNotes)
        saveButton = view.findViewById(R.id.saveButton)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("bird_sightings")

        // Set current date and time
        val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        displayDateTime.text = currentDateTime



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Save bird details to Firebase
        saveButton.setOnClickListener {
            val birdName = inputBirdName.text.toString()
            val location = displayLocation.text.toString()
            val dateTime = displayDateTime.text.toString()
            val notes = inputNotes.text.toString()

            if (birdName.isNotEmpty() && location.isNotEmpty() && dateTime.isNotEmpty()) {
                saveBirdSightingToFirebase(birdName, location, dateTime, notes)
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Method to save bird sighting to Firebase Realtime Database
    private fun saveBirdSightingToFirebase(birdName: String, location: String, dateTime: String, notes: String) {
        val birdId = database.push().key // Generate unique ID for the bird sighting
        val birdSighting = BirdSighting(birdName, location, dateTime, notes)

        birdId?.let {
            database.child(it).setValue(birdSighting).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Bird sighting saved successfully", Toast.LENGTH_SHORT).show()
                    clearInputs()
                } else {
                    Toast.makeText(requireContext(), "Failed to save bird sighting", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Clear input fields after saving
    private fun clearInputs() {
        inputBirdName.text.clear()
        inputNotes.text.clear()
        // Keep location and date time as is.
    }
}

// BirdSighting data class to model the bird sighting info
data class BirdSighting(
    var birdName: String? = null,
    var location: String? = null,
    var dateTime: String? = null,
    var notes: String? = null
) {
    // No-argument constructor required by Firebase
    constructor() : this(null, null, null, null)
}
