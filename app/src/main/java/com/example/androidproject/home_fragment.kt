package com.example.androidproject

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

private val LOCATION_PERMISSION_REQUEST = 1001

class home_fragment : Fragment() {
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var recyclerView: RecyclerView
    private lateinit var roomAdapter: RoomAdapter
    private lateinit var getLocationBtn : ImageView
    private lateinit var familyBtn : Button
    private lateinit var standardBtn : Button
    private lateinit var allBtn : Button
    private lateinit var luxuryBtn : Button
    private lateinit var suiteBtn : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_fragment, container, false)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                getUserLocation()
            } else {
                Toast.makeText(requireContext(), "Permission denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        recyclerView = view.findViewById(R.id.recyclerViewAllRoom)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        var list_room = ArrayList<Room>()
        var displayList = ArrayList<Room>()
        roomAdapter = RoomAdapter(displayList)
        recyclerView.adapter = roomAdapter
        RetrofitClient.instance.getAllRooms().enqueue(object : Callback<List<Room>> {
            override fun onResponse(call: Call<List<Room>>, response: Response<List<Room>>) {
                if (isAdded) {
                    if (response.isSuccessful && response.body() != null) {
                        list_room.clear()
                        displayList.clear()
                        response.body()?.let { list_room.addAll(it) }
                        Log.i("DEBUG", list_room.toString())
                        displayList.addAll(list_room)
                        roomAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireContext(), "Cannot fetch rooms. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<List<Room>>, t: Throwable) {
                if (isAdded) {
                    Toast.makeText(requireContext(), "Network Error: No internet connection.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        getLocationBtn = view.findViewById(R.id.getLocationBtn)
        getLocationBtn.setOnClickListener {
            getUserLocation()
        }

        allBtn = view.findViewById(R.id.allBtn)
        familyBtn = view.findViewById(R.id.Family)
        standardBtn = view.findViewById(R.id.Standard)
        luxuryBtn = view.findViewById(R.id.Luxury)
        suiteBtn = view.findViewById(R.id.Suite)

        allBtn.setOnClickListener {
            displayList.clear()
            displayList.addAll(list_room)
            roomAdapter.notifyDataSetChanged()
        }

        familyBtn.setOnClickListener {
            displayList.clear()
            displayList.addAll(list_room.filter { it.roomTypeName.equals("Family", ignoreCase = true) })
            roomAdapter.notifyDataSetChanged()
        }

        standardBtn.setOnClickListener {
            displayList.clear()
            displayList.addAll(list_room.filter { it.roomTypeName.equals("Standard", ignoreCase = true) })
            roomAdapter.notifyDataSetChanged()
        }

        luxuryBtn.setOnClickListener {
            displayList.clear()
            displayList.addAll(list_room.filter { it.roomTypeName.equals("Luxury", ignoreCase = true) })
            roomAdapter.notifyDataSetChanged()
        }

        suiteBtn.setOnClickListener {
            displayList.clear()
            displayList.addAll(list_room.filter { it.roomTypeName.equals("Suite", ignoreCase = true) })
            roomAdapter.notifyDataSetChanged()
        }
    }
    private fun hasLocationPermission(): Boolean {
        return requireContext().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED
    }
    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST
        )
    }
    private fun getUserLocation() {
        if (!hasLocationPermission()) {
            requestLocationPermission()
            return
        }
        locationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val address = getAddressFromLatLng(latitude, longitude)
                    Log.d("DEBUG", "Address: $address | Latitude: $latitude | Longitude: $longitude")
                    val textView6 = view?.findViewById<TextView>(R.id.textView6)
                    textView6?.text = address
                } else {
                    Toast.makeText(requireContext(), "Cannot get location. Try again.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT).show()
            }
    }
    private fun getAddressFromLatLng(latitude: Double, longitude: Double): String {
        return try {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                address.getAddressLine(0) ?: "Unknown location"
            } else {
                "Unknown location"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Cannot get address"
        }
    }

}
