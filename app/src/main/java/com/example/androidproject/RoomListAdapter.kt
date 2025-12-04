package com.example.androidproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.models.RoomResponse
import java.text.DecimalFormat
import java.util.ArrayList

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

        holder.name.text = "Phòng ${room.room_number} - ${room.type ?: "Standard"}"
        val priceVal = room.price_per_night ?: 0.0
        holder.price.text = "Giá: ${formatter.format(priceVal)} VND/đêm"
        holder.rating.text = if (room.status == "available") "Trống" else room.status

        // Hiển thị thông số thật từ API
        val info = StringBuilder()
        if (room.floor != null) info.append("• Tầng: ${room.floor}\n")
        if (room.max_guests != null) info.append("• Tối đa: ${room.max_guests} khách\n")
        if (room.bed_count != null) info.append("• Giường: ${room.bed_count}")

        holder.services.text = if (info.isNotEmpty()) info.toString() else "• Đang cập nhật thông tin"
        holder.image.setImageResource(R.drawable.hotel_img)

        // Logic gán dịch vụ (Client-side logic) dựa trên loại phòng thật
        val generatedServiceCodes = when(room.type) {
            "Suite", "Family", "Deluxe" -> arrayListOf("SV001", "SV002", "SV003", "SV004", "SV005")
            "Business" -> arrayListOf("SV001", "SV002", "SV006")
            else -> arrayListOf("SV001", "SV002")
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, RoomDetailActivity::class.java)

            intent.putExtra("ROOM_ID", room.id)
            intent.putExtra("ROOM_NUMBER", room.room_number)
            intent.putExtra("ROOM_TYPE", room.type)
            intent.putExtra("ROOM_PRICE", priceVal)
            intent.putExtra("ROOM_DESC", room.description)
            intent.putExtra("ROOM_GUESTS", room.max_guests)
            intent.putExtra("ROOM_BEDS", room.bed_count)
            intent.putExtra("ROOM_FLOOR", room.floor)

            intent.putStringArrayListExtra("ALLOWED_SERVICES", generatedServiceCodes)

            context.startActivity(intent)
        }
    }
}