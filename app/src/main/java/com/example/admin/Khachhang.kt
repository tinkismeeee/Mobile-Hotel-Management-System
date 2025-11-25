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

    // Định nghĩa Request Codes giống NhanVienActivity
    private val EDIT_CUSTOMER_REQUEST = 100
    private val ADD_CUSTOMER_REQUEST = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_khachhang)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // SỬA ĐỔI: Khởi tạo Adapter với onItemClick để mở Activity chỉnh sửa
        customerAdapter = CustormerAdapter(customerList) { selectedCustomer ->
            val intent = Intent(this, EditCustomerActivity::class.java)
            // Giả định User1 có thuộc tính user_id
            intent.putExtra("USER_ID", selectedCustomer.user_id)
            startActivityForResult(intent, EDIT_CUSTOMER_REQUEST) // requestCode = 100
        }
        recyclerView.adapter = customerAdapter

        fetchCustomers()

        // Nút back
        findViewById<ImageView>(R.id.btnToggleLayout).setOnClickListener {
            val intent = Intent(this, admin::class.java)
            startActivity(intent)
            finish() // Đóng Activity này sau khi chuyển hướng
        }

        // Nút Thêm Khách hàng
        findViewById<ImageView>(R.id.btnAdd).setOnClickListener {
            val intent = Intent(this, AddKhachHangActivity::class.java)
            startActivityForResult(intent, ADD_CUSTOMER_REQUEST) // requestCode = 101
        }
    }

    private fun fetchCustomers() {
        RetrofitClient.instance.getAllCustomers().enqueue(object : Callback<List<User1>> {
            override fun onResponse(call: Call<List<User1>>, response: Response<List<User1>>) {
                if (response.isSuccessful && response.body() != null) {
                    // SỬA ĐỔI: Sử dụng updateData() nếu CustormerAdapter có hàm này
                    customerAdapter.updateData(response.body()!!)
                } else {
                    Toast.makeText(
                        this@KhachHangActivity,
                        "Không lấy được dữ liệu khách hàng",
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Reload danh sách nếu thao tác chỉnh sửa/xóa (100) hoặc thêm mới (101) thành công
        if ((requestCode == EDIT_CUSTOMER_REQUEST || requestCode == ADD_CUSTOMER_REQUEST) && resultCode == RESULT_OK) {
            fetchCustomers()
        }
    }
}