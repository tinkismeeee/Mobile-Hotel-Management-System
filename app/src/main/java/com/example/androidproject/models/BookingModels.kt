package com.example.androidproject.models

import com.google.gson.annotations.SerializedName

// 1. Model cho Dịch vụ (Đã sửa khớp với API 217.216.72.223)
data class ServiceResponse(
    // Dùng @SerializedName để ánh xạ chính xác tên trường từ JSON
    @SerializedName("service_id")
    val id: Int,

    @SerializedName("service_code")
    val serviceCode: String, // VD: "SV001"

    val name: String,        // VD: "Laundry"
    val price: Double,       // Gson tự chuyển chuỗi "50000.00" thành Double
    val availability: Boolean,
    val description: String?,

    var quantity: Int = 0    // Biến đếm số lượng (Client tự thêm)
)

// 2. Model phụ cho danh sách dịch vụ gửi đi (POST)
data class BookingServiceItem(
    val serviceCode: String,
    val quantity: Int
)

// 3. Model trọn vẹn cho Request đặt phòng (POST)
data class CreateBookingRequest(
    val user_id: Int,
    val room_ids: List<Int>,
    val check_in: String,
    val check_out: String,
    val total_guests: Int,
    val services: List<BookingServiceItem>,
    val promotionCode: String?
)

// 4. Model cho Lịch sử đặt phòng (Giữ nguyên)
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
)