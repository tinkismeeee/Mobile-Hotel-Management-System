package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.Locale

class checkout : AppCompatActivity() {
    private lateinit var hotelImg : ImageView
    private lateinit var tvRoomNumber : TextView
    private lateinit var tvRoomType : TextView
    private lateinit var tvDateRange : TextView
    private lateinit var tvGuests : TextView
    private lateinit var tvBedCount : TextView
    private lateinit var tvService : TextView
    private lateinit var etPromoCode : EditText
    private lateinit var applyBtn : Button
    private lateinit var checkBox : CheckBox
    private lateinit var btnConfirmPayment : Button
    private lateinit var tvRoomPrice: TextView
    private lateinit var tvTaxes: TextView
    private lateinit var tvDiscount: TextView
    private lateinit var tvTotal: TextView
    private lateinit var btnBack: ImageButton
    private var basePrice: Int = 0
    private var taxesAmount: Int = 0
    private var discountAmount: Int = 0
    private var finalTotalPrice: Int = 0

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
        tvRoomPrice = findViewById(R.id.tvRoomPrice)
        tvTaxes = findViewById(R.id.tvTaxes)
        tvDiscount = findViewById(R.id.Discount)
        tvTotal = findViewById(R.id.tvTotal)
        btnBack = findViewById(R.id.btnBack)
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment)

        btnBack.setOnClickListener {
            finish()
        }

        val roomNumber = intent.getStringExtra("roomNumber")
        val floor = intent.getIntExtra("floor", 0)
        val roomType = intent.getStringExtra("roomType")
        val maxGuests = intent.getIntExtra("maxGuests", 0)
        val bedCount = intent.getIntExtra("bedCount", 0)
        val totalPriceFromIntent = intent.getStringExtra("totalPrice")
        val listOfService = intent.getStringArrayListExtra("listOfService")
        val roomImageId = intent.getIntExtra("roomImageId", 0)
        val checkIn = intent.getStringExtra("checkIn")
        val checkOut = intent.getStringExtra("checkOut")

        basePrice = totalPriceFromIntent?.replace(".", "")?.toIntOrNull() ?: 0

        tvRoomNumber.text = "Room ${roomNumber} - Floor $floor"
        tvRoomType.text = "Room type: ${roomType}"
        tvDateRange.text = "${formatDate(checkIn)} to ${formatDate(checkOut)}"
        tvGuests.text = "Max number of guests: $maxGuests"
        tvBedCount.text = "Max number of bed: $bedCount"
        hotelImg.setImageResource(roomImageId)
        if (listOfService != null && listOfService.isNotEmpty()) {
            val mappedServices = mapServiceCodesToNames(listOfService)
            tvService.text = "- " + mappedServices.joinToString("\n- ")
        } else {
            tvService.text = "No services selected"
        }

        taxesAmount = (basePrice * 0.08).toInt()
        finalTotalPrice = (basePrice + taxesAmount) - discountAmount
        tvRoomPrice.text = "${formatMoney(basePrice)} VND"
        tvTaxes.text = "${formatMoney(taxesAmount)} VND"
        tvDiscount.text = "- ${formatMoney(discountAmount)} VND"
        tvTotal.text = "${formatMoney(finalTotalPrice)} VND"

        applyBtn.setOnClickListener { handleApplyPromoCode() }

        btnConfirmPayment.setOnClickListener {
            if (checkBox.isChecked) {
                val intent = Intent(this, receipt::class.java)
                intent.putExtra("totalPrice", finalTotalPrice.toString())
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "Please check the agreement", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleApplyPromoCode() {
        val userInputCode = etPromoCode.text.toString().trim()
        if (userInputCode.isEmpty()) {
            etPromoCode.error = "Please enter promotion code"
            return
        }
        RetrofitClient.instance.getAllPromotions()
            .enqueue(object : retrofit2.Callback<List<Promotion>> {
                override fun onResponse(
                    call: Call<List<Promotion>>,
                    response: retrofit2.Response<List<Promotion>>
                ) {
                    if (!response.isSuccessful || response.body() == null) {
                        etPromoCode.error = "Connecting to server failed"
                        return
                    }
                    val promo = response.body()!!.find { it.promotion_code == userInputCode }
                    if (promo == null) {
                        etPromoCode.error = "Invalid promo code"
                        return
                    }
                    val discountPercentage = promo.discount_value.toFloat().toInt()
                    discountAmount = (basePrice * discountPercentage) / 100
                    taxesAmount = (basePrice * 0.08).toInt()
                    finalTotalPrice = (basePrice + taxesAmount) - discountAmount
                    tvRoomPrice.text = "${formatMoney(basePrice)} VND"
                    tvTaxes.text = "${formatMoney(taxesAmount)} VND"
                    tvTotal.text = "${formatMoney(finalTotalPrice)} VND"
                    tvDiscount.text = "- ${formatMoney(discountAmount)} VND"
                    Toast.makeText(this@checkout, "Promo code applied!", Toast.LENGTH_SHORT).show()
                    etPromoCode.isEnabled = false
                    applyBtn.isEnabled = false
                }

                override fun onFailure(call: Call<List<Promotion>>, t: Throwable) {
                    t.printStackTrace()
                    etPromoCode.error = "Error connecting to server"
                }
            })
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

    private fun formatMoney(amount: Int): String {
        return "%,d".format(amount).replace(",", ".")
    }
}
