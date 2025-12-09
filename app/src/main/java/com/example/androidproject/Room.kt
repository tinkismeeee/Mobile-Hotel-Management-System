package com.example.androidproject

import com.google.gson.annotations.SerializedName

data class Room(
    @SerializedName("room_id")
    val roomId: Int,

    @SerializedName("room_number")
    val roomNumber: String,

    @SerializedName("room_type_name")
    val roomTypeName: String,

    val floor: Int,

    @SerializedName("price_per_night")
    val pricePerNight: String,

    @SerializedName("max_guests")
    val maxGuests: Int,

    @SerializedName("bed_count")
    val bedCount: Int,

    val description: String?,
)
