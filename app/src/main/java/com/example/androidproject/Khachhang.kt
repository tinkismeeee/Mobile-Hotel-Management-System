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

class KhachHangActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var customerAdapter: CustormerAdapter
    private lateinit var actvSearchCustomer: AutoCompleteTextView

    private var customerList = mutableListOf<User1>()        
    private var displayList = mutableListOf<User1>()

    private val EDIT_CUSTOMER_REQUEST = 100
    private val ADD_CUSTOMER_REQUEST = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_khachhang)

        actvSearchCustomer = findViewById(R.id.actvSearchCustomer)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        customerAdapter = CustormerAdapter(displayList) { selectedCustomer ->
            val intent = Intent(this, EditCustomerActivity::class.java)
            intent.putExtra("USER_ID", selectedCustomer.user_id)
            startActivityForResult(intent, EDIT_CUSTOMER_REQUEST)
        }
        recyclerView.adapter = customerAdapter

        fetchCustomers()
        setupSearchListener()

        findViewById<ImageView>(R.id.btnToggleLayout).setOnClickListener {
            startActivity(Intent(this, admin::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.btnAdd).setOnClickListener {
            startActivityForResult(Intent(this, AddKhachHangActivity::class.java), ADD_CUSTOMER_REQUEST)
        }
    }

    // -------------------------
    // Lấy toàn bộ khách hàng
    // -------------------------
    private fun fetchCustomers() {
        RetrofitClient.instance.getAllCustomers().enqueue(object : Callback<List<User1>> {
            override fun onResponse(call: Call<List<User1>>, response: Response<List<User1>>) {
                if (response.isSuccessful && response.body() != null) {

                    customerList.clear()
                    customerList.addAll(response.body()!!)

                    displayList.clear()
                    displayList.addAll(customerList)
                    customerAdapter.notifyDataSetChanged()

                    // ✨ Map firstname cho AutoCompleteTextView
                    val nameList = customerList.map { it.first_name }

                    val adapterACTV = ArrayAdapter(
                        this@KhachHangActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        nameList
                    )

                    actvSearchCustomer.setAdapter(adapterACTV)
                    actvSearchCustomer.threshold = 1
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

    // -------------------------
    // Search theo firstname
    // -------------------------
    private fun setupSearchListener() {

        // Lọc realtime khi người dùng gõ
        actvSearchCustomer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()

                displayList.clear()

                if (query.isEmpty()) {
                    displayList.addAll(customerList)
                } else {
                    displayList.addAll(
                        customerList.filter {
                            it.first_name.contains(query, ignoreCase = true)
                        }
                    )
                }

                customerAdapter.notifyDataSetChanged()
            }
        })

        // Khi người dùng chọn tên từ dropdown
        actvSearchCustomer.setOnItemClickListener { parent, _, position, _ ->
            val selectedName = parent.getItemAtPosition(position).toString()

            displayList.clear()
            displayList.addAll(
                customerList.filter {
                    it.first_name.equals(selectedName, ignoreCase = true)
                }
            )

            customerAdapter.notifyDataSetChanged()
        }
    }

    // -------------------------
    // Refresh khi thêm/sửa
    // -------------------------
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == EDIT_CUSTOMER_REQUEST || requestCode == ADD_CUSTOMER_REQUEST)
            && resultCode == RESULT_OK
        ) {
            fetchCustomers()
        }
    }
}
