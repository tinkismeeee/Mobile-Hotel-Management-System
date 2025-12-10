package com.example.androidproject

import kotlin.collections.filter
import kotlin.collections.find

data class Room1(
    val id: String,
    val name: String,
    val capacity: Int,
    val price: Double,
    val description: String
)

object RoomRepository {
    private val allRooms = listOf(
        Room1("1", "Family 101", 1, 380000.0, "Phòng 1 người, gọn gàng."),
        Room1("2", "Standard 102", 2, 820000.0, "Phòng tiêu chuẩn 2 người."),
        Room1("3", "Suite 103", 3, 910000.0, "Suite rộng cho 3 người."),
        Room1("4", "Deluxe 104", 4, 750000.0, "Deluxe thoải mái, sức chứa 4 người."),
        Room1("5", "Deluxe 105", 2, 1050000.0, "Phòng Deluxe hiện đại."),
        Room1("6", "Suite 106", 1, 420000.0, "Phòng 1 người giá rẻ."),
        Room1("7", "Suite 107", 4, 1080000.0, "Suite lớn cho nhóm 4 người."),
        Room1("8", "Standard 108", 3, 950000.0, "Tiêu chuẩn phù hợp 3 người."),
        Room1("9", "Business 109", 2, 530000.0, "Phòng Business hợp lý cho 2 người."),
        Room1("10", "Business 110", 1, 670000.0, "Phòng đơn business."),
        Room1("11", "Family 111", 2, 780000.0, "Phòng gia đình nhỏ."),
        Room1("12", "Deluxe 112", 3, 1220000.0, "Deluxe 3 người sang trọng."),
        Room1("13", "Suite 113", 1, 590000.0, "Suite đơn hiện đại."),
        Room1("14", "Business 114", 2, 1150000.0, "Business 2 người cao cấp."),
        Room1("15", "Family 115", 4, 650000.0, "Phòng lớn cho gia đình."),
        Room1("16", "Business 116", 1, 490000.0, "Phòng 1 người, tiết kiệm."),
        Room1("17", "Standard 117", 3, 1120000.0, "Tiêu chuẩn 3 người."),
        Room1("18", "Deluxe 118", 4, 1250000.0, "Deluxe lớn cho nhóm 4 người."),
        Room1("19", "Suite 119", 2, 710000.0, "Suite tiện nghi."),
        Room1("20", "Deluxe 120", 3, 1280000.0, "Deluxe 3 người sang chảnh."),
        Room1("21", "Suite 121", 4, 900000.0, "Suite hợp lý cho 4 người."),
        Room1("22", "Deluxe 122", 3, 1020000.0, "Deluxe cao cấp 3 người."),
        Room1("23", "Family 123", 4, 830000.0, "Phòng gia đình 4 người."),
        Room1("24", "Standard 124", 2, 1490000.0, "Phòng tiêu chuẩn cao cấp 2 người."),
        Room1("25", "Deluxe 125", 4, 550000.0, "Deluxe 4 người giá tiết kiệm."),
        Room1("26", "Standard 126", 5, 1270000.0, "Phòng lớn chứa tối đa 5 người."),
        Room1("27", "Deluxe 127", 3, 860000.0, "Deluxe thoáng, hợp 3 người."),
        Room1("28", "Family 128", 4, 440000.0, "Phòng gia đình giá rẻ."),
        Room1("29", "Standard 129", 3, 820000.0, "Tiêu chuẩn 3 người."),
        Room1("30", "Business 130", 4, 1430000.0, "Business 4 người cao cấp.")
    )

    fun findRooms(guests: Int, maxPrice: Double): List<Room1> {
        val filtered = allRooms.filter { room ->
            room.capacity >= guests &&
                    (maxPrice < 0 || room.price <= maxPrice)
        }
        val minCapacity = filtered.minOfOrNull { it.capacity } ?: return emptyList()
        return filtered.filter { it.capacity == minCapacity }
    }

    fun getRoomById(id: String): Room1? {
        return allRooms.find { it.id == id }
    }
    fun getLowestPriceRoom(): Room1? {
        return allRooms.minByOrNull { it.price }
    }

    fun getHighestPriceRoom(): Room1? {
        return allRooms.maxByOrNull { it.price }
    }
}