package com.example.doan1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logoutTextView = findViewById<TextView>(R.id.tvLogout)
        logoutTextView.setOnClickListener {
            // Khi bấm Logout → chuyển sang MainSecondActivity2
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
        val btnEdit = findViewById<ImageButton>(R.id.btnEdit)
        btnEdit.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }
    }
}