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

class KhachHangActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var customerAdapter: CustormerAdapter
    private var customerList = mutableListOf<User1>()


    private val EDIT_CUSTOMER_REQUEST = 100
    private val ADD_CUSTOMER_REQUEST = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_khachhang)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        customerAdapter = CustormerAdapter(customerList) { selectedCustomer ->
            val intent = Intent(this, EditCustomerActivity::class.java)

            intent.putExtra("USER_ID", selectedCustomer.user_id)
            startActivityForResult(intent, EDIT_CUSTOMER_REQUEST)
        }
        recyclerView.adapter = customerAdapter

        fetchCustomers()


        findViewById<ImageView>(R.id.btnToggleLayout).setOnClickListener {
            val intent = Intent(this, admin::class.java)
            startActivity(intent)
            finish()
        }


        findViewById<ImageView>(R.id.btnAdd).setOnClickListener {
            val intent = Intent(this, AddKhachHangActivity::class.java)
            startActivityForResult(intent, ADD_CUSTOMER_REQUEST)
        }
    }

    private fun fetchCustomers() {
        RetrofitClient.instance.getAllCustomers().enqueue(object : Callback<List<User1>> {
            override fun onResponse(call: Call<List<User1>>, response: Response<List<User1>>) {
                if (response.isSuccessful && response.body() != null) {

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

        if ((requestCode == EDIT_CUSTOMER_REQUEST || requestCode == ADD_CUSTOMER_REQUEST) && resultCode == RESULT_OK) {
            fetchCustomers()
        }
    }
}