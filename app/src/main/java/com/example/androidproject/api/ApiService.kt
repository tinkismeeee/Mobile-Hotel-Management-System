package com.example.androidproject.api

import com.example.androidproject.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {
    // --- API CŨ (Cho RoomList & Checkout cũ) ---
    @GET("rooms")
    fun getAllRooms(): Call<List<RoomResponse>>

    @POST("invoices/preview")
    fun previewInvoice(@Body request: InvoicePreviewRequest): Call<InvoiceResponse>

    // --- API MỚI (Cho Checkout mới & Booking) ---
    @GET("services")
    fun getServices(): Call<List<ServiceResponse>>

    @GET("bookings")
    fun getBookingHistory(): Call<List<BookingHistoryResponse>>

    @POST("bookings")
    fun createBooking(@Body request: CreateBookingRequest): Call<ResponseBody>
}