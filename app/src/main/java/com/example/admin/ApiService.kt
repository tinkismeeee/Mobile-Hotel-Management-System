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
    fun updateCustomer(@retrofit2.http.Path("id") id: Int, @Body updatedCustomer: User1): Call<User1>

    @DELETE("api/customers/{id}")
    fun deleteCustomer(@retrofit2.http.Path("id") id: Int): Call<Void>

}

