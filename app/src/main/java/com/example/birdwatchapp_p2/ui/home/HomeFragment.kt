package com.example.birdwatchapp_p2.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.birdwatchapp_p2.R
import com.example.birdwatchapp_p2.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the map
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Check location permission
        requestLocationPermission()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        getCurrentLocationAndShowOnMap()
    }

    private fun requestLocationPermission() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        when {
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted
                getCurrentLocationAndShowOnMap()
            }
            else -> {
                // Request permission
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getCurrentLocationAndShowOnMap()
            }
        }

    private fun getCurrentLocationAndShowOnMap() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    googleMap.addMarker(MarkerOptions().position(currentLatLng).title("You are here"))


                    // Fetch and display birding hotspots
                    fetchBirdingHotspots(location.latitude, location.longitude)
                } else {
                    // Handle the case when the location is null
                }
            }.addOnFailureListener { exception ->
                // Handle failure to get last location
            }
        }
    }

    private fun fetchBirdingHotspots(lat: Double, lng: Double) {
        // Load preferences
        val sharedPreferences = requireActivity().getSharedPreferences("BirdWatchAppPrefs", 0)
        val unitSystem = sharedPreferences.getBoolean("unitSystem", true) // true for kilometers, false for miles
        val maxDistance = sharedPreferences.getString("maxDistance", "50")?.toDouble() ?: 50.0

        // Fetch hotspots and filter by maxDistance
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.ebird.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val birdApiService = retrofit.create(BirdApiService::class.java)
        birdApiService.getHotspots("011", "json", "rppfimmi69sq").enqueue(object : Callback<List<Hotspot>> {
            override fun onResponse(call: Call<List<Hotspot>>, response: Response<List<Hotspot>>) {
                if (response.isSuccessful) {
                    response.body()?.let { hotspots ->
                        val filteredHotspots = hotspots.filter { hotspot ->
                            val distance = calculateDistance(lat, lng, hotspot.latitude, hotspot.longitude, unitSystem)
                            distance <= maxDistance
                        }
                        displayHotspotsOnMap(filteredHotspots)
                    }
                }
            }

            override fun onFailure(call: Call<List<Hotspot>>, t: Throwable) {
                // Handle error
            }
        })
    }

    // Helper function to calculate the distance between two coordinates
    private fun calculateDistance(
        lat1: Double, lon1: Double, lat2: Double, lon2: Double, useKilometers: Boolean
    ): Double {
        val radius = if (useKilometers) 6371.0 else 3958.8 // Radius in kilometers or miles
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return radius * c
    }
    private fun displayHotspotsOnMap(hotspots: List<Hotspot>) {
        hotspots.forEach { hotspot ->
            val hotspotLocation = LatLng(hotspot.latitude, hotspot.longitude)
            googleMap.addMarker(MarkerOptions().position(hotspotLocation).title(hotspot.name))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
