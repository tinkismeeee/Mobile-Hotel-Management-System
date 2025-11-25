package com.example.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustormerAdapter(
    // 1. Dùng MutableList
    private val userList: MutableList<User1>,
    // 2. Dùng callback bắt buộc
    private val onItemClick: (User1) -> Unit
) : RecyclerView.Adapter<CustormerAdapter.CustomerViewHolder>() { // Đổi tên ViewHolder cho rõ ràng

    inner class CustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Ánh xạ các view (giữ nguyên ID từ code gốc của bạn)
        val tvUserId: TextView = itemView.findViewById(R.id.tvUserId)
        val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val tvFullName: TextView = itemView.findViewById(R.id.tvFullName)
        val tvPhone: TextView = itemView.findViewById(R.id.tvPhone)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val tvDob: TextView = itemView.findViewById(R.id.tvDob)
        val tvActive: TextView = itemView.findViewById(R.id.tvActive)

        // 3. Di chuyển logic click vào khối init
        init {
            itemView.setOnClickListener {
                // Kiểm tra adapterPosition để tránh crash nếu item bị xóa
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick(userList[adapterPosition])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        // 1. Lấy context từ parent
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_custormer_adapter, parent, false)
        return CustomerViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val user = userList[position]

        // Tối ưu hóa hiển thị chuỗi (giống UserAdapter)
        holder.tvUserId.text = "ID: ${user.user_id}"
        holder.tvUsername.text = "Username: ${user.username}"
        holder.tvEmail.text = "Email: ${user.email}"
        // Giả định User1 có thuộc tính fullName, hoặc dùng kết hợp first_name/last_name
        holder.tvFullName.text = user.fullName
        holder.tvPhone.text = "Phone: ${user.phone_number ?: "N/A"}"
        holder.tvAddress.text = "Address: ${user.address ?: "N/A"}"
        // Cần xử lý định dạng ngày tháng nếu nó chứa thời gian
        holder.tvDob.text = "DOB: ${user.date_of_birth?.substringBefore("T") ?: "N/A"}"
        holder.tvActive.text = "Active: ${if(user.is_active) "true" else "false"}"
    }

    override fun getItemCount(): Int = userList.size

    // 4. Thêm hàm updateData
    fun updateData(newList: List<User1>) {
        userList.clear()
        userList.addAll(newList)
        notifyDataSetChanged()
    }
}