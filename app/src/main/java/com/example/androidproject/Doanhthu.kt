package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoanhThuActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var invoiceAdapter: DoanhthuAdapter
    private val invoiceList = mutableListOf<Invoice>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doanhthu)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        invoiceAdapter = DoanhthuAdapter(invoiceList) { invoice ->
            val intent = Intent(this, InvoiceWebViewActivity::class.java)
            intent.putExtra("INVOICE_JSON", Gson().toJson(invoice))
            startActivity(intent)
        }

        recyclerView.adapter = invoiceAdapter

        fetchInvoices()

        findViewById<ImageView>(R.id.btnToggleLayout).setOnClickListener {
            startActivity(Intent(this, admin::class.java))
            finish()
        }
    }

    private fun fetchInvoices() {
        RetrofitClient.instance.getAllInvoices()
            .enqueue(object : Callback<List<Invoice>> {
                override fun onResponse(call: Call<List<Invoice>>, response: Response<List<Invoice>>) {
                    if (response.isSuccessful && response.body() != null) {
                        invoiceAdapter.updateData(response.body()!!)
                    } else {
                        Toast.makeText(this@DoanhThuActivity, "Không thể tải hóa đơn", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Invoice>>, t: Throwable) {
                    Log.e("DoanhThuActivity", "Lỗi API", t)
                    Toast.makeText(this@DoanhThuActivity, "Lỗi kết nối API", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
