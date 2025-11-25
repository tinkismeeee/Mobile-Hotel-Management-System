package com.example.androidproject

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EReceiptActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnDownload: Button
    private lateinit var tvHotelName: TextView
    private lateinit var tvDates: TextView
    private lateinit var tvGuests: TextView
    private lateinit var tvRoomPrice: TextView
    private lateinit var tvTaxes: TextView
    private lateinit var tvTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_e_receipt)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Ánh xạ View
        btnBack = findViewById(R.id.btnBack)
        btnDownload = findViewById(R.id.btnDownload)
        tvHotelName = findViewById(R.id.tvHotelName)
        tvDates = findViewById(R.id.tvDates)
        tvGuests = findViewById(R.id.tvGuests)
        tvRoomPrice = findViewById(R.id.tvRoomPrice)
        tvTaxes = findViewById(R.id.tvTaxes)
        tvTotal = findViewById(R.id.tvTotal)

        // LẤY DỮ LIỆU TỪ ACTIVITY TRƯỚC (Sẽ làm ở bước sau)
        val hotelName = intent.getStringExtra("HOTEL_NAME")
        val dates = intent.getStringExtra("DATES")
        val guests = intent.getStringExtra("GUESTS")
        val price = intent.getStringExtra("PRICE")
        val tax = intent.getStringExtra("TAX")
        val total = intent.getStringExtra("TOTAL")

        // Gán dữ liệu (nếu có)
        tvHotelName.text = hotelName ?: "N/A"
        tvDates.text = dates ?: "N/A"
        tvGuests.text = guests ?: "N/A"
        tvRoomPrice.text = price ?: "$0"
        tvTaxes.text = tax ?: "$0"
        tvTotal.text = total ?: "$0"

        // Xử lý nút Back
        btnBack.setOnClickListener {
            finish()
        }

        // Xử lý nút Download (Tạm thời)
        btnDownload.setOnClickListener {
            Toast.makeText(this, "Downloading Receipt...", Toast.LENGTH_SHORT).show()
        }
    }
}