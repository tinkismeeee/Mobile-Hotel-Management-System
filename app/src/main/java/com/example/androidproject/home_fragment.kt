package com.example.androidproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.api.RetrofitClient
import com.example.androidproject.models.RoomResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class home_fragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RoomListAdapter
    private var roomList: List<RoomResponse> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewAll)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadRooms()
    }

    private fun loadRooms() {
        RetrofitClient.instance.getAllRooms().enqueue(object : Callback<List<RoomResponse>> {
            override fun onResponse(call: Call<List<RoomResponse>>, response: Response<List<RoomResponse>>) {
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    if (list.isNotEmpty()) {
                        roomList = list
                        adapter = RoomListAdapter(roomList)
                        recyclerView.adapter = adapter
                    } else {
                        // Báo lỗi thật nếu API trả về rỗng
                        if (isAdded) Toast.makeText(context, "API trả về danh sách trống!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (isAdded) Toast.makeText(context, "Lỗi Server: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<RoomResponse>>, t: Throwable) {
                // Báo lỗi kết nối thật
                if (isAdded) Toast.makeText(context, "Lỗi kết nối: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}