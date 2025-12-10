package com.example.androidproject

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.database.database

class Home : BaseActivity() {
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
        val openFragment = intent.getStringExtra("openFragment")
        if (openFragment == "my_booking") {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, my_booking_fragment())
                .commit()
        }
        bottomNav = findViewById(R.id.bottomNavigationView)
        replaceFragment(home_fragment())
        val name_holder = findViewById<TextView>(R.id.name_holder)
        val address_holder = findViewById<TextView>(R.id.address_holder)
        val avatar = findViewById<com.google.android.material.imageview.ShapeableImageView>(R.id.avatar)

        name_holder.setText("Nguyễn Hữu Tính")
        address_holder.setText("Bình Thuận")

        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeBtn -> replaceFragment(home_fragment())
            }
            when(it.itemId){
                R.id.profileBtn -> replaceFragment(main_profile())
            }
            when(it.itemId){
                R.id.bookingBtn -> replaceFragment(my_booking_fragment())
            }
            when(it.itemId) {
                R.id.aboutBtn -> replaceFragment(about_fragment())
            }
            true
        }

        avatar.setOnClickListener {
            replaceFragment(main_profile())
        }

        setupChatBot()
    }
    fun replaceFragment(fragment: Fragment){
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransition : FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransition.replace(R.id.fragmentContainerView, fragment)
        fragmentTransition.commit()
    }
}