package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidproject.utils.MockData
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class RoomDetailActivity : AppCompatActivity() {

    private lateinit var tvHotelName: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvDescription: TextView
    private lateinit var chipGroupServices: ChipGroup
    private lateinit var tvDateRange: TextView
    private lateinit var layoutDateSelect: LinearLayout
    private lateinit var btnBookNow: Button
    private lateinit var etCheckIn: TextView
    private lateinit var etCheckOut: TextView

    private var currentRoomId: Int = 1
    private var allowedServices: ArrayList<String> = arrayListOf()
    private val currencyFormat = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_detail)

        // Ánh xạ View
        tvHotelName = findViewById(R.id.tvDetailHotelName)
        tvPrice = findViewById(R.id.tvDetailPrice)
        tvDescription = findViewById(R.id.tvDescription)
        chipGroupServices = findViewById(R.id.chipGroupServices)
        tvDateRange = findViewById(R.id.tvDateRange)
        layoutDateSelect = findViewById(R.id.layoutDateSelect)
        btnBookNow = findViewById(R.id.btnBookNow)
        etCheckIn = findViewById(R.id.etCheckIn)
        etCheckOut = findViewById(R.id.etCheckOut)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        // Nhận dữ liệu
        val roomNumber = intent.getStringExtra("ROOM_NUMBER") ?: ""
        val price = intent.getDoubleExtra("ROOM_PRICE", 0.0)
        currentRoomId = intent.getIntExtra("ROOM_ID", 1)
        allowedServices = intent.getStringArrayListExtra("ALLOWED_SERVICES") ?: arrayListOf("SV001", "SV002")
        val desc = intent.getStringExtra("ROOM_DESC")

        // Hiển thị
        tvHotelName.text = "Phòng $roomNumber"
        tvPrice.text = "${currencyFormat.format(price)} VND"
        tvDescription.text = if (!desc.isNullOrEmpty()) desc else "Phòng tiện nghi..."

        // Tạo Chips
        val allServices = MockData.getMockServices()
        chipGroupServices.removeAllViews()
        for (code in allowedServices) {
            val service = allServices.find { it.serviceCode == code }
            if (service != null) {
                val chip = Chip(this)
                chip.text = service.name
                chip.setChipBackgroundColorResource(android.R.color.white)
                chip.isCheckable = false
                chipGroupServices.addView(chip)
            }
        }

        // Ngày mặc định
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val today = sdf.format(Date())
        val tomorrow = sdf.format(Date(System.currentTimeMillis() + 86400000))
        etCheckIn.text = today
        etCheckOut.text = tomorrow
        tvDateRange.text = "$today - $tomorrow"

        layoutDateSelect.setOnClickListener { showDateRangePicker() }

        btnBookNow.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            intent.putExtra("HOTEL_NAME", tvHotelName.text.toString())
            intent.putExtra("HOTEL_LOCATION", "Chi tiết phòng")
            intent.putExtra("CHECK_IN_DATE", etCheckIn.text.toString())
            intent.putExtra("CHECK_OUT_DATE", etCheckOut.text.toString())
            intent.putExtra("ROOM_ID", currentRoomId)
            intent.putStringArrayListExtra("ALLOWED_SERVICES", allowedServices)
            startActivity(intent)
        }
    }

    private fun showDateRangePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker().setTitleText("Chọn ngày")
        val datePicker = builder.build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            etCheckIn.text = sdf.format(selection.first)
            etCheckOut.text = sdf.format(selection.second)
            tvDateRange.text = "${etCheckIn.text} - ${etCheckOut.text}"
        }
        datePicker.show(supportFragmentManager, "DATE_PICKER")
    }
}