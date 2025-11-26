package com.example.androidproject

// Cập nhật đầy đủ các trường để tương thích với Home Fragment mới
data class villas_and_hotels_list_item(
    val hotelId: String? = null,
    val hotelName: String? = null,
    val hotelImage: String? = null,
    val rating: Double? = null,
    val location: String? = null,

    // Các trường mới bắt buộc phải có:
    val pricePerNight: Int? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val guests: Int? = null,
    val rooms: Int? = null,
    val status: String? = null,
    val photoReference: String? = null
)