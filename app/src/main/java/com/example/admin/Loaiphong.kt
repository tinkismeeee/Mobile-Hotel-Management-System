package com.example.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoaiphongActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var roomAdapter: RoomTypeAdapter
    private var roomList = mutableListOf<RoomType>()

    private val EDIT_ROOM_REQUEST = 200
    private val ADD_ROOM_REQUEST = 201

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loaiphong)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        roomAdapter = RoomTypeAdapter(roomList) { selectedRoom ->
            // Chuyển sang activity edit room nếu muốn
            val intent = Intent(this, EditLoaiphong::class.java)
            intent.putExtra("ROOM_TYPE_ID", selectedRoom.room_type_id)
            startActivityForResult(intent, EDIT_ROOM_REQUEST)
        }
        recyclerView.adapter = roomAdapter

        fetchRoomTypes()

        // Nút back
        findViewById<ImageView>(R.id.btnToggleLayout).setOnClickListener {
            finish() // quay lại màn hình trước
        }

        // Nút thêm mới
        findViewById<ImageView>(R.id.btnAdd).setOnClickListener {
            val intent = Intent(this, AddLoaiphong::class.java)
            startActivityForResult(intent, ADD_ROOM_REQUEST)
        }
    }

    private fun fetchRoomTypes() {
        RetrofitClient.instance.getAllRoomTypes().enqueue(object : Callback<List<RoomType>> {
            override fun onResponse(call: Call<List<RoomType>>, response: Response<List<RoomType>>) {
                if (response.isSuccessful && response.body() != null) {
                    roomAdapter.updateData(response.body()!!)
                } else {
                    Toast.makeText(this@LoaiphongActivity, "Không lấy được dữ liệu RoomTypes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<RoomType>>, t: Throwable) {
                Log.e("LoaiphongActivity", "Lỗi kết nối API", t)
                Toast.makeText(this@LoaiphongActivity, "Lỗi kết nối API: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == EDIT_ROOM_REQUEST || requestCode == ADD_ROOM_REQUEST) && resultCode == RESULT_OK) {
            fetchRoomTypes()
        }
    }
}
