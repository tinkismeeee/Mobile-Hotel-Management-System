package com.example.androidproject

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class VideoCallActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Yêu cầu giao diện toàn màn hình
        hideSystemUI()

        setContentView(R.layout.activity_video_call)

        // 1. Tìm Views
        val btnEndCall: FloatingActionButton = findViewById(R.id.fab_end_call)
        val btnToggleMic: FloatingActionButton = findViewById(R.id.fab_toggle_mic)
        val btnToggleCamera: FloatingActionButton = findViewById(R.id.fab_toggle_camera)
        val btnBack: ImageView = findViewById(R.id.iv_back)

        // 2. Xử lý sự kiện
        btnEndCall.setOnClickListener {
            Toast.makeText(this, "Cuộc gọi kết thúc", Toast.LENGTH_SHORT).show()
            finish() // Đóng Activity
        }

        btnBack.setOnClickListener {
            finish() // Đóng Activity
        }

        btnToggleMic.setOnClickListener {
            // Thêm logic bật/tắt mic ở đây
            Toast.makeText(this, "Tắt tiếng", Toast.LENGTH_SHORT).show()
        }

        btnToggleCamera.setOnClickListener {
            // Thêm logic bật/tắt camera ở đây
            Toast.makeText(this, "Tắt camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideSystemUI() {
        // Ẩn thanh trạng thái và thanh điều hướng
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.run {
                hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }
}