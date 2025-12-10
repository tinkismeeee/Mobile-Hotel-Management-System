package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class admin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin)


        val btnManageEmployees: Button = findViewById(R.id.btnManageEmployees)
        val btnManageCustomers: Button = findViewById(R.id.btnManageCustomers)
        val btnManageServices: Button = findViewById(R.id.btnManageServices)
        val btnLogout: Button = findViewById(R.id.btnLogout)
        val btnRoom : Button = findViewById(R.id.btnRoom)
        val btnRevenue : Button = findViewById(R.id.btnRevenue)

        btnManageEmployees.setOnClickListener {
            Toast.makeText(this, "Bạn đã nhấn Quản lý nhân viên", Toast.LENGTH_SHORT).show()

        }

        btnManageEmployees.setOnClickListener {
            val intent = Intent(this, NhanVienActivity::class.java)
            startActivity(intent)
        }
        btnManageCustomers.setOnClickListener {
            Toast.makeText(this, "Bạn đã nhấn Quản lý khách hàng", Toast.LENGTH_SHORT).show()

        }
        btnManageCustomers.setOnClickListener {
            val intent = Intent(this, KhachHangActivity::class.java)
            startActivity(intent)
        }
        btnManageServices.setOnClickListener {
            val intent = Intent(this, Dichvu::class.java)
            startActivity(intent)
        }

        btnRevenue.setOnClickListener {
            val intent = Intent(this, DoanhThuActivity::class.java)
            startActivity(intent)
        }

        btnRoom.setOnClickListener {
            val intent = Intent(this, LoaiphongActivity::class.java)
            startActivity(intent)
        }

        btnRevenue.setOnClickListener {
            val intent = Intent(this, DoanhThuActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this, logout::class.java)
            startActivity(intent)
        }
    }
}