package com.example.androidproject

import com.example.androidproject.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class MessageAdapter(private val messageList: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val avatar: ShapeableImageView = itemView.findViewById(R.id.iv_avatar)

        // Các dòng này sẽ hết báo lỗi sau khi Rebuild
        val userName: TextView = itemView.findViewById(R.id.tv_user_name)
        val lastMessage: TextView = itemView.findViewById(R.id.tv_last_message)
        val timestamp: TextView = itemView.findViewById(R.id.tv_timestamp)

        fun bind(message: Message) {
            avatar.setImageResource(message.avatarResId)
            userName.text = message.userName
            lastMessage.text = message.lastMessage
            timestamp.text = message.timestamp

            // Xử lý click cho item
            itemView.setOnClickListener {
                // TODO: Mở màn hình chat chi tiết
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messageList[position])
    }

    override fun getItemCount() = messageList.size
}