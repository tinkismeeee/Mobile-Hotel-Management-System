package com.example.androidproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.models.RoomResponse
import com.example.androidproject.utils.MockData
import java.text.DecimalFormat

class RoomListAdapter(private val roomList: List<RoomResponse>) :
    RecyclerView.Adapter<RoomListAdapter.RoomViewHolder>() {

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.ivHotelImage)
        val name: TextView = itemView.findViewById(R.id.tvHotelName)
        val price: TextView = itemView.findViewById(R.id.tvLocation)
        val services: TextView = itemView.findViewById(R.id.tvServices)
        val rating: TextView = itemView.findViewById(R.id.tvRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_list_all_recycleview_item, parent, false)
        return RoomViewHolder(view)
    }

    override fun getItemCount() = roomList.size

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = roomList[position]
        val formatter = DecimalFormat("#,###")

        // Gán thông tin cơ bản
        holder.name.text = "Phòng ${room.room_number} - ${room.type ?: "Standard"}"
        holder.price.text = "Giá: ${formatter.format(room.price_per_night)} VND/đêm"
        holder.rating.text = room.status

        // Xử lý danh sách dịch vụ (An toàn)
        val safeAllowedCodes = room.allowedServiceCodes ?: arrayListOf("SV001", "SV002")
        val allServices = MockData.getMockServices()

        val serviceNames = safeAllowedCodes.mapNotNull { code ->
            allServices.find { it.serviceCode == code }?.name
        }

        if (serviceNames.isNotEmpty()) {
            holder.services.text = serviceNames.joinToString(separator = "\n", prefix = "• ")
        } else {
            holder.services.text = "• Tiện ích cơ bản"
        }

        holder.image.setImageResource(R.drawable.hotel_img)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, RoomDetailActivity::class.java)

            // Truyền các dữ liệu cơ bản
            intent.putExtra("ROOM_ID", room.id)
            intent.putExtra("ROOM_NUMBER", room.room_number)
            intent.putExtra("ROOM_TYPE", room.type)
            intent.putExtra("ROOM_PRICE", room.price_per_night)
            intent.putExtra("ROOM_DESC", room.description)
            intent.putExtra("ROOM_GUESTS", room.max_guests)
            intent.putExtra("ROOM_BEDS", room.bed_count)
            intent.putExtra("ROOM_FLOOR", room.floor)

            // [SỬA LỖI VĂNG APP TẠI ĐÂY]
            // Ép kiểu về ArrayList một cách tường minh trước khi gửi
            // Điều này ngăn chặn lỗi "ClassCastException" ngầm
            val listToSend = ArrayList(safeAllowedCodes)
            intent.putStringArrayListExtra("ALLOWED_SERVICES", listToSend)

            context.startActivity(intent)
        }
    }
}