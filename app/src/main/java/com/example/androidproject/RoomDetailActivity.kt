package com.example.androidproject

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

// === CÁC IMPORT CẦN THÊM ===
import android.content.Intent
import android.view.View
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.core.util.Pair
// ==========================

class RoomDetailActivity : AppCompatActivity() {

    private lateinit var tvHotelName: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvLocation: TextView
    private lateinit var etCheckIn: TextInputEditText
    private lateinit var etCheckOut: TextInputEditText
    private lateinit var btnBack: ImageButton
    private lateinit var btnBookNow: MaterialButton // <-- THÊM NÚT BOOK NOW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_room_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Ánh xạ các View
        tvHotelName = findViewById(R.id.tvDetailHotelName)
        tvRating = findViewById(R.id.tvDetailRating)
        tvLocation = findViewById(R.id.tvDetailLocation)
        etCheckIn = findViewById(R.id.etCheckIn)
        etCheckOut = findViewById(R.id.etCheckOut)
        btnBack = findViewById(R.id.btnBack)
        btnBookNow = findViewById(R.id.btnBookNow) // <-- ÁNH XẠ NÚT BOOK NOW

        // Lấy dữ liệu được gửi từ Adapter
        val hotelName = intent.getStringExtra("HOTEL_NAME")
        val location = intent.getStringExtra("HOTEL_LOCATION")
        val rating = intent.getStringExtra("HOTEL_RATING")
        val checkIn = intent.getStringExtra("CHECK_IN_DATE")
        val checkOut = intent.getStringExtra("CHECK_OUT_DATE")

        // Gán dữ liệu lên View
        tvHotelName.text = hotelName
        tvLocation.text = location
        tvRating.text = rating
        etCheckIn.setText(checkIn)
        etCheckOut.setText(checkOut)

        // Gắn sự kiện click cho nút Back
        btnBack.setOnClickListener {
            finish() // Đóng Activity này và quay lại màn hình trước
        }

        // Gắn sự kiện cho Date Picker
        val dateRangePickerClickListener = View.OnClickListener {
            showDateRangePicker()
        }
        etCheckIn.setOnClickListener(dateRangePickerClickListener)
        etCheckOut.setOnClickListener(dateRangePickerClickListener)

        // === LOGIC MỚI CHO NÚT "BOOK NOW" ===
        btnBookNow.setOnClickListener {
            // Tạo Intent (ý định) để mở CheckoutActivity
            val intent = Intent(this, CheckoutActivity::class.java)

            // "Gói" dữ liệu để gửi sang màn hình Checkout
            // Lấy từ các TextView/EditText đã được điền
            intent.putExtra("HOTEL_NAME", tvHotelName.text.toString())
            intent.putExtra("HOTEL_LOCATION", tvLocation.text.toString())
            intent.putExtra("CHECK_IN_DATE", etCheckIn.text.toString())
            intent.putExtra("CHECK_OUT_DATE", etCheckOut.text.toString())
            // (Bạn có thể gửi thêm giá, số lượng khách, v.v...)

            // Bắt đầu Activity mới
            startActivity(intent)
        }
        // ===================================
    }

    /**
     * Hàm này hiển thị một bảng chọn KHOẢNG NGÀY (DateRangePicker)
     */
    private fun showDateRangePicker() {
        // (Code của hàm này giữ nguyên như bước trước)
        val builder = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select Dates")
            .setTheme(R.style.ThemeOverlay_App_MaterialCalendar)

        val datePicker = builder.build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val startDateMillis = selection.first
            val endDateMillis = selection.second

            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val startDateString = sdf.format(startDateMillis)
            val endDateString = sdf.format(endDateMillis)

            etCheckIn.setText(startDateString)
            etCheckOut.setText(endDateString)
        }

        datePicker.show(supportFragmentManager, "DATE_RANGE_PICKER_TAG")
    }
}