package com.example.androidproject

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // -------------------------
    // ROOMS
    // -------------------------
    @GET("api/rooms")
    fun getAllRooms(): Call<List<Room>>

    @GET("api/rooms/{id}")
    fun getRoom(@Path("id") id: Int): Call<Room>

    @POST("api/rooms")
    fun addRoom(@Body room: Room): Call<Room>

    @PUT("api/rooms/{id}")
    fun updateRoom(@Path("id") id: Int, @Body updatedRoom: Room): Call<Room>

    @DELETE("api/rooms/{id}")
    fun deleteRoom(@Path("id") id: Int): Call<Void>



    // -------------------------
    // STAFF
    // -------------------------
    @GET("api/staff")
    fun getAllStaff(): Call<List<User>>

    @GET("api/staff/{id}")
    fun getUser(@Path("id") id: Int): Call<User>

    @POST("api/staff")
    fun addStaff(@Body user: NewUser): Call<User>

    @PUT("api/staff/{id}")
    fun updateStaff(@Path("id") id: Int, @Body updatedUser: UpdateUserRequest): Call<User>

    @DELETE("api/staff/{id}")
    fun deleteStaff(@Path("id") id: Int): Call<Void>



    // -------------------------
    // CUSTOMERS
    // -------------------------
    @GET("api/customers")
    fun getAllCustomers(): Call<List<User1>>

    @GET("api/customers/search")
    fun getCustomerByEmail(@Query("email") email: String): Call<User1>

    @GET("api/customers/{id}")
    fun getCustomer(@Path("id") id: Int): Call<User1>

    @POST("api/customers")
    fun addCustomer(@Body customer: NewCustomer): Call<NewCustomer>

    @PUT("api/customers/{id}")
    fun updateCustomer(@Path("id") id: Int, @Body updatedCustomer: UpdateCustomerRequest): Call<NewCustomer>

    @DELETE("api/customers/{id}")
    fun deleteCustomer(@Path("id") id: Int): Call<Void>



    // -------------------------
    // SERVICES
    // -------------------------
    @GET("api/services")
    fun getAllServices(): Call<List<Service>>

    @GET("api/services/{id}")
    fun getService(@Path("id") id: Int): Call<Service>

    @POST("api/services")
    fun addService(@Body newService: NewService): Call<NewService>

    @PUT("api/services/{id}")
    fun updateService(@Path("id") id: Int, @Body updatedService: UpdateServiceRequest): Call<Service>

    @DELETE("api/services/{id}")
    fun deleteService(@Path("id") id: Int): Call<Void>



    // -------------------------
    // INVOICES
    // -------------------------
    @GET("api/invoices")
    fun getAllInvoices(): Call<List<Invoice>>

    @GET("api/invoices/{id}")
    fun getInvoice(@Path("id") id: Int): Call<Invoice>



    // -------------------------
    // ROOM TYPES
    // -------------------------
    @GET("api/room-types")
    fun getAllRoomTypes(): Call<List<RoomType>>

    @GET("api/room-types/{id}")
    fun getRoomType(@Path("id") id: Int): Call<RoomType>

    @POST("api/room-types")
    fun addRoomType(@Body newRoomType: NewRoomType): Call<RoomType>

    @PUT("api/room-types/{id}")
    fun updateRoomType(@Path("id") id: Int, @Body updatedRoomType: UpdateRoomTypeRequest): Call<RoomType>

    @DELETE("api/room-types/{id}")
    fun deleteRoomType(@Path("id") id: Int): Call<Void>



    // -------------------------
    // PROMOTIONS
    // -------------------------
    @GET("api/promotions")
    fun getAllPromotions(): Call<List<Promotion>>
}
