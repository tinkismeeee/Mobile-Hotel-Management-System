package com.example.androidproject

import android.os.Bundle
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

private val LOCATION_PERMISSION_REQUEST = 1001

class home_fragment : Fragment() {
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var recyclerView: RecyclerView
    // Giả sử bạn đã có một Adapter tên là RoomAdapter
    private lateinit var roomAdapter: RoomAdapter
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

        recyclerView = view.findViewById(R.id.recyclerViewAllRoom)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        var list_room = ArrayList<Room>()
        roomAdapter = RoomAdapter(list_room)
        recyclerView.adapter = roomAdapter
        RetrofitClient.instance.getAllRooms().enqueue(object : Callback<List<Room>> {
            override fun onResponse(call: Call<List<Room>>, response: Response<List<Room>>) {
                if (isAdded) {
                    if (response.isSuccessful && response.body() != null) {
                        list_room.clear()
                        response.body()?.let { list_room.addAll(it) }
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


    }
}
