package com.example.androidproject.models

// Dữ liệu gửi đi để tính tiền
data class InvoicePreviewRequest(
    val bookingId: Int?, // Có thể null nếu chưa book, nhưng API yêu cầu logic nào đó
    val roomIds: List<Int>, // Danh sách ID phòng muốn book
    val checkIn: String,
    val checkOut: String,
    val promoCode: String? = null
)

// Dữ liệu nhận về (Đúng theo mẫu bạn gửi)
data class InvoiceResponse(
    val totalRoomCost: Double,
    val totalServiceCost: Double,
    val discountAmount: Double,
    val vatAmount: Double,
    val finalTotal: Double,
    val nights: Int
)