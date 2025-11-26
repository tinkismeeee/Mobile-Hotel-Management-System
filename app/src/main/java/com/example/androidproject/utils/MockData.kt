package com.example.androidproject.utils

import com.example.androidproject.models.RoomResponse
import com.example.androidproject.models.ServiceResponse
import com.example.androidproject.villas_and_hotels_list_item

object MockData {
    // 1. Dữ liệu Khách sạn giả (Cho Home)
    fun getMockHotels(): List<villas_and_hotels_list_item> {
        return listOf(
            villas_and_hotels_list_item(hotelName = "The Aston Vill Hotel (Mock)", rating = 4.8, location = "Alice Springs, Australia", pricePerNight = 2400000, startDate = "12/11", endDate = "14/11", guests = 2, rooms = 1, status = "available"),
            villas_and_hotels_list_item(hotelName = "Golden Bay Resort (Mock)", rating = 5.0, location = "Da Nang, Vietnam", pricePerNight = 3500000, startDate = "Today", endDate = "Tomorrow", guests = 4, rooms = 2, status = "available"),
            villas_and_hotels_list_item(hotelName = "Sapa Mountain View (Mock)", rating = 4.5, location = "Lao Cai, Vietnam", pricePerNight = 1200000, startDate = "01/01", endDate = "03/01", guests = 2, rooms = 1, status = "available"),
            villas_and_hotels_list_item(hotelName = "Sunrise Ocean Villa (Mock)", rating = 4.7, location = "Phu Quoc, Vietnam", pricePerNight = 5000000, startDate = "10/02", endDate = "15/02", guests = 6, rooms = 3, status = "available"),
            villas_and_hotels_list_item(hotelName = "Grand Plaza Hanoi (Mock)", rating = 4.2, location = "Hanoi, Vietnam", pricePerNight = 1800000, startDate = "05/03", endDate = "07/03", guests = 2, rooms = 1, status = "available")
        )
    }

    // 2. Dữ liệu Phòng giả (Cấu hình dịch vụ riêng cho từng phòng)
    fun getMockRooms(): List<RoomResponse> {
        return listOf(
            // [SỬA LỖI] Dùng Named Arguments để tránh sai thứ tự biến
            RoomResponse(
                id = 101,
                room_number = "101",
                price_per_night = 500000.0,
                description = "Wifi, Máy lạnh, Tủ lạnh mini",
                status = "available",
                type = "Standard",
                floor = 1,
                max_guests = 2,
                bed_count = 1,
                allowedServiceCodes = arrayListOf("SV001", "SV002")
            ),

            RoomResponse(
                id = 201,
                room_number = "201",
                price_per_night = 1500000.0,
                description = "View biển, Ban công rộng, King Bed",
                status = "available",
                type = "VIP",
                floor = 2,
                max_guests = 2,
                bed_count = 1,
                allowedServiceCodes = arrayListOf("SV001", "SV002", "SV003", "SV004", "SV006")
            ),

            RoomResponse(
                id = 302,
                room_number = "302",
                price_per_night = 3000000.0,
                description = "Bếp riêng, Hồ bơi, Phòng khách lớn",
                status = "available",
                type = "Suite",
                floor = 3,
                max_guests = 4,
                bed_count = 2,
                allowedServiceCodes = arrayListOf("SV001", "SV002", "SV003", "SV004", "SV005", "SV006")
            ),

            RoomResponse(
                id = 401,
                room_number = "401",
                price_per_night = 400000.0,
                description = "Cửa sổ nhỏ, Tiện nghi cơ bản",
                status = "available",
                type = "Economy",
                floor = 4,
                max_guests = 1,
                bed_count = 1,
                allowedServiceCodes = arrayListOf("SV001")
            )
        )
    }

    // 3. Danh sách Dịch vụ giả (Dùng để tham chiếu tên và giá)
    fun getMockServices(): List<ServiceResponse> {
        return listOf(
            ServiceResponse(1, "SV001", "Laundry", 50000.0, true, "Giặt là"),
            ServiceResponse(2, "SV002", "Breakfast", 100000.0, true, "Buffet sáng"),
            ServiceResponse(3, "SV003", "Airport Pickup", 200000.0, true, "Xe đưa đón"),
            ServiceResponse(4, "SV004", "Spa", 300000.0, true, "Thư giãn"),
            ServiceResponse(5, "SV005", "Dinner", 250000.0, true, "Bữa tối"),
            ServiceResponse(6, "SV006", "Mini Bar", 150000.0, true, "Đồ uống")
        )
    }
}