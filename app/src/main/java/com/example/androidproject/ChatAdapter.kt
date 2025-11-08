package com.example.androidproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val chatMessages: List<ChatMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 1. ViewHolder cho tin nhắn NHẬN (trái)
    inner class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.tv_message_text)
        val timestamp: TextView = itemView.findViewById(R.id.tv_timestamp)

        fun bind(message: ChatMessage) {
            messageText.text = message.messageText
            timestamp.text = message.timestamp
        }
    }

    // 2. ViewHolder cho tin nhắn GỬI (phải)
    inner class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.tv_message_text)
        val timestamp: TextView = itemView.findViewById(R.id.tv_timestamp)

        fun bind(message: ChatMessage) {
            messageText.text = message.messageText
            timestamp.text = message.timestamp
        }
    }

    // 3. Quyết định xem item ở vị trí `position` là loại nào
    override fun getItemViewType(position: Int): Int {
        return chatMessages[position].viewType
    }

    // 4. Tạo ViewHolder tương ứng với viewType
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ChatMessage.VIEW_TYPE_RECEIVED) {
            // Nếu là tin nhắn nhận, dùng layout item_chat_received
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_received, parent, false)
            ReceivedMessageViewHolder(view)
        } else {
            // Nếu là tin nhắn gửi, dùng layout item_chat_sent
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_sent, parent, false)
            SentMessageViewHolder(view)
        }
    }

    // 5. Gắn dữ liệu vào ViewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatMessages[position]
        if (holder.itemViewType == ChatMessage.VIEW_TYPE_RECEIVED) {
            (holder as ReceivedMessageViewHolder).bind(message)
        } else {
            (holder as SentMessageViewHolder).bind(message)
        }
    }

    override fun getItemCount() = chatMessages.size
}