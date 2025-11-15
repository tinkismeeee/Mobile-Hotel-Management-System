package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CheckoutActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var tvHotelName: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvDates: TextView
    private lateinit var tvGuests: TextView
    private lateinit var btnConfirmPayment: Button

    // Thêm các TextView cho giá
    private lateinit var tvRoomPrice: TextView
    private lateinit var tvTaxes: TextView
    private lateinit var tvTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_checkout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Ánh xạ View
        btnBack = findViewById(R.id.btnBack)
        tvHotelName = findViewById(R.id.tvHotelName)
        tvLocation = findViewById(R.id.tvLocation)
        tvDates = findViewById(R.id.tvDates)
        tvGuests = findViewById(R.id.tvGuests)
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment)
        tvRoomPrice = findViewById(R.id.tvRoomPrice) // <-- Ánh xạ
        tvTaxes = findViewById(R.id.tvTaxes)       // <-- Ánh xạ
        tvTotal = findViewById(R.id.tvTotal)       // <-- Ánh xạ

        // LẤY DỮ LIỆU TỪ ROOM DETAIL ACTIVITY
        val hotelName = intent.getStringExtra("HOTEL_NAME")
        val location = intent.getStringExtra("HOTEL_LOCATION")
        val checkIn = intent.getStringExtra("CHECK_IN_DATE")
        val checkOut = intent.getStringExtra("CHECK_OUT_DATE")

        // Gán dữ liệu
        tvHotelName.text = hotelName
        tvLocation.text = location
        tvDates.text = "$checkIn - $checkOut"
        tvGuests.text = "2 Guests" // (Tạm thời)
        // (Chúng ta đang dùng giá giả từ file XML, nên không cần gán giá)

        // Xử lý nút Back
        btnBack.setOnClickListener {
            finish() // Đóng màn hình này
        }

        // === LOGIC MỚI CHO NÚT "CONFIRM PAYMENT" ===
        btnConfirmPayment.setOnClickListener {
            // Tạo Intent (ý định) để mở BookingSuccessfulActivity
            val intent = Intent(this, BookingSuccessfulActivity::class.java)

            // === GỬI DỮ LIỆU HÓA ĐƠN SANG MÀN HÌNH "THÀNH CÔNG" ===
            intent.putExtra("HOTEL_NAME", tvHotelName.text.toString())
            intent.putExtra("DATES", tvDates.text.toString())
            intent.putExtra("GUESTS", tvGuests.text.toString())
            intent.putExtra("PRICE", tvRoomPrice.text.toString())
            intent.putExtra("TAX", tvTaxes.text.toString())
            intent.putExtra("TOTAL", tvTotal.text.toString())
            // ====================================================

            // Bắt đầu Activity mới
            startActivity(intent)
        }
        // =========================================
    }
}