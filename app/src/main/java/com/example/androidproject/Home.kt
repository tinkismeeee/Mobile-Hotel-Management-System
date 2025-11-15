package com.example.androidproject

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.database.database

// === CÁC IMPORT CẦN THIẾT ===
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

// === IMPORT ĐỂ SỬA LỖI BUILD ===
import com.example.androidproject.HomeFragment
import com.example.androidproject.MyBookingFragment
// ===================================

class Home : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Ánh xạ (tìm) bottomNav
        bottomNav = findViewById(R.id.bottomNavigationView)

        // Tải fragment mặc định
        replaceFragment(HomeFragment()) // <<< ĐÃ SỬA

        // Xử lý sự kiện khi nhấn vào các mục menu
        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                // ID từ file bottom_nav_menu.xml
                R.id.homeBtn -> replaceFragment(HomeFragment()) // <<< ĐÃ SỬA
                R.id.bookingBtn -> replaceFragment(MyBookingFragment()) // Tính năng của bạn

                // (Tạm thời cho 2 nút còn lại)
                R.id.messageBtn -> replaceFragment(HomeFragment()) // <<< ĐÃ SỬA
                R.id.profileBtn -> replaceFragment(HomeFragment()) // <<< ĐÃ SỬA
            }
            true
        }

        // Code cũ của bạn để set text vẫn giữ nguyên
        val name_holder = findViewById<TextView>(R.id.name_holder)
        val address_holder = findViewById<TextView>(R.id.address_holder)

        name_holder.setText("Nguyễn Hữu Tính")
        address_holder.setText("Tam thanh phú quý bình thuận việt nam")
    }

    // === HÀM THAY THẾ FRAGMENT ===
    fun replaceFragment(fragment: Fragment){
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransition : FragmentTransaction = fragmentManager.beginTransaction()

        // ID 'frame_layout' này lấy từ file activity_home.xml của bạn
        fragmentTransition.replace(R.id.frame_layout, fragment)
        fragmentTransition.commit()
    }
}