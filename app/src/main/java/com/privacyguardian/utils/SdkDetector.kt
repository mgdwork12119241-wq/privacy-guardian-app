package com.privacyguardian.utils

import com.privacyguardian.data.model.SdkCategory
import com.privacyguardian.data.model.SdkInfo

/**
 * Detects common SDKs included in APKs based on package name patterns
 */
object SdkDetector {

    private val sdkPatterns = mapOf(
        // Analytics
        "com.google.firebase.analytics" to SdkInfo(
            "Firebase Analytics",
            SdkCategory.ANALYTICS,
            "جمع بيانات الاستخدام وإرسالها إلى Google"
        ),
        "com.google.android.gms.analytics" to SdkInfo(
            "Google Analytics",
            SdkCategory.ANALYTICS,
            "تتبع سلوك المستخدم داخل التطبيق"
        ),
        "com.mixpanel.android" to SdkInfo(
            "Mixpanel",
            SdkCategory.ANALYTICS,
            "تحليلات متقدمة لتتبع الأحداث"
        ),
        "com.amplitude.api" to SdkInfo(
            "Amplitude",
            SdkCategory.ANALYTICS,
            "منصة تحليلات المنتجات"
        ),
        "com.flurry.android" to SdkInfo(
            "Flurry Analytics",
            SdkCategory.ANALYTICS,
            "تحليلات Yahoo للتطبيقات"
        ),
        "com.appsflyer" to SdkInfo(
            "AppsFlyer",
            SdkCategory.ANALYTICS,
            "تحليلات التسويق وال attributions"
        ),

        // Advertising
        "com.google.android.gms.ads" to SdkInfo(
            "Google AdMob",
            SdkCategory.ADVERTISING,
            "عرض الإعلانات وجمع بيانات للاستهداف"
        ),
        "com.facebook.ads" to SdkInfo(
            "Facebook Audience Network",
            SdkCategory.ADVERTISING,
            "إعلانات فيسبوك المدمجة"
        ),
        "com.unity3d.ads" to SdkInfo(
            "Unity Ads",
            SdkCategory.ADVERTISING,
            "منصة إعلانات Unity للألعاب"
        ),
        "com.chartboost.sdk" to SdkInfo(
            "Chartboost",
            SdkCategory.ADVERTISING,
            "شبكة إعلانات للألعاب"
        ),
        "com.adcolony.sdk" to SdkInfo(
            "AdColony",
            SdkCategory.ADVERTISING,
            "إعلانات الفيديو عالية الجودة"
        ),
        "com.vungle.warren" to SdkInfo(
            "Vungle",
            SdkCategory.ADVERTISING,
            "إعلانات الفيديو للتطبيقات"
        ),
        "com.applovin.sdk" to SdkInfo(
            "AppLovin",
            SdkCategory.ADVERTISING,
            "منصة تحقيق الدخل من الإعلانات"
        ),
        "com.mopub.mobileads" to SdkInfo(
            "MoPub",
            SdkCategory.ADVERTISING,
            "منصة إعلانات تويتر"
        ),
        "com.inmobi.ads" to SdkInfo(
            "InMobi",
            SdkCategory.ADVERTISING,
            "شبكة إعلانات عالمية"
        ),
        "com.startapp.android" to SdkInfo(
            "StartApp",
            SdkCategory.ADVERTISING,
            "إعلانات وتحقيق دخل"
        ),

        // Social
        "com.facebook.sdk" to SdkInfo(
            "Facebook SDK",
            SdkCategory.SOCIAL,
            "تكامل مع فيسبوك ومشاركة البيانات"
        ),
        "com.facebook.login" to SdkInfo(
            "Facebook Login",
            SdkCategory.SOCIAL,
            "تسجيل الدخول عبر فيسبوك"
        ),
        "com.facebook.share" to SdkInfo(
            "Facebook Share",
            SdkCategory.SOCIAL,
            "مشاركة المحتوى على فيسبوك"
        ),
        "com.twitter.sdk" to SdkInfo(
            "Twitter SDK",
            SdkCategory.SOCIAL,
            "تكامل مع تويتر"
        ),
        "com.snapchat.sdk" to SdkInfo(
            "Snapchat SDK",
            SdkCategory.SOCIAL,
            "تكامل مع سناب شات"
        ),
        "com.linkedin.android" to SdkInfo(
            "LinkedIn SDK",
            SdkCategory.SOCIAL,
            "تكامل مع لينكد إن"
        ),
        "com.instagram.android" to SdkInfo(
            "Instagram SDK",
            SdkCategory.SOCIAL,
            "تكامل مع إنستغرام"
        ),
        "com.whatsapp" to SdkInfo(
            "WhatsApp SDK",
            SdkCategory.SOCIAL,
            "مشاركة عبر واتساب"
        ),
        "com.telegram" to SdkInfo(
            "Telegram SDK",
            SdkCategory.SOCIAL,
            "تكامل مع تلغرام"
        ),

        // Payment
        "com.android.billingclient" to SdkInfo(
            "Google Play Billing",
            SdkCategory.PAYMENT,
            "المشتريات داخل التطبيق"
        ),
        "com.paypal.android" to SdkInfo(
            "PayPal SDK",
            SdkCategory.PAYMENT,
            "مدفوعات PayPal"
        ),
        "com.stripe.android" to SdkInfo(
            "Stripe SDK",
            SdkCategory.PAYMENT,
            "معالجة المدفوعات"
        ),
        "com.braintreepayments" to SdkInfo(
            "Braintree",
            SdkCategory.PAYMENT,
            "بوابة دفع PayPal"
        ),
        "com.squareup.sdk" to SdkInfo(
            "Square SDK",
            SdkCategory.PAYMENT,
            "مدفوعات Square"
        ),

        // Tracking
        "com.adjust.sdk" to SdkInfo(
            "Adjust",
            SdkCategory.TRACKING,
            "تتبع attributions والاحتيال"
        ),
        "com.kochava.base" to SdkInfo(
            "Kochava",
            SdkCategory.TRACKING,
            "تتبع التسويق والattributions"
        ),
        "com.tune.ma" to SdkInfo(
            "Tune",
            SdkCategory.TRACKING,
            "تحليلات التسويق المتنقل"
        ),
        "com.localytics.android" to SdkInfo(
            "Localytics",
            SdkCategory.TRACKING,
            "تتبع وتحليلات التطبيقات"
        ),
        "com.bugsnag.android" to SdkInfo(
            "Bugsnag",
            SdkCategory.TRACKING,
            "تتبع الأخطاء والأعطال"
        ),
        "com.crashlytics.android" to SdkInfo(
            "Crashlytics",
            SdkCategory.TRACKING,
            "تقرير أعطال Firebase"
        ),
        "com.google.firebase.crashlytics" to SdkInfo(
            "Firebase Crashlytics",
            SdkCategory.TRACKING,
            "تتبع أعطال التطبيق"
        ),

        // Google Services
        "com.google.firebase" to SdkInfo(
            "Firebase SDK",
            SdkCategory.OTHER,
            "خدمات Google المتكاملة"
        ),
        "com.google.android.gms" to SdkInfo(
            "Google Play Services",
            SdkCategory.OTHER,
            "خدمات Google Play الأساسية"
        ),
        "com.google.android.play" to SdkInfo(
            "Google Play Core",
            SdkCategory.OTHER,
            "مكتبة جوهرية لـ Google Play"
        ),

        // Maps
        "com.google.android.gms.maps" to SdkInfo(
            "Google Maps",
            SdkCategory.OTHER,
            "خرائط Google المدمجة"
        ),
        "com.mapbox.mapboxsdk" to SdkInfo(
            "Mapbox",
            SdkCategory.OTHER,
            "خرائط Mapbox المخصصة"
        ),

        // Push Notifications
        "com.google.firebase.messaging" to SdkInfo(
            "Firebase Cloud Messaging",
            SdkCategory.OTHER,
            "إشعارات الدفع من Firebase"
        ),
        "com.onesignal" to SdkInfo(
            "OneSignal",
            SdkCategory.OTHER,
            "منصة إشعارات الدفع"
        ),
        "com.parse" to SdkInfo(
            "Parse SDK",
            SdkCategory.OTHER,
            "منصة Backend كخدمة"
        ),

        // Database
        "io.realm" to SdkInfo(
            "Realm",
            SdkCategory.OTHER,
            "قاعدة بيانات محلية"
        ),
        "com.squareup.okhttp" to SdkInfo(
            "OkHttp",
            SdkCategory.OTHER,
            "مكتبة HTTP client"
        ),
        "com.squareup.retrofit" to SdkInfo(
            "Retrofit",
            SdkCategory.OTHER,
            "مكتبة HTTP لـ REST APIs"
        ),
        "com.google.gson" to SdkInfo(
            "Gson",
            SdkCategory.OTHER,
            "مكتبة JSON serialization"
        ),

        // Image Loading
        "com.bumptech.glide" to SdkInfo(
            "Glide",
            SdkCategory.OTHER,
            "تحميل وعرض الصور"
        ),
        "com.squareup.picasso" to SdkInfo(
            "Picasso",
            SdkCategory.OTHER,
            "مكتبة تحميل الصور"
        ),
        "com.facebook.fresco" to SdkInfo(
            "Fresco",
            SdkCategory.OTHER,
            "إدارة الصور من فيسبوك"
        ),

        // Authentication
        "com.google.android.gms.auth" to SdkInfo(
            "Google Sign-In",
            SdkCategory.SOCIAL,
            "تسجيل الدخول عبر Google"
        ),
        "com.firebaseui" to SdkInfo(
            "Firebase UI",
            SdkCategory.OTHER,
            "واجهات مصادقة Firebase"
        )
    )

    fun detectSdks(packageName: String, appClasses: List<String>? = null): List<SdkInfo> {
        val detectedSdks = mutableListOf<SdkInfo>()
        
        // Check package name patterns
        for ((pattern, sdkInfo) in sdkPatterns) {
            if (packageName.contains(pattern, ignoreCase = true) ||
                appClasses?.any { it.contains(pattern, ignoreCase = true) } == true) {
                if (!detectedSdks.any { it.name == sdkInfo.name }) {
                    detectedSdks.add(sdkInfo)
                }
            }
        }
        
        return detectedSdks.sortedBy { it.category.name }
    }

    fun getSdkRiskScore(sdks: List<SdkInfo>): Int {
        var score = 0
        for (sdk in sdks) {
            score += when (sdk.category) {
                SdkCategory.ADVERTISING -> 15
                SdkCategory.TRACKING -> 20
                SdkCategory.ANALYTICS -> 10
                SdkCategory.SOCIAL -> 10
                SdkCategory.PAYMENT -> 5
                SdkCategory.OTHER -> 2
            }
        }
        return minOf(score, 100)
    }
}