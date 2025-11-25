package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.api.RetrofitClient
import com.example.androidproject.models.RoomResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomListActivity : AppCompatActivity() {

    private lateinit var rvRoomList: RecyclerView
    private lateinit var btnBack: ImageButton
    private lateinit var tvTitle: TextView
    private lateinit var tvEmptyState: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_list)

        // Ánh xạ View
        rvRoomList = findViewById(R.id.rvRoomList)
        btnBack = findViewById(R.id.btnBackRoomList)
        tvTitle = findViewById(R.id.tvHotelTitle)
        tvEmptyState = findViewById(R.id.tvEmptyState)
        progressBar = findViewById(R.id.progressBar)

        // Nhận tên khách sạn từ trang trước
        val hotelName = intent.getStringExtra("HOTEL_NAME") ?: "Khách sạn"
        tvTitle.text = "Phòng tại $hotelName"

        rvRoomList.layoutManager = LinearLayoutManager(this)

        btnBack.setOnClickListener { finish() }

        // Gọi hàm tải dữ liệu
        loadRooms()
    }

    private fun loadRooms() {
        // Hiển thị loading
        progressBar.visibility = View.VISIBLE
        tvEmptyState.visibility = View.GONE
        rvRoomList.visibility = View.GONE

        // Gọi API lấy danh sách phòng
        RetrofitClient.instance.getAllRooms().enqueue(object : Callback<List<RoomResponse>> {
            override fun onResponse(call: Call<List<RoomResponse>>, response: Response<List<RoomResponse>>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val roomList = response.body()

                    if (!roomList.isNullOrEmpty()) {
                        // TRƯỜNG HỢP 1: CÓ DỮ LIỆU -> Hiện danh sách
                        rvRoomList.visibility = View.VISIBLE
                        rvRoomList.adapter = RoomListAdapter(roomList)
                    } else {
                        // TRƯỜNG HỢP 2: API TRẢ VỀ RỖNG -> Hiện thông báo
                        showEmptyState()
                    }
                } else {
                    // TRƯỜNG HỢP 3: LỖI API (404, 500...) -> Hiện thông báo
                    showEmptyState()
                }
            }

            override fun onFailure(call: Call<List<RoomResponse>>, t: Throwable) {
                // TRƯỜNG HỢP 4: MẤT MẠNG HOẶC SAI URL -> Hiện thông báo
                progressBar.visibility = View.GONE
                showEmptyState()
            }
        })
    }

    private fun showEmptyState() {
        tvEmptyState.visibility = View.VISIBLE
        rvRoomList.visibility = View.GONE
        // Bạn API Developer có thể debug ở đây để xem lỗi
    }
}