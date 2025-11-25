package com.example.androidproject

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import kotlinx.coroutines.launch
private val LOCATION_PERMISSION_REQUEST = 1001

class home_fragment : Fragment() {
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: home_list_all_recycleview_item_adapter
    private var allVillasAndHotelsList = mutableListOf<villas_and_hotels_list_item>()
    private val GOOGLE_API_KEY = "AIzaSyBBa7totCeTLg3APty-NckqqQK8nnRhrJc"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        recyclerView = view.findViewById(R.id.recyclerViewAll)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = home_list_all_recycleview_item_adapter(allVillasAndHotelsList)
        recyclerView.adapter = adapter
        getCurrentLocation()
        val locationBtn = view.findViewById<ImageView>(R.id.changeLocationBtn)
        locationBtn.setOnClickListener {
            getCurrentLocation()
            Toast.makeText(requireContext(), "Location changed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
        }

        locationClient.lastLocation.addOnSuccessListener { location ->
            if (!isAdded) {
                return@addOnSuccessListener
            }
            if (location != null) {
                val lat = location.latitude
                val lon = location.longitude
                Log.d("Location", "Got last location - Latitude: $lat, Longitude: $lon")
                fetchNearbyHotels(lat, lon)
            } else {
                Log.w("Location", "Last location is null. Requesting new location...")
                requestNewLocationData()
            }
        }.addOnFailureListener { exception ->
            if (!isAdded) {
                return@addOnFailureListener
            }
            Log.e("Location", "Failed to get last location", exception)
            Toast.makeText(requireContext(), "Failed to get location.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestNewLocationData() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setMaxUpdates(1)
            .build()

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        locationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (!isAdded) {
                return
            }
            val lastLocation = locationResult.lastLocation
            if (lastLocation != null) {
                val lat = lastLocation.latitude
                val lon = lastLocation.longitude
                Log.d("Location", "Got new location - Latitude: $lat, Longitude: $lon")
                fetchNearbyHotels(lat, lon)
            } else {
                Log.e("Location", "New location result is also null.")
            }
        }
    }

    private fun fetchNearbyHotels(latitude: Double, longitude: Double) {
        lifecycleScope.launch {
            try {
                val locationString = "$latitude,$longitude"
                val response = RetrofitClient.api.getNearbyHotels(
                    location = locationString,
                    radius = 5000,
                    type = "lodging",
                    apiKey = GOOGLE_API_KEY
                )

                val mappedList = response.results.mapNotNull { place ->
                    if (place.name == null) return@mapNotNull null

                    villas_and_hotels_list_item(
                        hotelName = place.name,
                        rating = place.rating ?: 0.0,
                        location = place.vicinity ?: "No address",
                        pricePerNight = 150,
                        startDate = "15",
                        endDate = "17 Dec 2024",
                        guests = 2,
                        rooms = 1,
                        status = "available",
                        photoReference = place.photos?.firstOrNull()?.photoReference
                    )
                }
                allVillasAndHotelsList.clear()
                allVillasAndHotelsList.addAll(mappedList)
                adapter.notifyDataSetChanged()

            } catch (e: Exception) {
                Log.e("API_ERROR", "Failed to fetch hotels", e)
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            Log.e("Location", "Location permission denied")
        }
    }
}
