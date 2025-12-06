package com.example.admin

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// 1️⃣ Class dùng cho GET (hoặc response của POST)
@Parcelize
data class User(
    val user_id: Int,
    val username: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val phone_number: String?,
    val is_active: Boolean = true
) : Parcelable {

    val fullName: String
        get() = "$first_name $last_name"

    val usernameAndId: String
        get() = "User: $username | ID: $user_id"
}

// 2️⃣ Class dùng cho POST (tạo nhân viên mới)
data class NewUser(
    val username: String,
    val password: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val phone_number: String?
)

data class UpdateUserRequest(
    val email: String,
    val first_name: String,
    val last_name: String,
    val phone_number: String?,
    val is_active: Boolean
)


data class User1(
    val user_id: Int,
    val username: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val phone_number: String?,
    val address: String?,
    val date_of_birth: String?,
    val is_active: Boolean
) {
    val fullName: String
        get() = "$first_name $last_name"
}
data class NewCustomer(
    val username: String,
    val password: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val phone_number: String?,
    val address: String?,
    val date_of_birth: String?,
    val is_active: Boolean
)
data class UpdateCustomerRequest(
    val email: String,        // Bắt buộc
    val first_name: String?,
    val last_name: String?,
    val phone_number: String?,
    val address: String?,
    val date_of_birth: String?,
    val is_active: Boolean?
)

data class Service(
    val service_id: Int,
    val service_code: String,
    val name: String,
    val price: String,
    val availability: Boolean,
    val description: String
)

data class NewService(
    val service_code: String,
    val name: String,
    val price: String,
    val availability: Boolean,
    val description: String
)

data class UpdateServiceRequest(
    val service_code: String,
    val name: String,
    val price: String,
    val availability: Boolean,
    val description: String
)

data class Invoice(
    val invoice_id: Int,
    val booking_id: Int,
    val staff_id: Int,
    val issue_date: String,
    val total_room_cost: String,
    val total_service_cost: String,
    val discount_amount: String,
    val final_amount: String,
    val vat_amount: String,
    val payment_method: String,
    val promotion_id: Int?,
    val payment_status: String,
    val total_amount: String?,
    val tax_amount: String?
)
data class RoomType(
    val room_type_id: Int,
    val name: String,
    val description: String,
    val is_active: Boolean
)


data class NewRoomType(
    val name: String,
    val description: String,
    val is_active: Boolean
)

data class UpdateRoomTypeRequest(
    val name: String,
    val description: String,
    val is_active: Boolean
)
