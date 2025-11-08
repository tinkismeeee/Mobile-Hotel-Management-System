package com.example.androidproject


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.example.androidproject.R

class BookingDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_detail)

        // 1. Tìm view
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        val openMapButton: MaterialButton = findViewById(R.id.btn_open_map)

        // 2. Xử lý click cho Toolbar navigation
        toolbar.setNavigationOnClickListener {
            // Khi người dùng click nút "Back"
            finish() // Đóng Activity hiện tại và quay lại màn hình trước
        }

        // 3. Xử lý click cho nút "Open Map"
        openMapButton.setOnClickListener {
            // Trong ứng dụng thật, bạn sẽ mở một Activity bản đồ
            // hoặc gửi Intent để mở ứng dụng bản đồ (Google Maps)
            Toast.makeText(this, "Mở bản đồ...", Toast.LENGTH_SHORT).show()
        }
    }
}