package com.example.androidproject

data class Room(
    val id: String,
    val name: String,
    val capacity: Int,
    val price: Double,
    val description: String
)

// 2. Kho chứa dữ liệu
object RoomRepository {
    // Danh sách phòng giả lập
    private val allRooms = listOf(
        Room("R001", "Phòng Đơn Tiêu Chuẩn", 1, 300000.0, "Giường đơn, cửa sổ thoáng mát."),
        Room("R002", "Phòng Đôi Nhìn Biển", 2, 500000.0, "View biển, ban công rộng, lãng mạn."),
        Room("R003", "Phòng Gia Đình (Family)", 4, 900000.0, "2 giường đôi lớn, có bếp nhỏ."),
        Room("R004", "Phòng VIP King", 2, 1500000.0, "Bồn tắm Jacuzzi, view toàn cảnh thành phố."),
        Room("R005", "Phòng Tập Thể (Dorm)", 6, 1200000.0, "Giường tầng, rộng rãi, phù hợp nhóm bạn.")
    )

    // Hàm lọc phòng (Logic tìm kiếm)
    fun findRooms(guests: Int, maxPrice: Double): List<Room> {
        return allRooms.filter { room ->
            // Điều kiện 1: Sức chứa phải đủ hoặc dư một chút
            val capacityMatch = room.capacity >= guests

            // Điều kiện 2: Giá phải nhỏ hơn ngân sách (Nếu khách không nói giá thì maxPrice = -1 -> bỏ qua)
            val priceMatch = if (maxPrice > 0) room.price <= maxPrice else true

            capacityMatch && priceMatch
        }
    }

    fun getRoomById(id: String): Room? {
        return allRooms.find { it.id == id }
    }
}