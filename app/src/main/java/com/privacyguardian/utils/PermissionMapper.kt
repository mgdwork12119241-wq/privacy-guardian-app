package com.privacyguardian.utils

import android.Manifest
import android.content.pm.PermissionInfo

/**
 * Maps Android permissions to Arabic explanations and risk levels
 */
object PermissionMapper {

    data class PermissionDetails(
        val arabicName: String,
        val explanation: String,
        val isDangerous: Boolean,
        val riskScore: Int // 0-100, higher is riskier
    )

    private val permissionMap = mapOf(
        // Dangerous Permissions - High Risk
        Manifest.permission.CAMERA to PermissionDetails(
            "الكاميرا",
            "يمكن للتطبيق الوصول إلى الكاميرا والتقاط الصور والفيديو في أي وقت",
            true,
            85
        ),
        Manifest.permission.RECORD_AUDIO to PermissionDetails(
            "الميكروفون",
            "يمكن للتطبيق تسجيل الصوت والاستماع إلى محيطك",
            true,
            90
        ),
        Manifest.permission.ACCESS_FINE_LOCATION to PermissionDetails(
            "الموقع الدقيق",
            "يمكن للتطبيق تتبع موقعك الجغرافي بدقة عالية (GPS)",
            true,
            95
        ),
        Manifest.permission.ACCESS_COARSE_LOCATION to PermissionDetails(
            "الموقع التقريبي",
            "يمكن للتطبيق معرفة موقعك التقريبي عبر الشبكة",
            true,
            70
        ),
        Manifest.permission.ACCESS_BACKGROUND_LOCATION to PermissionDetails(
            "الموقع في الخلفية",
            "يمكن للتطبيق تتبع موقعك حتى عندما لا تستخدمه",
            true,
            100
        ),
        Manifest.permission.READ_CONTACTS to PermissionDetails(
            "قراءة جهات الاتصال",
            "يمكن للتطبيق الوصول إلى جميع أرقام الهاتف والأسماء والبريد الإلكتروني",
            true,
            80
        ),
        Manifest.permission.WRITE_CONTACTS to PermissionDetails(
            "تعديل جهات الاتصال",
            "يمكن للتطبيق تعديل أو حذف جهات الاتصال",
            true,
            75
        ),
        Manifest.permission.READ_CALL_LOG to PermissionDetails(
            "قراءة سجل المكالمات",
            "يمكن للتطبيق رؤية جميع المكالمات الصادرة والواردة",
            true,
            85
        ),
        Manifest.permission.WRITE_CALL_LOG to PermissionDetails(
            "تعديل سجل المكالمات",
            "يمكن للتطبيق حذف سجل المكالمات",
            true,
            70
        ),
        Manifest.permission.READ_PHONE_STATE to PermissionDetails(
            "حالة الهاتف",
            "يمكن للتطبيق معرفة رقم هاتفك وحالة الشبكة والمكالمات",
            true,
            60
        ),
        Manifest.permission.CALL_PHONE to PermissionDetails(
            "إجراء مكالمات",
            "يمكن للتطبيق إجراء مكالمات هاتفية بدون علمك",
            true,
            80
        ),
        Manifest.permission.READ_SMS to PermissionDetails(
            "قراءة الرسائل",
            "يمكن للتطبيق قراءة جميع رسائلك النصية",
            true,
            90
        ),
        Manifest.permission.SEND_SMS to PermissionDetails(
            "إرسال الرسائل",
            "يمكن للتطبيق إرسال رسائل نصية قد تكلفك رسوماً",
            true,
            85
        ),
        Manifest.permission.RECEIVE_SMS to PermissionDetails(
            "استقبال الرسائل",
            "يمكن للتطبيق اعتراض الرسائل الواردة",
            true,
            75
        ),
        Manifest.permission.READ_EXTERNAL_STORAGE to PermissionDetails(
            "قراءة التخزين",
            "يمكن للتطبيق الوصول إلى جميع ملفاتك وصورك ومقاطع الفيديو",
            true,
            70
        ),
        Manifest.permission.WRITE_EXTERNAL_STORAGE to PermissionDetails(
            "كتابة التخزين",
            "يمكن للتطبيق تعديل أو حذف ملفاتك",
            true,
            65
        ),
        Manifest.permission.READ_CALENDAR to PermissionDetails(
            "قراءة التقويم",
            "يمكن للتطبيق رؤية مواعيدك وأحداثك الشخصية",
            true,
            50
        ),
        Manifest.permission.WRITE_CALENDAR to PermissionDetails(
            "تعديل التقويم",
            "يمكن للتطبيق إضافة أو حذف أحداث من تقويمك",
            true,
            45
        ),
        Manifest.permission.READ_PHONE_NUMBERS to PermissionDetails(
            "قراءة أرقام الهاتف",
            "يمكن للتطبيق معرفة أرقام هواتفك",
            true,
            55
        ),
        Manifest.permission.ANSWER_PHONE_CALLS to PermissionDetails(
            "الرد على المكالمات",
            "يمكن للتطبيق الرد على المكالمات أو قطعها",
            true,
            75
        ),
        Manifest.permission.ADD_VOICEMAIL to PermissionDetails(
            "إضافة بريد صوتي",
            "يمكن للتطبيق إضافة رسائل صوتية",
            true,
            40
        ),
        Manifest.permission.USE_SIP to PermissionDetails(
            "استخدام SIP",
            "يمكن للتطبيق إجراء مكالمات عبر الإنترنت",
            true,
            50
        ),
        Manifest.permission.BODY_SENSORS to PermissionDetails(
            "أجهزة استشعار الجسم",
            "يمكن للتطبيق الوصول إلى بيانات الصحة والنشاط البدني",
            true,
            60
        ),
        Manifest.permission.ACTIVITY_RECOGNITION to PermissionDetails(
            "التعرف على النشاط",
            "يمكن للتطبيق معرفة ما إذا كنت تمشي أو تركب أو تقود",
            true,
            55
        ),

        // Normal Permissions - Low Risk
        Manifest.permission.INTERNET to PermissionDetails(
            "الإنترنت",
            "الوصول إلى الإنترنت - صلاحية عادية مطلوبة لمعظم التطبيقات",
            false,
            10
        ),
        Manifest.permission.ACCESS_NETWORK_STATE to PermissionDetails(
            "حالة الشبكة",
            "معرفة ما إذا كان الجهاز متصلاً بالإنترنت",
            false,
            5
        ),
        Manifest.permission.ACCESS_WIFI_STATE to PermissionDetails(
            "حالة الواي فاي",
            "معرفة حالة اتصال الواي فاي",
            false,
            5
        ),
        Manifest.permission.BLUETOOTH to PermissionDetails(
            "البلوتوث",
            "الوصول إلى اتصال البلوتوث",
            false,
            15
        ),
        Manifest.permission.BLUETOOTH_ADMIN to PermissionDetails(
            "إدارة البلوتوث",
            "إدارة إعدادات البلوتوث",
            false,
            15
        ),
        Manifest.permission.RECEIVE_BOOT_COMPLETED to PermissionDetails(
            "التشغيل التلقائي",
            "بدء التطبيق تلقائياً عند تشغيل الجهاز",
            false,
            20
        ),
        Manifest.permission.VIBRATE to PermissionDetails(
            "الاهتزاز",
            "التحكم في اهتزاز الجهاز",
            false,
            0
        ),
        Manifest.permission.WAKE_LOCK to PermissionDetails(
            "إبقاء الشاشة نشطة",
            "منع الجهاز من الدخول في وضع السكون",
            false,
            10
        ),
        Manifest.permission.FOREGROUND_SERVICE to PermissionDetails(
            "خدمة المقدمة",
            "تشغيل خدمة في المقدمة",
            false,
            15
        ),
        Manifest.permission.REQUEST_INSTALL_PACKAGES to PermissionDetails(
            "تثبيت التطبيقات",
            "يمكن للتطبيق تثبيت تطبيقات أخرى",
            false,
            40
        )
    )

    fun getPermissionDetails(permissionName: String): PermissionDetails {
        return permissionMap[permissionName] ?: PermissionDetails(
            permissionName.substringAfterLast("."),
            "صلاحية غير معروفة - يرجى التحقق من مصدر التطبيق",
            true,
            50
        )
    }

    fun isDangerous(permissionName: String): Boolean {
        return permissionMap[permissionName]?.isDangerous ?: true
    }

    fun getRiskScore(permissionName: String): Int {
        return permissionMap[permissionName]?.riskScore ?: 50
    }

    fun getAllDangerousPermissions(): List<String> {
        return permissionMap.filter { it.value.isDangerous }.map { it.key }
    }
}