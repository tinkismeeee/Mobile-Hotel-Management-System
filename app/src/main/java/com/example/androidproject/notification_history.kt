package com.example.androidproject

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Notification_History : AppCompatActivity() {

    private lateinit var adapter: NotificationHistoryAdapter
    private lateinit var notifications: MutableList<NotificationItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_history)

        notifications = NotificationStorage.getAll(this).toMutableList()

        val recycler = findViewById<RecyclerView>(R.id.recyclerNotifications)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = NotificationHistoryAdapter(notifications)
        recycler.adapter = adapter

        findViewById<ImageView>(R.id.clearBtn).setOnClickListener {
            NotificationStorage.clearAll(this)
            notifications.clear()
            adapter.notifyDataSetChanged()
        }

        findViewById<ImageView>(R.id.backBtn).setOnClickListener {
            finish()
        }
    }
}
