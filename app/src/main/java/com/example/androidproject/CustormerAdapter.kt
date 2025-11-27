package com.example.androidproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustormerAdapter(

    private val userList: MutableList<User1>,

    private val onItemClick: (User1) -> Unit
) : RecyclerView.Adapter<CustormerAdapter.CustomerViewHolder>() {

    inner class CustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvUserId: TextView = itemView.findViewById(R.id.tvUserId)
        val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val tvFullName: TextView = itemView.findViewById(R.id.tvFullName)
        val tvPhone: TextView = itemView.findViewById(R.id.tvPhone)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val tvDob: TextView = itemView.findViewById(R.id.tvDob)
        val tvActive: TextView = itemView.findViewById(R.id.tvActive)


        init {
            itemView.setOnClickListener {

                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick(userList[adapterPosition])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_custormer_adapter, parent, false)
        return CustomerViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val user = userList[position]


        holder.tvUserId.text = "ID: ${user.user_id}"
        holder.tvUsername.text = "Username: ${user.username}"
        holder.tvEmail.text = "Email: ${user.email}"

        holder.tvFullName.text = user.fullName
        holder.tvPhone.text = "Phone: ${user.phone_number ?: "N/A"}"
        holder.tvAddress.text = "Address: ${user.address ?: "N/A"}"

        holder.tvDob.text = "DOB: ${user.date_of_birth?.substringBefore("T") ?: "N/A"}"
        holder.tvActive.text = "Active: ${if(user.is_active) "true" else "false"}"
    }

    override fun getItemCount(): Int = userList.size


    fun updateData(newList: List<User1>) {
        userList.clear()
        userList.addAll(newList)
        notifyDataSetChanged()
    }
}