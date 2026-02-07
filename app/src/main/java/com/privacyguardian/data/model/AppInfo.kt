package com.privacyguardian.data.model

import android.graphics.drawable.Drawable

/**
 * Represents an installed application with its privacy analysis data
 */
data class AppInfo(
    val packageName: String,
    val appName: String,
    val versionName: String,
    val versionCode: Long,
    val icon: Drawable?,
    val installTime: Long,
    val appSize: Long,
    val permissions: List<PermissionInfo>,
    val sdks: List<SdkInfo>,
    val securityScore: Int,
    val riskLevel: RiskLevel,
    val isSystemApp: Boolean
) {
    val dangerousPermissionsCount: Int
        get() = permissions.count { it.isDangerous }
    
    val normalPermissionsCount: Int
        get() = permissions.count { !it.isDangerous }
}

enum class RiskLevel {
    SAFE, LOW, MEDIUM, HIGH
}

data class PermissionInfo(
    val name: String,
    val isDangerous: Boolean,
    val isGranted: Boolean,
    val description: String,
    val arabicExplanation: String
)

data class SdkInfo(
    val name: String,
    val category: SdkCategory,
    val description: String
)

enum class SdkCategory {
    ANALYTICS, ADVERTISING, SOCIAL, PAYMENT, TRACKING, OTHER
}