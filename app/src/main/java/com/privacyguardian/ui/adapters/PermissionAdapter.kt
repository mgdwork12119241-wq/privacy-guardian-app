package com.privacyguardian.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.privacyguardian.R
import com.privacyguardian.data.model.PermissionInfo

class PermissionAdapter : RecyclerView.Adapter<PermissionAdapter.PermissionViewHolder>() {

    private var permissions: List<PermissionInfo> = emptyList()

    fun submitList(newList: List<PermissionInfo>) {
        permissions = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_permission, parent, false)
        return PermissionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PermissionViewHolder, position: Int) {
        holder.bind(permissions[position])
    }

    override fun getItemCount(): Int = permissions.size

    class PermissionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val permissionIcon: ImageView = itemView.findViewById(R.id.permissionIcon)
        private val permissionName: TextView = itemView.findViewById(R.id.permissionName)
        private val permissionDescription: TextView = itemView.findViewById(R.id.permissionDescription)
        private val permissionStatus: TextView = itemView.findViewById(R.id.permissionStatus)

        fun bind(permission: PermissionInfo) {
            permissionName.text = permission.arabicExplanation.substringBefore(":")
            permissionDescription.text = permission.arabicExplanation.substringAfter(":", permission.description)
            
            // Icon based on risk
            val iconRes = if (permission.isDangerous) {
                R.drawable.ic_warning
            } else {
                R.drawable.ic_check_circle
            }
            permissionIcon.setImageResource(iconRes)
            
            val iconColor = if (permission.isDangerous) {
                R.color.danger_red
            } else {
                R.color.safe_green
            }
            permissionIcon.setColorFilter(ContextCompat.getColor(itemView.context, iconColor))
            
            // Status
            if (permission.isGranted) {
                permissionStatus.text = "ممنوحة"
                permissionStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.danger_red))
            } else {
                permissionStatus.text = "غير ممنوحة"
                permissionStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.text_secondary_dark))
            }
        }
    }
}