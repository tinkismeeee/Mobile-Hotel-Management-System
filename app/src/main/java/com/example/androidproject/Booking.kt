package com.example.androidproject

// Dùng để định nghĩa cấu trúc dữ liệu
data class Booking(
    val hotelId: String? = null,
    val hotelName: String? = null,
    val hotelImage: String? = null,
    val rating: Double? = null,
    val location: String? = null,
    val pricePerNight: Int? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val guests: Int? = null,
    val rooms: Int? = null,
    val status: String? = null
)