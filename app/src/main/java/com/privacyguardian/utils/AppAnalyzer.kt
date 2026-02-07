package com.privacyguardian.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.os.Build
import com.privacyguardian.data.model.AppInfo
import com.privacyguardian.data.model.PermissionInfo as AppPermissionInfo
import com.privacyguardian.data.model.RiskLevel
import com.privacyguardian.data.model.SdkInfo
import java.io.File
import java.util.Date

/**
 * Analyzes installed apps for privacy and security risks
 */
class AppAnalyzer(private val context: Context) {

    private val packageManager: PackageManager = context.packageManager

    fun analyzeAllApps(): List<AppInfo> {
        val apps = mutableListOf<AppInfo>()
        
        val installedPackages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getInstalledPackages(PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            packageManager.getInstalledPackages(0)
        }
        
        for (packageInfo in installedPackages) {
            try {
                val appInfo = analyzeApp(packageInfo.packageName)
                if (appInfo != null) {
                    apps.add(appInfo)
                }
            } catch (e: Exception) {
                // Skip apps that can't be analyzed
                continue
            }
        }
        
        return apps.sortedByDescending { it.securityScore }
    }

    fun analyzeApp(packageName: String): AppInfo? {
        return try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(
                    packageName,
                    PackageManager.PackageInfoFlags.of(
                        PackageManager.GET_PERMISSIONS.toLong() or
                        PackageManager.GET_META_DATA.toLong()
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_PERMISSIONS or PackageManager.GET_META_DATA
                )
            }

            val applicationInfo = packageInfo.applicationInfo ?: return null
            
            val permissions = analyzePermissions(packageInfo)
            val sdks = SdkDetector.detectSdks(packageName)
            val securityScore = calculateSecurityScore(permissions, sdks, applicationInfo)
            val riskLevel = calculateRiskLevel(securityScore, permissions)

            AppInfo(
                packageName = packageName,
                appName = packageManager.getApplicationLabel(applicationInfo).toString(),
                versionName = packageInfo.versionName ?: "Unknown",
                versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    packageInfo.longVersionCode
                } else {
                    @Suppress("DEPRECATION")
                    packageInfo.versionCode.toLong()
                },
                icon = packageManager.getApplicationIcon(applicationInfo),
                installTime = packageInfo.firstInstallTime,
                appSize = getAppSize(applicationInfo),
                permissions = permissions,
                sdks = sdks,
                securityScore = securityScore,
                riskLevel = riskLevel,
                isSystemApp = (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun analyzePermissions(packageInfo: PackageInfo): List<AppPermissionInfo> {
        val permissions = mutableListOf<AppPermissionInfo>()
        
        val requestedPermissions = packageInfo.requestedPermissions ?: return permissions
        val permissionFlags = packageInfo.requestedPermissionsFlags ?: IntArray(0)
        
        for (i in requestedPermissions.indices) {
            val permissionName = requestedPermissions[i]
            val isGranted = (permissionFlags.getOrNull(i) ?: 0) and 
                    PackageInfo.REQUESTED_PERMISSION_GRANTED != 0
            
            val details = PermissionMapper.getPermissionDetails(permissionName)
            
            permissions.add(
                AppPermissionInfo(
                    name = permissionName,
                    isDangerous = details.isDangerous,
                    isGranted = isGranted,
                    description = details.explanation,
                    arabicExplanation = "${details.arabicName}: ${details.explanation}"
                )
            )
        }
        
        return permissions
    }

    private fun calculateSecurityScore(
        permissions: List<AppPermissionInfo>,
        sdks: List<SdkInfo>,
        appInfo: ApplicationInfo
    ): Int {
        var score = 100
        
        // Deduct for dangerous permissions
        for (permission in permissions) {
            if (permission.isDangerous) {
                val riskScore = PermissionMapper.getRiskScore(permission.name)
                score -= (riskScore * 0.3).toInt()
            }
        }
        
        // Deduct for SDKs
        score -= SdkDetector.getSdkRiskScore(sdks)
        
        // Deduct for system apps (they usually have more permissions)
        if ((appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0) {
            score -= 10
        }
        
        // Bonus for apps with few permissions
        if (permissions.size < 5) {
            score += 10
        }
        
        return score.coerceIn(0, 100)
    }

    private fun calculateRiskLevel(
        securityScore: Int,
        permissions: List<AppPermissionInfo>
    ): RiskLevel {
        val dangerousCount = permissions.count { it.isDangerous }
        
        return when {
            securityScore >= 80 && dangerousCount <= 2 -> RiskLevel.SAFE
            securityScore >= 60 && dangerousCount <= 4 -> RiskLevel.LOW
            securityScore >= 40 || dangerousCount <= 6 -> RiskLevel.MEDIUM
            else -> RiskLevel.HIGH
        }
    }

    private fun getAppSize(applicationInfo: ApplicationInfo): Long {
        return try {
            File(applicationInfo.sourceDir).length()
        } catch (e: Exception) {
            0L
        }
    }

    fun generatePrivacyReport(appInfo: AppInfo): String {
        val report = StringBuilder()
        
        report.append("تحليل خصوصية التطبيق: ${appInfo.appName}\n\n")
        
        // Risk Assessment
        report.append("التقييم العام: ")
        report.append(when (appInfo.riskLevel) {
            RiskLevel.SAFE -> "آمن ✅"
            RiskLevel.LOW -> "منخفض الخطورة ⚠️"
            RiskLevel.MEDIUM -> "متوسط الخطورة ⚠️⚠️"
            RiskLevel.HIGH -> "عالي الخطورة 🚨"
        })
        report.append("\n\n")
        
        // Permissions Analysis
        report.append("الصلاحيات المطلوبة:\n")
        if (appInfo.permissions.isEmpty()) {
            report.append("- لا يتطلب أي صلاحيات\n")
        } else {
            val dangerousPerms = appInfo.permissions.filter { it.isDangerous }
            val normalPerms = appInfo.permissions.filter { !it.isDangerous }
            
            if (dangerousPerms.isNotEmpty()) {
                report.append("\nصلاحيات خطيرة (${dangerousPerms.size}):\n")
                dangerousPerms.forEach { perm ->
                    report.append("• ${perm.arabicExplanation}\n")
                }
            }
            
            if (normalPerms.isNotEmpty()) {
                report.append("\nصلاحيات عادية (${normalPerms.size}):\n")
                normalPerms.take(5).forEach { perm ->
                    report.append("• ${perm.arabicExplanation}\n")
                }
                if (normalPerms.size > 5) {
                    report.append("... و${normalPerms.size - 5} أخرى\n")
                }
            }
        }
        
        // SDKs Analysis
        if (appInfo.sdks.isNotEmpty()) {
            report.append("\nمكتبات وSDKs مكتشفة:\n")
            appInfo.sdks.forEach { sdk ->
                report.append("• ${sdk.name}: ${sdk.description}\n")
            }
        }
        
        // Recommendations
        report.append("\nالتوصيات:\n")
        when (appInfo.riskLevel) {
            RiskLevel.SAFE -> report.append("✅ هذا التطبيق آمن نسبياً ولا يشكل تهديداً كبيراً للخصوصية\n")
            RiskLevel.LOW -> report.append("⚠️ راجع الصلاحيات الممنوحة وتأكد من حاجة التطبيق لها\n")
            RiskLevel.MEDIUM -> report.append("⚠️⚠️ يحتوي على صلاحيات خطيرة - تأكد من ثقتك بالمطور\n")
            RiskLevel.HIGH -> report.append("🚨 يحتوي على صلاحيات عالية الخطورة - يُنصح بالحذف إن لم يكن ضرورياً\n")
        }
        
        if (appInfo.sdks.any { it.category == com.privacyguardian.data.model.SdkCategory.ADVERTISING }) {
            report.append("📊 يحتوي على SDKs إعلانية - قد يقوم بجمع بيانات لاستهدافك بالإعلانات\n")
        }
        
        if (appInfo.sdks.any { it.category == com.privacyguardian.data.model.SdkCategory.TRACKING }) {
            report.append("👁️ يحتوي على SDKs تتبع - يراقب نشاطك داخل التطبيق\n")
        }
        
        return report.toString()
    }
}