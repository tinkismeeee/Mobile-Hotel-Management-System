package com.example.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KhachHangActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var customerAdapter: CustormerAdapter
    private var customerList = mutableListOf<User1>() // Dùng data class User1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_khachhang)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Truyền context và danh sách vào adapter
        customerAdapter = CustormerAdapter(
            context = this,
            userList = customerList,
            onItemClick = null // Chỉ hiển thị, không cần click
        )
        recyclerView.adapter = customerAdapter

        fetchCustomers()

        // Nút back (nếu có)
        findViewById<ImageView>(R.id.btnToggleLayout).setOnClickListener {
            val intent = Intent(this, admin::class.java)
            startActivity(intent)
        }
    }

    private fun fetchCustomers() {
        RetrofitClient.instance.getAllCustomers().enqueue(object : Callback<List<User1>> {
            override fun onResponse(call: Call<List<User1>>, response: Response<List<User1>>) {
                if (response.isSuccessful && response.body() != null) {
                    customerList.clear()
                    customerList.addAll(response.body()!!)
                    customerAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@KhachHangActivity,
                        "Không lấy được dữ liệu",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<User1>>, t: Throwable) {
                Log.e("KhachHangActivity", "Lỗi kết nối API", t)
                Toast.makeText(
                    this@KhachHangActivity,
                    "Lỗi kết nối API",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
