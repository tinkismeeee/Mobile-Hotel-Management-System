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

class Dichvu : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var serviceAdapter: ServiceAdapter
    private lateinit var actvSearchService: AutoCompleteTextView

    private var serviceList = mutableListOf<Service>()
    private var displayList = mutableListOf<Service>()

    private val EDIT_SERVICE_REQUEST = 200
    private val ADD_SERVICE_REQUEST = 201

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dichvu)

        actvSearchService = findViewById(R.id.actvSearchService)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        serviceAdapter = ServiceAdapter(displayList) { selectedService ->
            val intent = Intent(this, EditDichvuActivity::class.java)
            intent.putExtra("SERVICE_ID", selectedService.service_id)
            startActivityForResult(intent, EDIT_SERVICE_REQUEST)
        }
        recyclerView.adapter = serviceAdapter

        fetchServices()
        setupSearchListener()

        findViewById<ImageView>(R.id.btnToggleLayout).setOnClickListener {
            startActivity(Intent(this, admin::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.btnAdd).setOnClickListener {
            startActivityForResult(Intent(this, AddDichvu::class.java), ADD_SERVICE_REQUEST)
        }
    }

    // -------------------------
    // Lấy danh sách dịch vụ từ API
    // -------------------------
    private fun fetchServices() {
        RetrofitClient.instance.getAllServices()
            .enqueue(object : Callback<List<Service>> {
                override fun onResponse(call: Call<List<Service>>, response: Response<List<Service>>) {
                    if (response.isSuccessful && response.body() != null) {

                        serviceList.clear()
                        serviceList.addAll(response.body()!!)

                        displayList.clear()
                        displayList.addAll(serviceList)
                        serviceAdapter.notifyDataSetChanged()

                        // Tạo danh sách tên dịch vụ cho AutoCompleteTextView
                        val serviceNames = serviceList.map { it.name }

                        val adapterACTV = ArrayAdapter(
                            this@Dichvu,
                            android.R.layout.simple_dropdown_item_1line,
                            serviceNames
                        )

                        actvSearchService.setAdapter(adapterACTV)
                        actvSearchService.threshold = 1

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

    // -------------------------
    // Tìm kiếm theo service_name
    // -------------------------
    private fun setupSearchListener() {

        // Lọc realtime khi gõ chữ
        actvSearchService.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()

                displayList.clear()

                if (query.isEmpty()) {
                    displayList.addAll(serviceList)
                } else {
                    displayList.addAll(
                        serviceList.filter {
                            it.name.contains(query, ignoreCase = true)
                        }
                    )
                }

                serviceAdapter.notifyDataSetChanged()
            }
        })

        actvSearchService.setOnItemClickListener { parent, _, position, _ ->
            val selectedName = parent.getItemAtPosition(position).toString()

            displayList.clear()
            displayList.addAll(
                serviceList.filter {
                    it.name.equals(selectedName, ignoreCase = true)
                }
            )

            serviceAdapter.notifyDataSetChanged()
        }
    }

    // -------------------------
    // Refresh khi thêm hoặc sửa dịch vụ
    // -------------------------
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == EDIT_SERVICE_REQUEST || requestCode == ADD_SERVICE_REQUEST)
            && resultCode == RESULT_OK
        ) {
            fetchServices()
        }
    }
}
