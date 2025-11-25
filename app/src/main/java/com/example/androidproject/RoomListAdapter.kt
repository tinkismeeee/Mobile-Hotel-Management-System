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

class RoomListAdapter(private val roomList: List<RoomResponse>) :
    RecyclerView.Adapter<RoomListAdapter.RoomViewHolder>() {

    /**
     * ViewHolder class: Ánh xạ các view trong layout item
     */
    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.ivHotelImage)
        val name: TextView = itemView.findViewById(R.id.tvHotelName)
        val price: TextView = itemView.findViewById(R.id.tvLocation) // Tận dụng ID cũ (tvLocation) để hiện giá
        val rating: TextView = itemView.findViewById(R.id.tvRating)

        // [QUAN TRỌNG] TextView mới để hiển thị danh sách tiện ích
        // Đảm bảo bạn đã thêm TextView có id=@+id/tvServices trong file XML layout
        val services: TextView = itemView.findViewById(R.id.tvServices)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        // Sử dụng lại layout item cũ (đã được sửa thêm tvServices)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_list_all_recycleview_item, parent, false)
        return RoomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = roomList[position]
        val formatter = DecimalFormat("#,###")

        // 1. Hiển thị Tên phòng
        holder.name.text = "Phòng số: ${room.room_number}"

        // 2. Hiển thị Giá tiền (Format đẹp: 1,000,000)
        holder.price.text = "Giá: ${formatter.format(room.price_per_night)} VND/đêm"

        // 3. Hiển thị Trạng thái
        holder.rating.text = if (room.status == "available") "Trống" else "Đã đặt"

        // 4. [LOGIC MỚI] Hiển thị Tiện ích từ description
        // Dùng description làm danh sách tiện ích. Nếu rỗng thì hiện chữ mặc định.
        val servicesText = if (!room.description.isNullOrEmpty()) {
            "Tiện ích: ${room.description}"
        } else {
            "Tiện ích: Đầy đủ tiện nghi, view đẹp, thoáng mát..."
        }
        holder.services.text = servicesText

        // 5. Hiển thị Ảnh (Hiện tại dùng ảnh mẫu, sau này API có link ảnh thì dùng Glide)
        holder.image.setImageResource(R.drawable.hotel_img)

        // 6. Xử lý sự kiện khi bấm vào phòng
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, RoomDetailActivity::class.java)

            // Truyền dữ liệu sang màn hình Chi tiết
            intent.putExtra("ROOM_ID", room.id)
            intent.putExtra("ROOM_NUMBER", room.room_number)
            intent.putExtra("ROOM_PRICE", room.price_per_night)
            intent.putExtra("ROOM_DESC", room.description)

            context.startActivity(intent)
        }
    }
}