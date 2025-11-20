package com.example.androidproject

sealed class NotificationItem {
    data class Header(val title: String) : NotificationItem()
    data class Notification(
        val content: String,
        val time: String,
        val iconResId: Int,
        val iconBgResId: Int
    ) : NotificationItem()

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_NOTIFICATION = 1
    }
}