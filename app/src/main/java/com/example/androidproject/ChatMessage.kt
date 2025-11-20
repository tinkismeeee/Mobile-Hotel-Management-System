package com.example.androidproject

data class ChatMessage(
    val messageText: String,
    val timestamp: String,
    val viewType: Int // Dùng để phân biệt tin nhắn gửi và nhận
) {
    companion object {
        const val VIEW_TYPE_SENT = 1 // Tin nhắn gửi (phải)
        const val VIEW_TYPE_RECEIVED = 2 // Tin nhắn nhận (trái)
    }
}