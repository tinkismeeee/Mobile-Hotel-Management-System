package com.example.androidproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RoomTypeAdapter(
    private val roomList: MutableList<RoomType>,
    private val onItemClick: (RoomType) -> Unit
) : RecyclerView.Adapter<RoomTypeAdapter.RoomViewHolder>() {

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRoomId: TextView = itemView.findViewById(R.id.tvRoomId)
        val tvRoomName: TextView = itemView.findViewById(R.id.tvRoomName)
        val tvRoomDescription: TextView = itemView.findViewById(R.id.tvRoomDescription)
        val tvRoomStatus: TextView = itemView.findViewById(R.id.tvRoomStatus)

        init {
            itemView.setOnClickListener {
                onItemClick(roomList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_loaiphongadapter, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = roomList[position]
        holder.tvRoomId.text = "ID: ${room.room_type_id}"
        holder.tvRoomName.text = room.name
        holder.tvRoomDescription.text = room.description
        holder.tvRoomStatus.text = "Status: ${if (room.is_active) "True" else "Inactive"}"
    }

    override fun getItemCount(): Int = roomList.size

    fun updateData(newList: List<RoomType>) {
        roomList.clear()
        roomList.addAll(newList)
        notifyDataSetChanged()
    }
}
