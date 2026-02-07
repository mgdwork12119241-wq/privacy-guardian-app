package com.privacyguardian.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.privacyguardian.R
import com.privacyguardian.databinding.ActivityPremiumBinding

class PremiumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPremiumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.premium_title)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnContactTelegram.setOnClickListener {
            showContactDialog()
        }

        binding.btnFeature1.setOnClickListener {
            showFeatureInfo("تحليل APK شامل", "تحليل عميق لملف APK يكشف عن جميع المكونات والمكتبات المضمنة")
        }

        binding.btnFeature2.setOnClickListener {
            showFeatureInfo("كشف SDKs", "اكتشاف جميع SDKs والمكتبات الإعلانية والتتبع المضمنة في التطبيق")
        }

        binding.btnFeature3.setOnClickListener {
            showFeatureInfo("استخراج Domains", "استخراج جميع النطاقات والـ endpoints التي يتواصل معها التطبيق")
        }

        binding.btnFeature4.setOnClickListener {
            showFeatureInfo("تقرير PDF", "تصدير تقرير مفصل بصيغة PDF يمكن مشاركته")
        }
    }

    private fun showContactDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("الترقية للنسخة المدفوعة")
            .setMessage("للحصول على النسخة المدفوعة، يرجى التواصل معنا عبر Telegram:\n\n@Mhsenmhsen1\n\nسنقوم بإرسال كود التفعيل أو نسخة APK Pro بعد تأكيد الدفع.")
            .setPositiveButton("فتح Telegram") { _, _ ->
                openTelegram()
            }
            .setNegativeButton("إلغاء", null)
            .show()
    }

    private fun openTelegram() {
        val telegramIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://t.me/Mhsenmhsen1")
        }
        
        try {
            startActivity(telegramIntent)
        } catch (e: Exception) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Mhsenmhsen1"))
            startActivity(browserIntent)
        }
    }

    private fun showFeatureInfo(title: String, description: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(description)
            .setPositiveButton("فهمت", null)
            .show()
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