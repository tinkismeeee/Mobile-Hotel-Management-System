package com.example.androidproject

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Locale

class checkout : AppCompatActivity() {
    lateinit var hotelImg : ImageView
    lateinit var tvRoomNumber : TextView
    lateinit var tvRoomType : TextView
    lateinit var tvDateRange : TextView
    lateinit var tvGuests : TextView
    lateinit var tvBedCount : TextView
    lateinit var tvService : TextView
    lateinit var etPromoCode : EditText
    lateinit var applyBtn : Button
    lateinit var checkBox : CheckBox
    lateinit var btnConfirmPayment : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_checkout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        hotelImg = findViewById(R.id.hotelImg)
        tvRoomNumber = findViewById(R.id.tvRoomNumber)
        tvRoomType = findViewById(R.id.tvRoomType)
        tvDateRange = findViewById(R.id.tvDateRange)
        tvGuests = findViewById(R.id.tvGuests)
        tvBedCount = findViewById(R.id.tvBedCount)
        tvService = findViewById(R.id.tvService)
        etPromoCode = findViewById(R.id.etPromoCode)
        applyBtn = findViewById(R.id.btnApplyPromo)
        checkBox = findViewById(R.id.cbAgreement)
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment)
//        intent.putExtra("roomNumber", booking.roomNumber)
//        intent.putExtra("floor", booking.floor)
//        intent.putExtra("roomType", booking.roomType)
//        intent.putExtra("maxGuests", booking.maxGuests)
//        intent.putExtra("bedCount", booking.bedCount)
//        intent.putExtra("totalPrice", formatMoney(finalTotal))
//        intent.putStringArrayListExtra("listOfService", ArrayList(booking.services))
//        intent.putExtra("roomImageId", imageResId)
//        intent.putExtra("checkIn", booking.checkIn)
//        intent.putExtra("checkOut", booking.checkOut)

        val roomNumber = intent.getStringExtra("roomNumber")
        val floor = intent.getIntExtra("floor", 0)
        val roomType = intent.getStringExtra("roomType")
        val maxGuests = intent.getIntExtra("maxGuests", 0)
        val bedCount = intent.getIntExtra("bedCount", 0)
        val totalPrice = intent.getStringExtra("totalPrice")
        val listOfService = intent.getStringArrayListExtra("listOfService")
        val roomImageId = intent.getIntExtra("roomImageId", 0)
        val checkIn = intent.getStringExtra("checkIn")
        val checkOut = intent.getStringExtra("checkOut")

        tvRoomNumber.text = "Room ${roomNumber} - Floor $floor"
        tvRoomType.text = "Room type: ${roomType}"
        tvDateRange.text = "${formatDate(checkIn)} to ${formatDate(checkOut)}"
        tvGuests.text = "Max number of guests: $maxGuests"
        tvBedCount.text = "Max number of bed: $bedCount"
        hotelImg.setImageResource(roomImageId)
        if (listOfService != null) {
            val mappedServices = mapServiceCodesToNames(listOfService)
            tvService.text = "- " + mappedServices.joinToString("\n- ")
        } else {
            tvService.text = "No services selected"
        }

        applyBtn.setOnClickListener {
            if (etPromoCode.text.isEmpty()) {
                return@setOnClickListener
            }

        }


    }

    private fun mapServiceCodesToNames(serviceCodes: List<String>): List<String> {
        val serviceMap = mapOf(
            "SV001" to "Laundry and ironing service",
            "SV002" to "Buffet breakfast",
            "SV003" to "Pickup from airport",
            "SV004" to "Relaxing massage and spa",
            "SV005" to "Dinner buffet at restaurant",
            "SV006" to "In-room mini bar",
            "SV007" to "Additional bed for extra guest",
            "SV008" to "Daily city tour guide"
        )
        return serviceCodes.map { code -> serviceMap[code] ?: "Unknown service" }
    }

    private fun formatDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return ""
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            if (date != null) outputFormat.format(date) else dateString
        } catch (e: Exception) {
            dateString
        }
    }
}