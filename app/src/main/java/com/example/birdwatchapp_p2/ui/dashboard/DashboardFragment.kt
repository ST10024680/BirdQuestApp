package com.example.birdwatchapp_p2.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.birdwatchapp_p2.R
import com.example.birdwatchapp_p2.databinding.FragmentDashboardBinding
import com.example.birdwatchapp_p2.ui.CapturesAdapter
import com.example.birdwatchapp_p2.ui.captures.BirdSighting
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardFragment : Fragment() {

    private lateinit var recyclerViewCaptures: RecyclerView
    private lateinit var capturesAdapter: CapturesAdapter
    private lateinit var capturesList: MutableList<BirdSighting>
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        // Initialize RecyclerView
        recyclerViewCaptures = view.findViewById(R.id.recyclerViewCaptures)
        recyclerViewCaptures.layoutManager = LinearLayoutManager(requireContext())
        capturesList = mutableListOf()

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference.child("bird_sightings")

        // Fetch bird captures from Firebase
        fetchCapturesFromFirebase()

        return view
    }

    private fun fetchCapturesFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                capturesList.clear()
                for (captureSnapshot in snapshot.children) {
                    val capture = captureSnapshot.getValue(BirdSighting::class.java)
                    capture?.let { capturesList.add(it) }
                }
                capturesAdapter = CapturesAdapter(capturesList)
                recyclerViewCaptures.adapter = capturesAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })
    }
}