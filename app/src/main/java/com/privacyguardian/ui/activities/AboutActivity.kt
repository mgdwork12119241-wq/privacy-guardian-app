package com.privacyguardian.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.privacyguardian.BuildConfig
import com.privacyguardian.R
import com.privacyguardian.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set version
        binding.versionText.text = getString(R.string.about_version, BuildConfig.VERSION_NAME)

        // Telegram contact button
        binding.btnContactTelegram.setOnClickListener {
            openTelegram()
        }
    }

    private fun openTelegram() {
        val telegramIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://t.me/Mhsenmhsen1")
        }
        
        try {
            startActivity(telegramIntent)
        } catch (e: Exception) {
            // If Telegram app is not installed, open in browser
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Mhsenmhsen1"))
            startActivity(browserIntent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}