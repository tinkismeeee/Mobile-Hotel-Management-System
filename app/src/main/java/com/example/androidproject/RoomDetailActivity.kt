package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class RoomDetailActivity : AppCompatActivity() {

    private lateinit var ivDetailImage: ImageView
    private lateinit var tvHotelName: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvLocation: TextView
    private lateinit var etCheckIn: TextInputEditText
    private lateinit var etCheckOut: TextInputEditText
    private lateinit var btnBack: ImageButton
    private lateinit var btnBookNow: MaterialButton

    // Biến lưu thông tin phòng
    private var currentRoomId: Int = 1
    private var currentPrice: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_room_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Ánh xạ View
        ivDetailImage = findViewById(R.id.ivDetailImage)
        tvHotelName = findViewById(R.id.tvDetailHotelName)
        tvRating = findViewById(R.id.tvDetailRating)
        tvLocation = findViewById(R.id.tvDetailLocation) // Dùng cái này để hiện tiện ích
        etCheckIn = findViewById(R.id.etCheckIn)
        etCheckOut = findViewById(R.id.etCheckOut)
        btnBack = findViewById(R.id.btnBack)
        btnBookNow = findViewById(R.id.btnBookNow)

        // 2. NHẬN DỮ LIỆU TỪ ROOMLIST ACTIVITY
        // Các key này phải khớp với bên Adapter
        val roomNumber = intent.getStringExtra("ROOM_NUMBER") ?: "Phòng VIP"
        val roomDesc = intent.getStringExtra("ROOM_DESC") ?: "Đầy đủ tiện nghi"
        currentPrice = intent.getDoubleExtra("ROOM_PRICE", 0.0)
        currentRoomId = intent.getIntExtra("ROOM_ID", 1)

        // 3. HIỂN THỊ DỮ LIỆU LÊN GIAO DIỆN
        tvHotelName.text = "Phòng số: $roomNumber"

        // Hiển thị tiện ích vào chỗ Location cũ
        tvLocation.text = "Tiện ích: $roomDesc"

        // Giá trị rating giả lập hoặc lấy từ API nếu có
        tvRating.text = "4.8"

        // Hiển thị ảnh mặc định (hoặc xử lý ảnh nếu API có trả về URL)
        ivDetailImage.setImageResource(R.drawable.hotel_img)

        // 4. Xử lý nút Back
        btnBack.setOnClickListener {
            finish()
        }

        // 5. Xử lý chọn ngày (Date Picker)
        val dateRangePickerClickListener = View.OnClickListener {
            showDateRangePicker()
        }
        etCheckIn.setOnClickListener(dateRangePickerClickListener)
        etCheckOut.setOnClickListener(dateRangePickerClickListener)

        // 6. Xử lý nút Đặt Ngay (Book Now)
        btnBookNow.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)

            // Truyền thông tin hiển thị sang trang Checkout
            intent.putExtra("HOTEL_NAME", tvHotelName.text.toString())
            intent.putExtra("HOTEL_LOCATION", tvLocation.text.toString()) // Truyền tiện ích sang để hiển thị chơi
            intent.putExtra("CHECK_IN_DATE", etCheckIn.text.toString())
            intent.putExtra("CHECK_OUT_DATE", etCheckOut.text.toString())

            // [QUAN TRỌNG] Truyền ID phòng để trang Checkout gọi API tính tiền
            intent.putExtra("ROOM_ID", currentRoomId)

            startActivity(intent)
        }
    }

    /**
     * Hàm hiển thị lịch chọn ngày
     */
    private fun showDateRangePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Chọn ngày lưu trú")
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