package com.example.androidproject.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RoomResponse(
    // Ánh xạ trường "room_id" từ API vào biến "id"
    @SerializedName("room_id")
    val id: Int,

    // Ánh xạ trường "room_number"
    @SerializedName("room_number")
    val room_number: String,

    // Ánh xạ giá tiền, Gson sẽ tự ép kiểu String/Number sang Double
    @SerializedName("price_per_night")
    val price_per_night: Double,

    // Mô tả phòng (có thể null)
    val description: String?,

    // Trạng thái: "available", "booked", "maintenance"
    val status: String,

    // Ánh xạ loại phòng từ trường "room_type_name" của API
    @SerializedName("room_type_name")
    val type: String? = "Standard",

    // Các thông số chi tiết (API có trả về)
    val floor: Int? = 1,

    @SerializedName("max_guests")
    val max_guests: Int? = 2,

    @SerializedName("bed_count")
    val bed_count: Int? = 1,

    // [QUAN TRỌNG] Biến này dùng để lưu danh sách dịch vụ được phép (cho logic lọc).
    // Vì API chưa trả về trường này, ta để nó là Nullable (?) và mặc định là null.
    // Adapter sẽ tự động điền dữ liệu giả vào đây nếu nó null.
    val allowedServiceCodes: ArrayList<String>? = null

) : Serializable