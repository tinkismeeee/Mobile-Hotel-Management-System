package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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
    private lateinit var autoCompleteTextView: AutoCompleteTextView

    private var userList = mutableListOf<User>()        // danh sách gốc
    private var displayList = mutableListOf<User>()     // danh sách hiển thị (đã lọc)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nhanvien)

        autoCompleteTextView = findViewById(R.id.actvSearchStaff)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        userAdapter = UserAdapter(displayList) { selectedUser ->
            val intent = Intent(this, EditNhanVienActivity::class.java)
            intent.putExtra("USER_ID", selectedUser.user_id)
            startActivityForResult(intent, 100)
        }
        recyclerView.adapter = userAdapter

        fetchStaff()

        findViewById<ImageView>(R.id.btnToggleLayout).setOnClickListener {
            startActivity(Intent(this, admin::class.java))
        }

        findViewById<ImageView>(R.id.btnAdd).setOnClickListener {
            startActivityForResult(Intent(this, AddNhanVienActivity::class.java), 101)
        }

        setupSearchListener()
    }

    // ----------------------------
    // Lấy toàn bộ nhân viên từ API
    // ----------------------------
    private fun fetchStaff() {
        RetrofitClient.instance.getAllStaff().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful && response.body() != null) {

                    userList.clear()
                    userList.addAll(response.body()!!)

                    displayList.clear()
                    displayList.addAll(userList)
                    userAdapter.notifyDataSetChanged()

                    // Tạo danh sách tên cho AutoCompleteTextView
                    val names = userList.map { it.first_name }
                    val actvAdapter = ArrayAdapter(
                        this@NhanVienActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        names
                    )
                    autoCompleteTextView.setAdapter(actvAdapter)
                    autoCompleteTextView.threshold = 1

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

    // ----------------------------------------
    // Tìm kiếm nhân viên theo tên (realtime)
    // ----------------------------------------
    private fun setupSearchListener() {
        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()

                displayList.clear()

                if (query.isEmpty()) {
                    displayList.addAll(userList)
                } else {
                    displayList.addAll(
                        userList.filter { it.first_name.contains(query, ignoreCase = true) }
                    )
                }

                userAdapter.notifyDataSetChanged()
            }
        })

        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position).toString()

            displayList.clear()
            displayList.addAll(
                userList.filter { it.first_name.equals(selected, ignoreCase = true) }
            )

            userAdapter.notifyDataSetChanged()
        }
    }

    // ----------------------------------------
    // Refresh lại danh sách khi thêm / sửa
    // ----------------------------------------
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 100 || requestCode == 101) && resultCode == RESULT_OK) {
            fetchStaff()
        }
    }
}
