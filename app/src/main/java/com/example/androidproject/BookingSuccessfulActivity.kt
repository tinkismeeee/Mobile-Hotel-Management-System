package com.example.androidproject

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BookingSuccessfulActivity : AppCompatActivity() {

    private lateinit var btnViewReceipt: Button
    private lateinit var btnBackToHome: TextView
    private var hotelName: String? = null
    private var dates: String? = null
    private var guests: String? = null
    private var price: String? = null
    private var tax: String? = null
    private var total: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_booking_successful)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnViewReceipt = findViewById(R.id.btnViewReceipt)
        btnBackToHome = findViewById(R.id.btnBackToHome)

        hotelName = intent.getStringExtra("HOTEL_NAME")
        dates = intent.getStringExtra("DATES")
        guests = intent.getStringExtra("GUESTS")
        price = intent.getStringExtra("PRICE")
        tax = intent.getStringExtra("TAX")
        total = intent.getStringExtra("TOTAL")

        showSuccessNotification()

        btnViewReceipt.setOnClickListener {
            val receiptIntent = Intent(this, EReceiptActivity::class.java)
            receiptIntent.putExtra("HOTEL_NAME", hotelName)
            receiptIntent.putExtra("DATES", dates)
            receiptIntent.putExtra("GUESTS", guests)
            receiptIntent.putExtra("PRICE", price)
            receiptIntent.putExtra("TAX", tax)
            receiptIntent.putExtra("TOTAL", total)
            startActivity(receiptIntent)
        }

        btnBackToHome.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun showSuccessNotification() {
        val channelId = "booking_success_channel"
        val notificationId = 1

        val displayHotel = hotelName ?: "Khách sạn"
        val displayTotal = total ?: ""
        val contentText = "Bạn đã đặt phòng tại $displayHotel thành công. Tổng tiền: $displayTotal"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Booking Notifications"
            val descriptionText = "Thông báo trạng thái đặt phòng"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, EReceiptActivity::class.java).apply {
            putExtra("HOTEL_NAME", hotelName)
            putExtra("DATES", dates)
            putExtra("GUESTS", guests)
            putExtra("PRICE", price)
            putExtra("TAX", tax)
            putExtra("TOTAL", total)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Đặt phòng thành công!")
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }

        NotificationManagerCompat.from(this).notify(notificationId, builder.build())
    }
}