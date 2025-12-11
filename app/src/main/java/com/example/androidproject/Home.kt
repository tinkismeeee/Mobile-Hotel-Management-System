package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.database.database
import retrofit2.Call
import retrofit2.Response

class Home : BaseActivity() {
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var notificationBtn : ImageView
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

//        name_holder.setText("Nguyễn Hữu Tính")
//        address_holder.setText("Bình Thuận")

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

        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val session = EncryptedSharedPreferences.create(
            this,
            getString(R.string.session_file_name),
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        val userEmail = session.getString("email", null)
        if (userEmail != null) {
            Log.i("DEBUG", userEmail.toString())
            RetrofitClient.instance.getCustomerByEmail(userEmail!!)
                .enqueue(object : retrofit2.Callback<User1> {
                    override fun onResponse(call: Call<User1>, response: Response<User1>) {
                        if (response.isSuccessful) {
                            val user = response.body()
                            val userId = user?.user_id ?: 0
                            val first_name = user?.first_name ?: ""
                            val last_name = user?.last_name ?: ""
                            val address = user?.address ?: ""
                            address_holder.text = address
                            name_holder.text = "$first_name $last_name"
                            Log.i("DEBUG", "${name_holder.text.toString()} | ${address_holder.text.toString()}")
                        } else {
                            Log.e("API", "Error: ${response.code()}")
                            name_holder.setText("Nguyễn Hữu Tính")
                            address_holder.setText("Phú Qúy, Bình Thuận")
                        }
                    }
                    override fun onFailure(call: Call<User1>, t: Throwable) {
                        Log.e("API_ERR", t.message ?: "Unknown error")
                    }
                })
        }
        else {
            name_holder.setText("Nguyễn Hữu Tính")
            address_holder.setText("Phú Qúy, Bình Thuận")
        }
        notificationBtn = findViewById(R.id.notificationBtn)
        notificationBtn.setOnClickListener {
            val intent = Intent(this, Notification_History::class.java)
            startActivity(intent)
        }
    }
    fun replaceFragment(fragment: Fragment){
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransition : FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransition.replace(R.id.fragmentContainerView, fragment)
        fragmentTransition.commit()
    }
}