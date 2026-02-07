# حارس الخصوصية - Privacy Guardian AR

<p align="center">
  <img src="app/src/main/res/drawable/ic_launcher_foreground.xml" width="120" height="120" alt="App Icon">
</p>

<p align="center">
  <strong>تطبيق عربي لتحليل خصوصية وأمان التطبيقات المثبتة على جهازك</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-100%25-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin">
  <img src="https://img.shields.io/badge/Android-26+-3DDC84?logo=android&logoColor=white" alt="Android">
  <img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="License">
  <img src="https://img.shields.io/badge/Offline-100%25-green" alt="Offline">
</p>

---

## 📱 المميزات

### ✅ النسخة المجانية
- 🔍 فحص جميع التطبيقات المثبتة
- 📋 عرض الصلاحيات المطلوبة لكل تطبيق
- 🌐 تفسير الصلاحيات باللغة العربية
- 🎯 تقييم أمان من 0 إلى 100
- 🚦 تصنيف التطبيقات حسب مستوى الخطورة
- 🌙 وضع Dark Mode
- 🔒 يعمل 100% بدون إنترنت

### ⭐ النسخة المدفوعة
- 🔬 تحليل APK شامل (Static Analysis)
- 🧩 كشف جميع SDKs المضمنة
- 🌐 استخراج Domains و Endpoints
- 📄 تقرير مفصل بصيغة PDF
- 🔄 تحديثات مستمرة

---

## 🛠️ التقنيات المستخدمة

- **Kotlin** - لغة البرمجة الرئيسية
- **Android SDK** - minSdk 26 (Android 8.0)
- **Material Design 3** - واجهة المستخدم
- **ViewBinding** - ربط العناصر
- **Coroutines** - العمليات غير المتزامنة
- **Room** - قاعدة البيانات المحلية

---

## 📥 التحميل

### آخر إصدار
يمكنك تحميل أحدث نسخة Debug APK من قسم [Actions](https://github.com/YOUR_USERNAME/privacy-guardian-ar/actions) أو من [Releases](https://github.com/YOUR_USERNAME/privacy-guardian-ar/releases).

### رابط مباشر
```
https://github.com/YOUR_USERNAME/privacy-guardian-ar/actions/workflows/build.yml
```

---

## 🏗️ البناء محلياً

### المتطلبات
- Android Studio Hedgehog (2023.1.1) أو أحدث
- JDK 17 أو أحدث
- Android SDK 34

### خطوات البناء

1. **استنساخ المستودع**
```bash
git clone https://github.com/YOUR_USERNAME/privacy-guardian-ar.git
cd privacy-guardian-ar
```

2. **فتح المشروع في Android Studio**
```bash
studio .
```

3. **بناء Debug APK**
```bash
./gradlew assembleDebug
```

4. **بناء Release APK**
```bash
./gradlew assembleRelease
```

### ملف الإعدادات (local.properties)
```properties
sdk.dir=/path/to/android/sdk
```

---

## 📁 هيكل المشروع

```
privacy-guardian-ar/
├── app/
│   ├── src/main/
│   │   ├── java/com/privacyguardian/
│   │   │   ├── data/
│   │   │   │   ├── model/          # نماذج البيانات
│   │   │   │   └── repository/     # المستودعات
│   │   │   ├── ui/
│   │   │   │   ├── activities/     # الأنشطة
│   │   │   │   └── adapters/       # المحولات
│   │   │   ├── utils/              # الأدوات المساعدة
│   │   │   │   ├── AppAnalyzer.kt
│   │   │   │   ├── PermissionMapper.kt
│   │   │   │   └── SdkDetector.kt
│   │   │   └── PrivacyGuardianApp.kt
│   │   ├── res/                    # الموارد
│   │   │   ├── layout/             # تخطيطات XML
│   │   │   ├── values/             # القيم
│   │   │   ├── drawable/           # الرسومات
│   │   │   └── menu/               # القوائم
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── .github/
│   └── workflows/
│       └── build.yml               # GitHub Actions
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 🔐 الخصوصية والأمان

- ✅ **لا يتطلب إذن الإنترنت** - يعمل بالكامل Offline
- ✅ **لا يجمع بيانات المستخدمين** - خصوصية تامة
- ✅ **لا يحتوي على trackers** - خالٍ من التتبع
- ✅ **مفتوح المصدر** - يمكن مراجعة الكود كاملاً
- ✅ **تحليل Static فقط** - لا يُعدّل في التطبيقات

---

## 💰 نظام الربح

### النسخة المجانية
- فحص أساسي للتطبيقات
- عرض الصلاحيات والتقييم
- كشف SDKs الأساسية

### النسخة المدفوعة
- تحليل شامل لكل تطبيق
- تفاصيل متقدمة
- تقارير PDF

### طريقة الشراء
1. التواصل عبر Telegram: **@Mhsenmhsen1**
2. إرسال تفاصيل الدفع
3. استلام كود التفعيل أو APK Pro

---

## 🤝 المساهمة

نرحب بمساهماتكم! يمكنكم المساهمة عن طريق:

1. Fork المستودع
2. إنشاء فرع جديد (`git checkout -b feature/amazing-feature`)
3. Commit التغييرات (`git commit -m 'Add amazing feature'`)
4. Push إلى الفرع (`git push origin feature/amazing-feature`)
5. فتح Pull Request

---

## 📝 الترخيص

هذا المشروع مرخص بموجب [MIT License](LICENSE).

```
MIT License

Copyright (c) 2024 Privacy Guardian AR

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

---

## 📞 التواصل

- **Telegram:** [@Mhsenmhsen1](https://t.me/Mhsenmhsen1)
- **GitHub Issues:** [افتح Issue](https://github.com/YOUR_USERNAME/privacy-guardian-ar/issues)

---

## 🙏 شكر خاص

- [Android Open Source Project](https://source.android.com/)
- [Material Design](https://material.io/design)
- مجتمع المطورين العرب

---

<p align="center">
  <strong>صنع بـ ❤️ للمجتمع العربي</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Made%20with-Kotlin-orange?style=for-the-badge&logo=kotlin" alt="Made with Kotlin">
</p>