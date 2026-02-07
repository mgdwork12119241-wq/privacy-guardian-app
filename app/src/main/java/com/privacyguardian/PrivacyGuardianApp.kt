package com.privacyguardian

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class PrivacyGuardianApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Force dark mode as default
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}