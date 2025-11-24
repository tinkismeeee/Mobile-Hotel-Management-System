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
}

