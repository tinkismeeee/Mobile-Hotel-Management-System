package com.example.androidproject

data class Promotion(
    val promotion_id: Int,
    val promotion_code: String,
    val name: String,
    val discount_value: String,
    val start_date: String,
    val end_date: String,
    val is_active: Boolean,
    val scope: String,
    val description: String,
    val usage_limit: Int,
    val used_count: Int
)