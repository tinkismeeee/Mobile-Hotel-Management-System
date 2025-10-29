package com.example.doan1

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logoutTextView = findViewById<TextView>(R.id.tvLogout)
        logoutTextView.setOnClickListener {

            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
        val btnEdit = findViewById<ImageButton>(R.id.btnEdit)
        btnEdit.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }
        val btnlanguages = findViewById<LinearLayout>(R.id.btnLanguages)
        btnlanguages.setOnClickListener {
            val intent = Intent(this, MainActivity4::class.java)
            startActivity(intent)
        }

        val btnnotifications = findViewById<LinearLayout>(R.id.btnNotifications)
        btnnotifications.setOnClickListener {
            val intent = Intent(this, MainActivity5::class.java)
            startActivity(intent)
        }
        val btnhelpAndSp = findViewById<LinearLayout>(R.id.btnhelpAndSp)
        btnhelpAndSp.setOnClickListener {
            val intent = Intent(this, MainActivity6::class.java)
            startActivity(intent)
        }
        val btnsecurity = findViewById<LinearLayout>(R.id.btnSecurity)
        btnsecurity.setOnClickListener {
            val intent = Intent(this,  MainActivity7::class.java)
            startActivity(intent)
        }

    }
}