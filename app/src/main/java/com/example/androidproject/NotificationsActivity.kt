package com.example.androidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.google.android.material.appbar.MaterialToolbar

class NotificationsActivity : AppCompatActivity() {

    private lateinit var rvNotifications: RecyclerView
    private lateinit var notificationAdapter: NotificationAdapter
    private val notificationList = mutableListOf<NotificationItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        // 1. Tìm Views
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        rvNotifications = findViewById(R.id.rv_notifications)

        // 2. Thiết lập Toolbar
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_filter -> {
                    Toast.makeText(this, "Mở bộ lọc...", Toast.LENGTH_SHORT).show()
                    // TODO: Hiển thị BottomSheet (màn hình cuối)
                    true
                }
                else -> false
            }
        }

        // 3. Thiết lập RecyclerView
        setupRecyclerView()

        // 4. Tải dữ liệu giả
        loadDummyData()
    }

    private fun setupRecyclerView() {
        notificationAdapter = NotificationAdapter(notificationList)
        rvNotifications.apply {
            layoutManager = LinearLayoutManager(this@NotificationsActivity)
            adapter = notificationAdapter
        }
    }

    private fun loadDummyData() {
        notificationList.add(NotificationItem.Header("Today"))
        notificationList.add(NotificationItem.Notification(
            "Intercontinental Hotel has received new accommodation rates",
            "2 hours ago",
            R.drawable.ic_booking,
            R.drawable.bg_circle_blue
        ))
        notificationList.add(NotificationItem.Notification(
            "20% discount if you book on Sat/Sun 17 November 2024 at Clove Hotel",
            "3 hours ago",
            R.drawable.ic_home, // Giả sử dùng icon home, bạn nên tạo icon discount
            R.drawable.bg_circle_orange
        ))
        notificationList.add(NotificationItem.Notification(
            "Congratulations, you have successfully booked a room at Palm Gliss Resort",
            "5 hours ago",
            R.drawable.ic_booking,
            R.drawable.bg_circle_blue
        ))

        notificationList.add(NotificationItem.Header("Yesterday"))
        notificationList.add(NotificationItem.Notification(
            "Payment has been successful, your order is being processed",
            "1d ago",
            R.drawable.ic_calendar, // Giả sử dùng icon calendar, bạn nên tạo icon payment
            R.drawable.bg_circle_green
        ))
        notificationList.add(NotificationItem.Notification(
            "Free breakfast if you book for November 27, 2024",
            "1d ago",
            R.drawable.ic_home, // Giả sử
            R.drawable.bg_circle_orange
        ))

        notificationAdapter.notifyDataSetChanged()
    }
}