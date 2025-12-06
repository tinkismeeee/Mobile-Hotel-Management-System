package com.example.androidproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView

private val roomImageIds = mutableListOf<Int>()

class RoomAdapter(private val roomList: List<Room>) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {
    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomNumber: TextView = itemView.findViewById(R.id.textviewRoomNumber)
        val floor: TextView = itemView.findViewById(R.id.textviewFloor)
        val roomType: TextView = itemView.findViewById(R.id.textviewRoomType)
        val maxGuests: TextView = itemView.findViewById(R.id.textviewMaxGuests)
        val bedCount: TextView = itemView.findViewById(R.id.textviewBedCount)
        val description: TextView = itemView.findViewById(R.id.textviewDescription)
        val roomImage: ImageView = itemView.findViewById(R.id.roomImg)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.room_adapter, parent, false)
        if (roomImageIds.isEmpty()) {
            val context = parent.context
            for (i in 1..35) {
                val imageName = "room_image_$i"
                val resourceId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
                if (resourceId != 0) {
                    roomImageIds.add(resourceId)
                }
            }
        }
        return RoomViewHolder(view)
    }
    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val currentRoom = roomList[position]
        holder.roomNumber.text = "Room number: ${currentRoom.roomNumber}"
        holder.floor.text = "Floor ${currentRoom.floor}"
        holder.roomType.text = currentRoom.roomTypeName
        holder.maxGuests.text = "Max guests: ${currentRoom.maxGuests}"
        holder.bedCount.text = "Bed count: ${currentRoom.bedCount}"
        holder.description.text = "Description: ${currentRoom.description ?: "N/A"}"
        if (roomImageIds.isNotEmpty()) {
            val randomImageId = roomImageIds.random()
            holder.roomImage.setImageResource(randomImageId)
        }
    }
    override fun getItemCount(): Int {
        return roomList.size
    }
}
