package com.example.androidproject

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

class NhanVienActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private var userList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nhanvien)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        userAdapter = UserAdapter(userList) { selectedUser ->
            val intent = Intent(this, EditNhanVienActivity::class.java)
            intent.putExtra("USER_ID", selectedUser.user_id)
            startActivityForResult(intent, 100) // requestCode = 100
        }
        recyclerView.adapter = userAdapter

        fetchStaff()

        findViewById<ImageView>(R.id.btnToggleLayout).setOnClickListener {
            val intent = Intent(this, admin::class.java)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.btnAdd).setOnClickListener {
            val intent = Intent(this, AddNhanVienActivity::class.java)
            startActivityForResult(intent, 101) // thêm nhân viên xong reload
        }
    }

    private fun fetchStaff() {
        RetrofitClient.instance.getAllStaff().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if(response.isSuccessful && response.body() != null) {
                    userAdapter.updateData(response.body()!!)
                } else {
                    Toast.makeText(this@NhanVienActivity, "Không lấy được dữ liệu", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("NhanVienActivity", "Lỗi kết nối API", t)
                Toast.makeText(this@NhanVienActivity, "Lỗi kết nối API", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 100 || requestCode == 101) && resultCode == RESULT_OK) {
            fetchStaff() // reload danh sách sau sửa/xoá hoặc thêm
        }
    }
}
