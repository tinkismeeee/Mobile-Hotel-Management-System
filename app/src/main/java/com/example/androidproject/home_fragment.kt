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
    private val GOOGLE_API_KEY = "7139eb9e72384540b4d1b6d403c5b319"

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
        val locationBtn = view.findViewById<ImageView>(R.id.changeLocationBtn)
        locationBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Location changed", Toast.LENGTH_SHORT).show()
        }
    }









}
