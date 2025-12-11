package com.example.androidproject

import BookingRealm
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID
import io.realm.kotlin.ext.query

class room_detail : AppCompatActivity() {
    companion object {
        private const val CHANNEL_ID = "booking_channel"
        private const val NOTIF_ID = 1001
    }

    lateinit var btnBookNow : Button
    lateinit var laundry_service : Button
    lateinit var breakfast_service : Button
    lateinit var pickup_service : Button
    lateinit var massage_service : Button
    lateinit var dinner_service : Button
    lateinit var minibar_service : Button
    lateinit var additionalbed_service : Button
    lateinit var localguide_service : Button
    lateinit var roomNumber : TextView
    lateinit var RoomType : TextView
    lateinit var MaxGuests : TextView
    lateinit var BedCount : TextView
    lateinit var Description : TextView
    lateinit var Floor: TextView
    lateinit var imageView : ImageView
    lateinit var layoutDateSelect : LinearLayout
    lateinit var tvDateRange: TextView
    private var apiCheckIn = ""
    private var apiCheckOut = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_room_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        createNotificationChannel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1002)
            }
        }

        // find views
        btnBookNow = findViewById(R.id.btnBookNow)
        laundry_service = findViewById(R.id.laundry_service)
        breakfast_service = findViewById(R.id.breakfast_service)
        pickup_service = findViewById(R.id.pickup_service)
        massage_service = findViewById(R.id.massage_service)
        dinner_service = findViewById(R.id.dinner_service)
        minibar_service = findViewById(R.id.minibar_service)
        additionalbed_service = findViewById(R.id.additionalbed_service)
        localguide_service = findViewById(R.id.localguide_service)

        imageView = findViewById<ImageView>(R.id.ivDetailImage)
        roomNumber = findViewById(R.id.roomNumber)
        RoomType = findViewById(R.id.tvRoomType)
        Floor = findViewById(R.id.tvFloor)
        BedCount = findViewById(R.id.tvBedCount)
        MaxGuests = findViewById(R.id.tvMaxGuests)
        Description = findViewById(R.id.tvDescription)

        layoutDateSelect = findViewById(R.id.layoutDateSelect)
        tvDateRange = findViewById(R.id.tvDateRange)

        val roomNumberVal = intent.getStringExtra("roomNumber")
        val floorVal = intent.getIntExtra("floor", 0)
        val roomTypeVal = intent.getStringExtra("roomTypeName")
        val maxGuestsVal = intent.getIntExtra("maxGuests", 0)
        val bedCountVal = intent.getIntExtra("bedCount", 0)
        val descriptionVal = intent.getStringExtra("description")
        val roomImageId = intent.getIntExtra("roomImageId", 0)
        val roomId = intent.getIntExtra("room_id", 0)

        val selectedServices = mutableListOf<String>()

        imageView.setImageResource(roomImageId)
        roomNumber.text = ("Room: " + roomNumberVal) ?: ""
        RoomType.text = roomTypeVal ?: ""
        Floor.text = floorVal.toString() ?: "0"
        BedCount.text = bedCountVal.toString() ?: "0"
        MaxGuests.text = maxGuestsVal.toString() ?: "0"
        Description.text = descriptionVal ?: ""

        layoutDateSelect.setOnClickListener {
            showDateRangePicker { displayStart, displayEnd, apiStart, apiEnd ->
                tvDateRange.text = "$displayStart - $displayEnd"
                tvDateRange.tag = Pair(apiStart, apiEnd)
            }
        }

        laundry_service.setOnClickListener { toggleServiceButton(laundry_service) }
        breakfast_service.setOnClickListener { toggleServiceButton(breakfast_service) }
        pickup_service.setOnClickListener { toggleServiceButton(pickup_service) }
        massage_service.setOnClickListener { toggleServiceButton(massage_service) }
        dinner_service.setOnClickListener { toggleServiceButton(dinner_service) }
        minibar_service.setOnClickListener { toggleServiceButton(minibar_service) }
        additionalbed_service.setOnClickListener { toggleServiceButton(additionalbed_service) }
        localguide_service.setOnClickListener { toggleServiceButton(localguide_service) }

        btnBookNow.setOnClickListener {
            printAllBookings()

            if (tvDateRange.text == "Chọn ngày") {
                Toast.makeText(this, "Please select date range", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val masterKey = MasterKey.Builder(this)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            val session = EncryptedSharedPreferences.create(
                this,
                getString(R.string.session_file_name),
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            val userEmail = session.getString("email", null)
            RetrofitClient.instance.getCustomerByEmail(userEmail!!)
                .enqueue(object : retrofit2.Callback<User1> {
                    override fun onResponse(call: Call<User1>, response: Response<User1>) {
                        if (response.isSuccessful) {
                            val user = response.body()
                            val userId = user?.user_id ?: 0
                            Log.i("DEBUG", userId.toString())
                            createBooking(roomId, userId)
                        } else {
                            Log.e("API", "Error: ${response.code()}")
                        }
                    }
                    override fun onFailure(call: Call<User1>, t: Throwable) {
                        Log.e("API_ERR", t.message ?: "Unknown error")
                    }
                })
        }
    }

    private fun showDateRangePicker(onSelected: (String, String, String, String) -> Unit) {
        val constraints = CalendarConstraints.Builder()
            .setStart(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select Date Range")
            .setCalendarConstraints(constraints)
            .build()

        picker.addOnPositiveButtonClickListener { range ->
            val start = range.first
            val end = range.second

            val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val apiFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val displayStart = displayFormat.format(start)
            val displayEnd = displayFormat.format(end)

            val apiStart = apiFormat.format(start)
            val apiEnd = apiFormat.format(end)

            onSelected(displayStart, displayEnd, apiStart, apiEnd)
        }

        picker.show(supportFragmentManager, "date_picker")
    }

    private fun toggleServiceButton(button: Button) {
        button.isSelected = !button.isSelected
        if (button.isSelected) {
            button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.button_color)
            button.setTextColor(ContextCompat.getColor(this, R.color.white))
        } else {
            button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.gray)
            button.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
    }

    private fun createBooking(roomId: Int, userId: Int) {

        val apiDates = tvDateRange.tag as? Pair<String, String>
        if (apiDates == null) {
            Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show()
            return
        }

        val apiCheckIn = apiDates.first
        val apiCheckOut = apiDates.second

        val serviceMap = listOf(
            laundry_service to "SV001",
            breakfast_service to "SV002",
            pickup_service to "SV003",
            massage_service to "SV004",
            dinner_service to "SV005",
            minibar_service to "SV006",
            additionalbed_service to "SV007",
            localguide_service to "SV008"
        )

        val selectedServices = serviceMap
            .filter { it.first.isSelected }
            .map { it.second }

        val bookingId = UUID.randomUUID().toString()
        val priceString = intent.getStringExtra("pricePerNight")
        val price = priceString?.toDoubleOrNull()?.toInt() ?: 0

        val roomNumberVal = intent.getStringExtra("roomNumber")
        val floorVal = intent.getIntExtra("floor", 0)
        val roomTypeVal = intent.getStringExtra("roomTypeName")
        val maxGuestsVal = intent.getIntExtra("maxGuests", 0)
        val bedCountVal = intent.getIntExtra("bedCount", 0)
        val descriptionVal = intent.getStringExtra("description")
        val roomImageId = intent.getIntExtra("roomImageId", 0)
        val roomId = intent.getIntExtra("room_id", 0)
        val realm = App.realm
        realm.writeBlocking {
            copyToRealm(
                BookingRealm().apply {
                    this.roomNumber = roomNumberVal ?: ""
                    this.floor = floorVal
                    this.roomType = roomTypeVal ?: ""
                    this.maxGuests = maxGuestsVal
                    this.bedCount = bedCountVal
                    this.description = descriptionVal ?: ""
                    this.bookingId = bookingId
                    this.roomId = roomId
                    this.userId = userId
                    this.checkIn = apiCheckIn
                    this.checkOut = apiCheckOut
                    this.totalGuests = MaxGuests.text.toString().toInt()
                    this.price = price
                    this.services.addAll(selectedServices)
                }
            )
        }

        val notifEnabled = NotificationManagerCompat.from(this).areNotificationsEnabled()
        if (!notifEnabled) {
            Toast.makeText(this, "Notifications are disabled for this app. Please enable them in system settings.", Toast.LENGTH_LONG).show()
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Booking Successful")
            .setContentText("Your booking has been created!")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Your booking has been created! Booking ID: $bookingId"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)

        try {
            NotificationManagerCompat.from(this).notify(NOTIF_ID, builder.build())
        } catch (e: Exception) {
            Log.e("NOTIF_ERROR", "Failed to send notification", e)
        }

        try {
            NotificationStorage.save(
                this,
                NotificationItem(
                    "Booking Successful",
                    "Your booking has been successfully created! Booking ID: $bookingId",
                    System.currentTimeMillis()
                )
            )
        } catch (e: Exception) {
            Log.e("NOTIF_SAVE_ERR", "Failed to save notification", e)
        }

        printAllBookings()

        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 600)
    }

    private fun printAllBookings() {
        val realm = App.realm
        val bookings = realm.query<BookingRealm>().find()

        Log.i("DEBUG", "------ ALL BOOKINGS ------")
        bookings.forEach { b ->
            Log.i("RDEBUG", """
            RoomNumber: ${b.roomNumber}
            Floor: ${b.floor}
            RoomType: ${b.roomType}
            MaxGuests: ${b.maxGuests}
            BedCount: ${b.bedCount}
            Description: ${b.description}
            BookingId: ${b.bookingId}
            RoomId: ${b.roomId}
            UserId: ${b.userId}
            Check-in: ${b.checkIn}
            Check-out: ${b.checkOut}
            Guests: ${b.totalGuests}
            Price: ${b.price}
            Services: ${b.services.joinToString()}
        """.trimIndent())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, "Booking Channel", importance)
            channel.description = "Notifications for successful bookings"
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
