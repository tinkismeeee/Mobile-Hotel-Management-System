package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class logout_profile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logout_profile)

        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val btnLogout = findViewById<Button>(R.id.btnLogout)


        btnCancel.setOnClickListener {
            finish()
        }
    }
}