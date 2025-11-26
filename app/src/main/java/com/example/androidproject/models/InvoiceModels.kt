package com.example.androidproject.models

data class InvoicePreviewRequest(
    val bookingId: Int?,
    val roomIds: List<Int>,
    val checkIn: String,
    val checkOut: String,
    val promoCode: String? = null
)

data class InvoiceResponse(
    val totalRoomCost: Double,
    val totalServiceCost: Double,
    val discountAmount: Double,
    val vatAmount: Double,
    val finalTotal: Double,
    val nights: Int
)