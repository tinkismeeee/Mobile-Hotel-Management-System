package com.example.androidproject // Sửa 1: Đổi package name cho đúng

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R // Sửa 2: Import file R của project
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MessagesActivity : AppCompatActivity() {

    private lateinit var rvMessages: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: MutableList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        // 1. Tìm Views
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val fab: FloatingActionButton = findViewById(R.id.fab_new_message)

        // Dòng này sẽ hết báo lỗi sau khi bạn Rebuild
        rvMessages = findViewById(R.id.rv_messages)

        // 2. Thiết lập Toolbar
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_search -> {
                    Toast.makeText(this, "Tìm kiếm...", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        // 3. Thiết lập Bottom Navigation
        // Đặt item "Message" được chọn
        bottomNav.selectedItemId = R.id.nav_message
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { /* Chuyển đến Home */ true }
                R.id.nav_booking -> { /* Chuyển đến Booking */ true }
                R.id.nav_message -> { /* Đã ở đây */ true }
                R.id.nav_profile -> { /* Chuyển đến Profile */ true }
                else -> false
            }
        }

        // 4. Thiết lập RecyclerView
        setupRecyclerView()

        // 5. Xử lý click FAB
        fab.setOnClickListener {
            Toast.makeText(this, "Soạn tin nhắn mới...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        // SỬA 3: Tạm thời tạo một danh sách rỗng để tránh lỗi build
        messageList = mutableListOf()

        /*
        // Dữ liệu giả này đang gây lỗi vì bạn chưa thêm ảnh avatar1, avatar2...
        // Chúng ta sẽ tạm thời comment (vô hiệu hóa) nó
        messageList = mutableListOf(
            Message("Helena Greenville", "Hello, how are you?", "10:00 AM", R.drawable.avatar1),
            Message("Lavina Fansel", "Are you available for...", "09:30 AM", R.drawable.avatar2),
            Message("Ancu Henzel", "See you at 5 PM", "Yesterday", R.drawable.avatar3),
            Message("Clia Haley", "Thanks!", "Yesterday", R.drawable.avatar4),
            Message("Taa Maggie", "Okay, noted.", "2d ago", R.drawable.avatar5),
            Message("Mathewe Kwampelski", "Project update...", "2d ago", R.drawable.avatar6)
        )
        */

        // (Bạn cần thêm các ảnh avatar (avatar1, avatar2,...) vào drawable)

        // Khởi tạo Adapter và gán cho RecyclerView
        messageAdapter = MessageAdapter(messageList)
        rvMessages.apply {
            layoutManager = LinearLayoutManager(this@MessagesActivity)
            adapter = messageAdapter
        }
    }
}