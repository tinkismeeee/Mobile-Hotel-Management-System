package com.example.androidproject

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Query


interface ApiService {
    @GET("api/rooms")
    fun getAllRooms(): Call<List<Room>>
    @GET("api/rooms/{id}")
    fun getRoom(@retrofit2.http.Path("id") id: Int): Call<Room>
    @POST("api/rooms")
    fun addRoom(@Body room: Room): Call<Room>
    @PUT("api/rooms/{id}")
    fun updateRoom(@retrofit2.http.Path("id") id: Int, @Body updatedRoom: Room): Call<Room>
    @DELETE("api/rooms/{id}")
    fun deleteRoom(@retrofit2.http.Path("id") id: Int): Call<Void>
    
    @GET("api/staff")
    fun getAllStaff(): Call<List<User>>
    @GET("api/staff/{id}")
    fun getUser(@retrofit2.http.Path("id") id: Int): Call<User>

    @POST("api/staff")
    fun addStaff(@Body user: NewUser): Call<User>

    @PUT("api/staff/{id}")
    fun updateStaff(@retrofit2.http.Path("id") id: Int, @Body updatedUser: UpdateUserRequest): Call<User>

    @DELETE("api/staff/{id}")
    fun deleteStaff(@retrofit2.http.Path("id") id: Int): Call<Void>
    @GET("api/customers")
    fun getAllCustomers(): Call<List<User1>>

    @GET("api/customers")
    fun getCustomerByEmail(@Query("email") email: String): Call<User1>

    @GET("api/customers/{id}")
    fun getCustomer(@retrofit2.http.Path("id") id: Int): Call<User1>

    @POST("api/customers")
    fun addCustomer(@Body customer: NewCustomer): Call<NewCustomer>

    @PUT("api/customers/{id}")
    fun updateCustomer(@retrofit2.http.Path("id") id: Int, @Body updatedCustomer: UpdateCustomerRequest): Call<NewCustomer>

    @DELETE("api/customers/{id}")
    fun deleteCustomer(@retrofit2.http.Path("id") id: Int): Call<Void>

    // Promotion
    @GET("api/promotions")
    fun getAllPromotions(): Call<List<Promotion>>



}

