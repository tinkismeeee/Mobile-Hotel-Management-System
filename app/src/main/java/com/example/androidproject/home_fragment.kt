package com.example.androidproject

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.utils.MockData // Import MockData
import com.google.android.gms.location.*
import kotlinx.coroutines.launch

private val LOCATION_PERMISSION_REQUEST = 1001

class home_fragment : Fragment() {
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: home_list_all_recycleview_item_adapter

    private var allVillasAndHotelsList = mutableListOf<villas_and_hotels_list_item>()

    // API Key Google
    private val GOOGLE_API_KEY = "AIzaSyBBa7totCeTLg3APty-NckqqQK8nnRhrJc"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Khởi tạo an toàn
        try {
            locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        } catch (e: Exception) {
            useMockData("Lỗi khởi tạo vị trí")
            return
        }

        recyclerView = view.findViewById(R.id.recyclerViewAll)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = home_list_all_recycleview_item_adapter(allVillasAndHotelsList)
        recyclerView.adapter = adapter

        // Bắt đầu lấy dữ liệu
        getCurrentLocation()

        val locationBtn = view.findViewById<ImageView>(R.id.changeLocationBtn)
        locationBtn.setOnClickListener {
            getCurrentLocation()
            context?.let { Toast.makeText(it, "Đang làm mới...", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun getCurrentLocation() {
        // Kiểm tra xem Fragment còn sống không
        if (!isAdded || context == null) return

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
        } else {
            locationClient.lastLocation.addOnSuccessListener { location ->
                // [QUAN TRỌNG] Kiểm tra lại lần nữa sau khi async trả về
                if (!isAdded) return@addOnSuccessListener

                if (location != null) {
                    fetchNearbyHotels(location.latitude, location.longitude)
                } else {
                    requestNewLocationData()
                }
            }.addOnFailureListener {
                if (isAdded) useMockData("Không lấy được vị trí")
            }
        }
    }

    private fun requestNewLocationData() {
        // [SỬA LỖI CRASH] Kiểm tra context an toàn thay vì requireContext()
        val ctx = context ?: return

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setMaxUpdates(1)
            .build()

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    if (!isAdded) return // Fragment chết rồi thì thôi

                    val lastLocation = locationResult.lastLocation
                    if (lastLocation != null) {
                        fetchNearbyHotels(lastLocation.latitude, lastLocation.longitude)
                    } else {
                        useMockData("Không tìm thấy vị trí mới")
                    }
                }
            }, null)
        } else {
            useMockData("Thiếu quyền vị trí")
        }
    }

    private fun fetchNearbyHotels(latitude: Double, longitude: Double) {
        lifecycleScope.launch {
            if (!isAdded) return@launch

            try {
                val locationString = "$latitude,$longitude"
                val response = GoogleClient.api.getNearbyHotels(
                    location = locationString,
                    radius = 5000,
                    type = "lodging",
                    apiKey = GOOGLE_API_KEY
                )

                val mappedList = response.results.mapNotNull { place ->
                    if (place.name == null) return@mapNotNull null
                    villas_and_hotels_list_item(
                        hotelName = place.name,
                        rating = place.rating ?: 4.5,
                        location = place.vicinity ?: "Vietnam",
                        pricePerNight = (100..500).random() * 10000,
                        startDate = "Today",
                        endDate = "Tomorrow",
                        guests = 2,
                        rooms = 1,
                        status = "available",
                        photoReference = place.photos?.firstOrNull()?.photoReference
                    )
                }

                if (!isAdded) return@launch // Check lần cuối trước khi update UI

                if (mappedList.isNotEmpty()) {
                    allVillasAndHotelsList.clear()
                    allVillasAndHotelsList.addAll(mappedList)
                    adapter.notifyDataSetChanged()
                } else {
                    useMockData("Google API trả về rỗng")
                }

            } catch (e: Exception) {
                if (isAdded) useMockData("Lỗi API Google: ${e.message}")
            }
        }
    }

    // Hàm chuyển sang dữ liệu giả an toàn
    private fun useMockData(reason: String) {
        if (!isAdded) return

        context?.let {
            // Toast.makeText(it, "Dùng dữ liệu mẫu ($reason)", Toast.LENGTH_SHORT).show()
            // (Có thể bỏ comment dòng trên nếu muốn hiện thông báo lỗi)
        }

        allVillasAndHotelsList.clear()
        allVillasAndHotelsList.addAll(MockData.getMockHotels())
        adapter.notifyDataSetChanged()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (!isAdded) return

        if (requestCode == LOCATION_PERMISSION_REQUEST && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            useMockData("Quyền vị trí bị từ chối")
        }
    }
}