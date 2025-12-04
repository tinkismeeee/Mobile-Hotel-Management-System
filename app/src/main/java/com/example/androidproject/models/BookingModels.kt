package com.example.androidproject.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

// Model Dịch vụ nhận từ API
data class ServiceResponse(
    @SerializedName("service_id") val id: Int,
    @SerializedName("service_code") val serviceCode: String,
    val name: String,
    val price: Double,
    val availability: Boolean,
    val description: String?,
    var quantity: Int = 0
) : Serializable

// Model Dịch vụ gửi lên Server (Rút gọn)
data class BookingServiceItem(
    val serviceCode: String,
    val quantity: Int
) : Serializable

// Model Tạo Đặt phòng (Gửi lên Server)
data class CreateBookingRequest(
    val user_id: Int,
    val room_ids: List<Int>,
    val check_in: String,
    val check_out: String,
    val total_guests: Int,
    val services: List<BookingServiceItem>,
    val promotionCode: String?
) : Serializable

// Model Lịch sử (Nhận từ Server)
data class BookingHistoryResponse(
    val booking_id: Int,
    val user_id: Int,
    val booking_date: String,
    val check_in: String,
    val check_out: String,
    val status: String,
    val total_guests: Int,
    val username: String,
    val hotel_name: String? = "Khách sạn Demo"
) : Serializable