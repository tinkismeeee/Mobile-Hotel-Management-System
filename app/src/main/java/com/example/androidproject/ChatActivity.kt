package com.example.androidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView
    private lateinit var etMessageInput: EditText
    private lateinit var btnSend: FloatingActionButton
    private lateinit var ivBack: ImageView

    private lateinit var chatAdapter: ChatAdapter
    private val messageList = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // 1. Tìm Views
        rvChat = findViewById(R.id.rv_chat)
        etMessageInput = findViewById(R.id.et_message_input)
        btnSend = findViewById(R.id.btn_send)
        ivBack = findViewById(R.id.iv_back) // Nút back trong toolbar tùy chỉnh

        // 2. Thiết lập RecyclerView
        setupRecyclerView()

        // 3. Thêm dữ liệu giả (demo)
        loadDummyMessages()

        // 4. Xử lý sự kiện click
        ivBack.setOnClickListener {
            finish() // Đóng Activity, quay lại màn hình trước
        }

        btnSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messageList)
        rvChat.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                stackFromEnd = true // Tin nhắn mới sẽ ở dưới cùng
            }
            adapter = chatAdapter
        }
    }

    private fun loadDummyMessages() {
        messageList.add(ChatMessage(
            "Hi, I want to book this hotel. Is it available for 2 nights?",
            "10:00 AM",
            ChatMessage.VIEW_TYPE_RECEIVED
        ))
        messageList.add(ChatMessage(
            "Hi, Hélèna",
            "10:01 AM",
            ChatMessage.VIEW_TYPE_SENT
        ))
        messageList.add(ChatMessage(
            "Yes, it's available. You can book now.",
            "10:01 AM",
            ChatMessage.VIEW_TYPE_SENT
        ))
        chatAdapter.notifyDataSetChanged()
    }

    private fun sendMessage() {
        val messageText = etMessageInput.text.toString().trim()
        if (messageText.isNotEmpty()) {
            // Lấy thời gian hiện tại
            val currentTime = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())

            // Tạo tin nhắn mới (mặc định là tin nhắn gửi)
            val newMessage = ChatMessage(
                messageText,
                currentTime,
                ChatMessage.VIEW_TYPE_SENT
            )

            // Thêm vào danh sách và cập nhật adapter
            messageList.add(newMessage)
            chatAdapter.notifyItemInserted(messageList.size - 1)

            // Cuộn xuống tin nhắn mới nhất
            rvChat.scrollToPosition(messageList.size - 1)

            // Xóa nội dung trong ô nhập liệu
            etMessageInput.text.clear()
        } else {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }
}