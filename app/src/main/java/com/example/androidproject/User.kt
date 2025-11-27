package com.example.androidproject

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


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
    val email: String,
    val first_name: String?,
    val last_name: String?,
    val phone_number: String?,
    val address: String?,
    val date_of_birth: String?,
    val is_active: Boolean?
)

