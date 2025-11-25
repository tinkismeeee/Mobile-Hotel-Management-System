package com.example.androidproject.api

import com.example.androidproject.models.InvoicePreviewRequest
import com.example.androidproject.models.InvoiceResponse
import com.example.androidproject.models.RoomResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    // Lấy danh sách phòng (tương ứng với danh sách khách sạn ở Home)
    @GET("rooms")
    fun getAllRooms(): Call<List<RoomResponse>>

    // Tính toán hóa đơn (Preview Invoice)
    @POST("invoices/preview")
    fun previewInvoice(@Body request: InvoicePreviewRequest): Call<InvoiceResponse>
}