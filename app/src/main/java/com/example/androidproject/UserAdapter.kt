package com.example.androidproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(
    private val userList: MutableList<User>,
    private val onItemClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFullName: TextView = itemView.findViewById(R.id.tvFullName)
        val tvUsernameId: TextView = itemView.findViewById(R.id.tvUsernameId)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val tvPhone: TextView = itemView.findViewById(R.id.tvPhoneNumber)
        val tvStaff: TextView = itemView.findViewById(R.id.tvStaff)

        init {
            itemView.setOnClickListener {
                onItemClick(userList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_useradapter, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.tvFullName.text = user.fullName
        holder.tvUsernameId.text = user.usernameAndId
        holder.tvEmail.text = "Email: ${user.email}"
        holder.tvPhone.text = "Phone: ${user.phone_number ?: "N/A"}"
        holder.tvStaff.text = "Staff: ${if(user.is_active) "true" else "false"}"
    }

    override fun getItemCount(): Int = userList.size

    fun updateData(newList: List<User>) {
        userList.clear()
        userList.addAll(newList)
        notifyDataSetChanged()
    }
}
