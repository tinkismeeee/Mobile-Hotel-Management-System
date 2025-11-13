package com.example.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class logout : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_logout)
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnCancel.setOnClickListener {
            val intent = Intent(this, admin::class.java)
            startActivity(intent)
            finish()
        }

    }
}