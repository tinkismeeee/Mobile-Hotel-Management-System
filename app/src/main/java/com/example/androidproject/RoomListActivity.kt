package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.api.RetrofitClient
import com.example.androidproject.models.RoomResponse
import com.example.androidproject.utils.MockData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections
import java.util.Random

class RoomListActivity : AppCompatActivity() {

    private lateinit var rvRoomList: RecyclerView
    private lateinit var btnBack: ImageButton
    private lateinit var tvTitle: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyState: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_list)

        // 1. Ánh xạ View
        rvRoomList = findViewById(R.id.rvRoomList)
        btnBack = findViewById(R.id.btnBackRoomList)
        tvTitle = findViewById(R.id.tvHotelTitle)
        progressBar = findViewById(R.id.progressBar)
        tvEmptyState = findViewById(R.id.tvEmptyState)

        // 2. Setup giao diện
        val hotelName = intent.getStringExtra("HOTEL_NAME") ?: "Khách sạn"
        tvTitle.text = "Phòng tại $hotelName"

        tvEmptyState.visibility = View.GONE

        rvRoomList.layoutManager = LinearLayoutManager(this)

        btnBack.setOnClickListener { finish() }

        // 3. Bắt đầu tải dữ liệu
        loadRooms()
    }

    private fun loadRooms() {
        progressBar.visibility = View.VISIBLE

        // Gọi API lấy danh sách phòng
        RetrofitClient.instance.getAllRooms().enqueue(object : Callback<List<RoomResponse>> {
            override fun onResponse(call: Call<List<RoomResponse>>, response: Response<List<RoomResponse>>) {
                progressBar.visibility = View.GONE
                val list = response.body()
                if (response.isSuccessful && !list.isNullOrEmpty()) {
                    showData(list)
                } else {
                    useMockRooms("Server không có phòng")
                }
            }

            override fun onFailure(call: Call<List<RoomResponse>>, t: Throwable) {
                progressBar.visibility = View.GONE
                useMockRooms("Lỗi kết nối Server")
            }
        })
    }

    private fun useMockRooms(reason: String) {
        Toast.makeText(this, reason, Toast.LENGTH_SHORT).show()
        showData(MockData.getMockRooms())
    }

    private fun showData(originalList: List<RoomResponse>) {
        val hotelName = intent.getStringExtra("HOTEL_NAME") ?: ""
        val displayList = ArrayList(originalList)

        // Xáo trộn ngẫu nhiên để tạo cảm giác mỗi khách sạn khác nhau
        val seed = hotelName.hashCode().toLong()
        Collections.shuffle(displayList, Random(seed))

        rvRoomList.adapter = RoomListAdapter(displayList)
    }
}