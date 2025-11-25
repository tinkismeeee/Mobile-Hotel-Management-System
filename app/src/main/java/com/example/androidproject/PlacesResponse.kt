package com.example.androidproject

import com.google.gson.annotations.SerializedName

data class PlacesResponse(
    val results: List<Place>
)

data class Place(
    val name: String?,
    val vicinity: String?, // Địa chỉ
    val rating: Double?,
    @SerializedName("user_ratings_total")
    val userRatingsTotal: Int?,
    val photos: List<Photo>?
)

data class Photo(
    @SerializedName("photo_reference")
    val photoReference: String?
)