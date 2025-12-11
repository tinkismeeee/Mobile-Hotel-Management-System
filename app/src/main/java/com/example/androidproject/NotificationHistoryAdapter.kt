package com.example.androidproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class NotificationHistoryAdapter(
    private val items: List<NotificationItem>
) : RecyclerView.Adapter<NotificationHistoryAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = items[position]

        holder.tvTitle.text = item.title
        holder.tvMessage.text = item.message
        holder.tvTime.text = formatTimestamp(item.timestamp)
    }

    override fun getItemCount(): Int = items.size

    private fun formatTimestamp(ts: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault())
        return sdf.format(Date(ts))
    }
}
