package com.example.birdwatchapp_p2.ui.settings


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.birdwatchapp_p2.R



class SettingsFragment : Fragment() {

    private lateinit var switchUnitSystem: Switch
    private lateinit var inputMaxDistance: EditText
    private lateinit var saveButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        switchUnitSystem = view.findViewById(R.id.switchUnitSystem)
        inputMaxDistance = view.findViewById(R.id.inputMaxDistance)
        saveButton = view.findViewById(R.id.saveButton)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load saved preferences
        val sharedPreferences = requireActivity().getSharedPreferences("BirdWatchAppPrefs", 0)
        val unitSystem = sharedPreferences.getBoolean("unitSystem", true) // true for kilometers, false for miles
        val maxDistance = sharedPreferences.getString("maxDistance", "50") // default to 50 km

        switchUnitSystem.isChecked = unitSystem
        inputMaxDistance.setText(maxDistance)

        saveButton.setOnClickListener {
            // Save preferences
            val editor = sharedPreferences.edit()
            editor.putBoolean("unitSystem", switchUnitSystem.isChecked)
            editor.putString("maxDistance", inputMaxDistance.text.toString())
            editor.apply()

            findNavController().navigate(R.id.navigation_home)
// Go back after saving
        }

    }

}
