package com.privacyguardian.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.privacyguardian.R
import com.privacyguardian.data.model.AppInfo
import com.privacyguardian.data.model.RiskLevel
import java.text.SimpleDateFormat
import java.util.Locale

class AppsAdapter(
    private val onAppClick: (AppInfo) -> Unit
) : ListAdapter<AppInfo, AppsAdapter.AppViewHolder>(AppDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view, onAppClick)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AppViewHolder(
        itemView: View,
        private val onAppClick: (AppInfo) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)
        private val appIcon: ImageView = itemView.findViewById(R.id.appIcon)
        private val appName: TextView = itemView.findViewById(R.id.appName)
        private val packageName: TextView = itemView.findViewById(R.id.packageName)
        private val permissionCount: TextView = itemView.findViewById(R.id.permissionCount)
        private val riskLevel: TextView = itemView.findViewById(R.id.riskLevel)
        private val scoreProgress: ProgressBar = itemView.findViewById(R.id.scoreProgress)
        private val scoreText: TextView = itemView.findViewById(R.id.scoreText)

        fun bind(appInfo: AppInfo) {
            appIcon.setImageDrawable(appInfo.icon)
            appName.text = appInfo.appName
            packageName.text = appInfo.packageName
            
            val dangerousCount = appInfo.dangerousPermissionsCount
            val totalCount = appInfo.permissions.size
            permissionCount.text = "$dangerousCount/$totalCount صلاحية خطيرة"
            
            // Risk level styling
            val riskText = when (appInfo.riskLevel) {
                RiskLevel.SAFE -> "آمن"
                RiskLevel.LOW -> "منخفض"
                RiskLevel.MEDIUM -> "متوسط"
                RiskLevel.HIGH -> "عالي"
            }
            riskLevel.text = riskText
            
            val riskColor = when (appInfo.riskLevel) {
                RiskLevel.SAFE -> R.color.safe_green
                RiskLevel.LOW -> R.color.info_blue
                RiskLevel.MEDIUM -> R.color.warning_yellow
                RiskLevel.HIGH -> R.color.danger_red
            }
            riskLevel.setTextColor(ContextCompat.getColor(itemView.context, riskColor))
            
            // Score circle
            scoreProgress.progress = appInfo.securityScore
            scoreText.text = appInfo.securityScore.toString()
            
            // Progress color based on score
            val progressColor = when {
                appInfo.securityScore >= 80 -> R.color.safe_green
                appInfo.securityScore >= 60 -> R.color.info_blue
                appInfo.securityScore >= 40 -> R.color.warning_yellow
                else -> R.color.danger_red
            }
            scoreProgress.progressTintList = ContextCompat.getColorStateList(itemView.context, progressColor)
            
            // Click listener
            itemView.setOnClickListener { onAppClick(appInfo) }
        }
    }

    class AppDiffCallback : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem == newItem
        }
    }
}