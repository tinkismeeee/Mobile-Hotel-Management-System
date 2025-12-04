package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView // [QUAN TRỌNG] Dùng TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidproject.api.RetrofitClient
import com.example.androidproject.models.ServiceResponse
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    // [SỬA LỖI] Dùng TextView (không dùng TextInputEditText) để tránh Crash
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

        // [SỬA LỖI] Ánh xạ đúng loại View
        etCheckIn = findViewById(R.id.etCheckIn)
        etCheckOut = findViewById(R.id.etCheckOut)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        // Nhận dữ liệu
        val roomNumber = intent.getStringExtra("ROOM_NUMBER") ?: ""
        val price = intent.getDoubleExtra("ROOM_PRICE", 0.0)
        val roomType = intent.getStringExtra("ROOM_TYPE") ?: ""
        currentRoomId = intent.getIntExtra("ROOM_ID", 1)
        allowedServices = intent.getStringArrayListExtra("ALLOWED_SERVICES") ?: arrayListOf("SV001", "SV002")
        val desc = intent.getStringExtra("ROOM_DESC")

        // Hiển thị thông tin
        tvHotelName.text = if(roomType.isNotEmpty()) "$roomType - P.$roomNumber" else "Phòng $roomNumber"
        tvPrice.text = "${currencyFormat.format(price)} VND"
        tvDescription.text = if (!desc.isNullOrEmpty()) desc else "Phòng tiện nghi..."

        // Gọi API lấy tên dịch vụ để hiển thị
        loadServicesForChips()

        // Ngày mặc định (Hôm nay -> Ngày mai)
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

    private fun loadServicesForChips() {
        RetrofitClient.instance.getServices().enqueue(object : Callback<List<ServiceResponse>> {
            override fun onResponse(call: Call<List<ServiceResponse>>, response: Response<List<ServiceResponse>>) {
                if (response.isSuccessful) {
                    val allServices = response.body() ?: emptyList()
                    chipGroupServices.removeAllViews()
                    for (service in allServices) {
                        if (allowedServices.contains(service.serviceCode)) {
                            val chip = Chip(this@RoomDetailActivity)
                            chip.text = service.name
                            chip.setChipBackgroundColorResource(android.R.color.white)
                            chip.setChipStrokeColorResource(android.R.color.black)
                            chip.setChipStrokeWidth(1f)
                            chip.isCheckable = false
                            chipGroupServices.addView(chip)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<ServiceResponse>>, t: Throwable) {}
        })
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