package com.example.doan1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2) // thay bằng layout bạn đang dùng

        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // 👉 Khi bấm Cancel -> quay về màn hình 1
        btnCancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // đóng Activity hiện tại
        }
    }
}