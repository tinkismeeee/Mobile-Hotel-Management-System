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
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.core.util.Pair

// === DÒNG SỬA LỖI: THÊM IMPORT NÀY ===
import android.view.View
// ===================================

class RoomDetailActivity : AppCompatActivity() {

    private lateinit var tvHotelName: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvLocation: TextView
    private lateinit var etCheckIn: TextInputEditText
    private lateinit var etCheckOut: TextInputEditText
    private lateinit var btnBack: ImageButton

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

        // === THAY ĐỔI LOGIC DATE PICKER ===

        // Tạo một bộ lắng nghe chung
        // Dòng này (View.OnClickListener) sẽ hết báo lỗi
        val dateRangePickerClickListener = View.OnClickListener {
            showDateRangePicker()
        }

        // 1. Khi nhấn vào ô Check-in -> Mở chung 1 bảng
        etCheckIn.setOnClickListener(dateRangePickerClickListener)

        // 2. Khi nhấn vào ô Check-out -> Cũng mở chung 1 bảng
        etCheckOut.setOnClickListener(dateRangePickerClickListener)

        // ======================================
    }

    /**
     * Hàm này hiển thị một bảng chọn KHOẢNG NGÀY (DateRangePicker)
     */
    private fun showDateRangePicker() {
        // 1. Tạo bảng chọn KHOẢNG NGÀY
        val builder = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select Dates")
            // Áp dụng Style (Theme) mới
            .setTheme(R.style.ThemeOverlay_App_MaterialCalendar)

        val datePicker = builder.build()

        // 2. Khi người dùng nhấn "OK"
        datePicker.addOnPositiveButtonClickListener { selection ->
            // 'selection' bây giờ là một Pair<Long, Long> (Ngày bắt đầu, Ngày kết thúc)

            // Lấy ngày bắt đầu
            val startDateMillis = selection.first
            // Lấy ngày kết thúc
            val endDateMillis = selection.second

            // Định dạng lại ngày tháng (từ Long -> String "dd-MM-yyyy")
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val startDateString = sdf.format(startDateMillis)
            val endDateString = sdf.format(endDateMillis)

            // Gán ngày đã định dạng vào CẢ HAI ô EditText
            etCheckIn.setText(startDateString)
            etCheckOut.setText(endDateString)
        }

        // 3. Hiển thị bảng chọn ngày
        datePicker.show(supportFragmentManager, "DATE_RANGE_PICKER_TAG")
    }
}