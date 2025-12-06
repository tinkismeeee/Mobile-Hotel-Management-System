package com.example.admin

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE


interface ApiService {
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

    @GET("api/customers/{id}")
    fun getCustomer(@retrofit2.http.Path("id") id: Int): Call<User1>

    @POST("api/customers")
    fun addCustomer(@Body customer: NewCustomer): Call<NewCustomer>

    @PUT("api/customers/{id}")
    fun updateCustomer(@retrofit2.http.Path("id") id: Int, @Body updatedCustomer: UpdateCustomerRequest): Call<NewCustomer>

    @DELETE("api/customers/{id}")
    fun deleteCustomer(@retrofit2.http.Path("id") id: Int): Call<Void>


    @GET("api/services")
    fun getAllServices(): Call<List<Service>>

    @GET("api/services/{id}")
    fun getService(@retrofit2.http.Path("id") id: Int): Call<Service>

    @POST("api/services")
    fun addService(@Body newService: NewService): Call<NewService>

    @PUT("api/services/{id}")
    fun updateService(
        @retrofit2.http.Path("id") id: Int,
        @Body updatedService: UpdateServiceRequest
    ): Call<Service>

    @DELETE("api/services/{id}")
    fun deleteService(@retrofit2.http.Path("id") id: Int): Call<Void>

    @GET("api/invoices")
    fun getAllInvoices(): Call<List<Invoice>>


    @GET("api/invoices/{id}")
    fun getInvoice(@retrofit2.http.Path("id") id: Int): Call<Invoice>


    // Lấy tất cả RoomTypes
    @GET("api/room-types")
    fun getAllRoomTypes(): Call<List<RoomType>>

    // Lấy RoomType theo id
    @GET("api/room-types/{id}")
    fun getRoomType(@retrofit2.http.Path("id") id: Int): Call<RoomType>

    // Thêm RoomType mới
    @POST("api/room-types")
    fun addRoomType(@Body newRoomType: NewRoomType): Call<RoomType>

    // Cập nhật RoomType
    @PUT("api/room-types/{id}")
    fun updateRoomType(@retrofit2.http.Path("id") id: Int, @Body updatedRoomType:  UpdateRoomTypeRequest): Call<RoomType>

    // Xóa RoomType
    @DELETE("api/room-types/{id}")
    fun deleteRoomType(@retrofit2.http.Path("id") id: Int): Call<Void>




}

