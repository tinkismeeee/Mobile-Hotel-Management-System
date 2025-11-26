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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_room_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ivDetailImage = findViewById(R.id.ivDetailImage)
        tvHotelName = findViewById(R.id.tvDetailHotelName)
        tvRating = findViewById(R.id.tvDetailRating)
        tvLocation = findViewById(R.id.tvDetailLocation)
        etCheckIn = findViewById(R.id.etCheckIn)
        etCheckOut = findViewById(R.id.etCheckOut)
        btnBack = findViewById(R.id.btnBack)
        btnBookNow = findViewById(R.id.btnBookNow)

        val hotelName = intent.getStringExtra("HOTEL_NAME")
        val location = intent.getStringExtra("HOTEL_LOCATION")
        val rating = intent.getStringExtra("HOTEL_RATING")
        val photoReference = intent.getStringExtra("HOTEL_PHOTO_REFERENCE")

        tvHotelName.text = hotelName
        tvLocation.text = location
        tvRating.text = rating
        if (photoReference != null) {
            val apiKey = "AIzaSyBBa7totCeTLg3APty-NckqqQK8nnRhrJc"
            // Lấy ảnh chất lượng cao hơn cho màn hình chi tiết (maxwidth=800)
            val photoUrl = "https://maps.googleapis.com/maps/api/place/photo" +
                    "?maxwidth=800" +
                    "&photoreference=$photoReference" +
                    "&key=$apiKey"

            Glide.with(this)
                .load(photoUrl)
                .centerCrop()
                .placeholder(R.drawable.hotel_img) // Ảnh mặc định khi đang tải
                .error(R.drawable.hotel_img)       // Ảnh mặc định nếu lỗi
                .into(ivDetailImage)
        } else {
            ivDetailImage.setImageResource(R.drawable.hotel_img)
        }

        btnBack.setOnClickListener {
            finish()
        }

        val dateRangePickerClickListener = View.OnClickListener {
            showDateRangePicker()
        }
        etCheckIn.setOnClickListener(dateRangePickerClickListener)
        etCheckOut.setOnClickListener(dateRangePickerClickListener)

        btnBookNow.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            intent.putExtra("HOTEL_NAME", tvHotelName.text.toString())
            intent.putExtra("HOTEL_LOCATION", tvLocation.text.toString())
            intent.putExtra("CHECK_IN_DATE", etCheckIn.text.toString())
            intent.putExtra("CHECK_OUT_DATE", etCheckOut.text.toString())
            startActivity(intent)
        }
    }

    /**
     * Hàm này hiển thị một bảng chọn KHOẢNG NGÀY (DateRangePicker)
     */
    private fun showDateRangePicker() {
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