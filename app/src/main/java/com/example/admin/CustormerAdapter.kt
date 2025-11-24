package com.example.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustormerAdapter(
    private val context: Context,
    private val userList: List<User1>,
    private val onItemClick: ((User1) -> Unit)? = null
) : RecyclerView.Adapter<CustormerAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUserId: TextView = view.findViewById(R.id.tvUserId)
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
        val tvEmail: TextView = view.findViewById(R.id.tvEmail)
        val tvFullName: TextView = view.findViewById(R.id.tvFullName)
        val tvPhone: TextView = view.findViewById(R.id.tvPhone)
        val tvAddress: TextView = view.findViewById(R.id.tvAddress)
        val tvDob: TextView = view.findViewById(R.id.tvDob)
        val tvActive: TextView = view.findViewById(R.id.tvActive)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_custormer_adapter, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.tvUserId.text = "ID: ${user.user_id}"
        holder.tvUsername.text = "Username: ${user.username}"
        holder.tvEmail.text = "Email: ${user.email}"
        holder.tvFullName.text = "Full Name: ${user.fullName}"
        holder.tvPhone.text = "Phone: ${user.phone_number ?: "N/A"}"
        holder.tvAddress.text = "Address: ${user.address ?: "N/A"}"
        holder.tvDob.text = "DOB: ${user.date_of_birth ?: "N/A"}"
        holder.tvActive.text = "Active: ${user.is_active}"

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(user)
        }
    }

    override fun getItemCount(): Int = userList.size
}
