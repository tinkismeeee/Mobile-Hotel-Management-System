package com.example.androidproject.models

import java.io.Serializable

data class RoomResponse(
    val id: Int,
    val room_number: String,
    val price_per_night: Double,
    val description: String, // Chúng ta sẽ dùng dòng này để hiển thị tiện ích
    val status: String,
    val type: String? = "Standard" // Giả sử có thêm loại phòng
) : Serializable