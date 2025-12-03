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

class Dichvu : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var serviceAdapter: ServiceAdapter
    private var serviceList = mutableListOf<Service>()

    private val EDIT_SERVICE_REQUEST = 200
    private val ADD_SERVICE_REQUEST = 201

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dichvu)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        serviceAdapter = ServiceAdapter(serviceList) { selectedService ->
            val intent = Intent(this, EditDichvuActivity::class.java)
            intent.putExtra("SERVICE_ID", selectedService.service_id)
            startActivityForResult(intent, EDIT_SERVICE_REQUEST)
        }
        recyclerView.adapter = serviceAdapter

        fetchServices()

        // Nút quay lại admin
        findViewById<ImageView>(R.id.btnToggleLayout).setOnClickListener {
            startActivity(Intent(this, admin::class.java))
            finish()
        }

        // Nút thêm dịch vụ
        findViewById<ImageView>(R.id.btnAdd).setOnClickListener {
            val intent = Intent(this, AddDichvu::class.java)
            startActivityForResult(intent, ADD_SERVICE_REQUEST)
        }
    }

    private fun fetchServices() {
        RetrofitClient.instance.getAllServices()
            .enqueue(object : Callback<List<Service>> {
                override fun onResponse(call: Call<List<Service>>, response: Response<List<Service>>) {
                    if (response.isSuccessful && response.body() != null) {
                        serviceAdapter.updateData(response.body()!!)
                    } else {
                        Toast.makeText(this@Dichvu, "Không thể tải danh sách dịch vụ", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Service>>, t: Throwable) {
                    Log.e("DichvuActivity", "Lỗi kết nối API", t)
                    Toast.makeText(this@Dichvu, "Lỗi kết nối API", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == EDIT_SERVICE_REQUEST || requestCode == ADD_SERVICE_REQUEST) && resultCode == RESULT_OK) {
            fetchServices() // reload danh sách sau khi sửa hoặc thêm
        }
    }
}
