package com.example.androidproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R

class NotificationAdapter(private val items: List<NotificationItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 1. ViewHolder cho Tiêu đề (Header)
    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerTitle: TextView = itemView.findViewById(R.id.tv_header_title)
        fun bind(item: NotificationItem.Header) {
            headerTitle.text = item.title
        }
    }

    // 2. ViewHolder cho Thông báo (Notification)
    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconBackground: FrameLayout = itemView.findViewById(R.id.icon_background)
        private val icon: ImageView = itemView.findViewById(R.id.iv_notification_icon)
        private val content: TextView = itemView.findViewById(R.id.tv_notification_content)
        private val time: TextView = itemView.findViewById(R.id.tv_notification_time)

        fun bind(item: NotificationItem.Notification) {
            content.text = item.content
            time.text = item.time
            icon.setImageResource(item.iconResId)
            iconBackground.background = ContextCompat.getDrawable(itemView.context, item.iconBgResId)

            itemView.setOnClickListener {
                // TODO: Xử lý click vào thông báo
            }
        }
    }

    // 3. Quyết định loại view
    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is NotificationItem.Header -> NotificationItem.VIEW_TYPE_HEADER
            is NotificationItem.Notification -> NotificationItem.VIEW_TYPE_NOTIFICATION
        }
    }

    // 4. Tạo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == NotificationItem.VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification, parent, false)
            NotificationViewHolder(view)
        }
    }

    // 5. Gắn dữ liệu
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is NotificationItem.Header -> (holder as HeaderViewHolder).bind(item)
            is NotificationItem.Notification -> (holder as NotificationViewHolder).bind(item)
        }
    }

    override fun getItemCount() = items.size
}