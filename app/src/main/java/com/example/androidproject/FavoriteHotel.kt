package com.example.androidproject

// Giả sử bạn có sẵn resource ID cho ảnh
data class FavoriteHotel(
    val name: String,
    val rating: Double,
    val location: String,
    val pricePerNight: Int,
    val imageResId: Int,
    val isFavorite: Boolean = true
)