package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BookingSuccessfulActivity : AppCompatActivity() {

    private lateinit var btnViewReceipt: Button
    private lateinit var btnBackToHome: TextView

    // === KHAI BÁO BIẾN ĐỂ LƯU DỮ LIỆU HÓA ĐƠN ===
    private var hotelName: String? = null
    private var dates: String? = null
    private var guests: String? = null
    private var price: String? = null
    private var tax: String? = null
    private var total: String? = null
    // ============================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_booking_successful)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnViewReceipt = findViewById(R.id.btnViewReceipt)
        btnBackToHome = findViewById(R.id.btnBackToHome)

        // === NHẬN DỮ LIỆU TỪ CHECKOUT ACTIVITY ===
        hotelName = intent.getStringExtra("HOTEL_NAME")
        dates = intent.getStringExtra("DATES")
        guests = intent.getStringExtra("GUESTS")
        price = intent.getStringExtra("PRICE")
        tax = intent.getStringExtra("TAX")
        total = intent.getStringExtra("TOTAL")
        // =======================================

        // Xử lý sự kiện nhấn nút "View E-Receipt"
        btnViewReceipt.setOnClickListener {
            // TẠO INTENT MỚI ĐỂ MỞ EReceiptActivity
            val receiptIntent = Intent(this, EReceiptActivity::class.java)

            // GỬI TIẾP DỮ LIỆU HÓA ĐƠN SANG MÀN HÌNH HÓA ĐƠN
            receiptIntent.putExtra("HOTEL_NAME", hotelName)
            receiptIntent.putExtra("DATES", dates)
            receiptIntent.putExtra("GUESTS", guests)
            receiptIntent.putExtra("PRICE", price)
            receiptIntent.putExtra("TAX", tax)
            receiptIntent.putExtra("TOTAL", total)

            startActivity(receiptIntent)
        }

        // Xử lý sự kiện nhấn nút "Back to Home"
        btnBackToHome.setOnClickListener {
            // Tạo Intent để quay về Home Activity
            val intent = Intent(this, Home::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish() // Đóng Activity này lại
        }
    }
}