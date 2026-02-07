package com.privacyguardian.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.privacyguardian.R
import com.privacyguardian.data.model.RiskLevel
import com.privacyguardian.databinding.ActivityAppDetailsBinding
import com.privacyguardian.ui.adapters.PermissionAdapter
import com.privacyguardian.utils.AppAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppDetailsBinding
    private lateinit var appAnalyzer: AppAnalyzer
    private lateinit var permissionAdapter: PermissionAdapter

    companion object {
        const val EXTRA_PACKAGE_NAME = "extra_package_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        appAnalyzer = AppAnalyzer(this)
        setupPermissionsRecyclerView()

        val packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME)
        if (packageName != null) {
            loadAppDetails(packageName)
        } else {
            finish()
        }
    }

    private fun setupPermissionsRecyclerView() {
        permissionAdapter = PermissionAdapter()
        binding.recyclerPermissions.apply {
            layoutManager = LinearLayoutManager(this@AppDetailsActivity)
            adapter = permissionAdapter
            setHasFixedSize(true)
        }
    }

    private fun loadAppDetails(packageName: String) {
        lifecycleScope.launch {
            try {
                val appInfo = withContext(Dispatchers.IO) {
                    appAnalyzer.analyzeApp(packageName)
                }

                if (appInfo != null) {
                    displayAppInfo(appInfo)
                } else {
                    finish()
                }
            } catch (e: Exception) {
                finish()
            }
        }
    }

    private fun displayAppInfo(appInfo: com.privacyguardian.data.model.AppInfo) {
        // Header
        binding.appIcon.setImageDrawable(appInfo.icon)
        binding.appName.text = appInfo.appName
        binding.packageName.text = appInfo.packageName
        binding.versionInfo.text = getString(R.string.version, appInfo.versionName)

        // Security Score
        binding.scoreProgress.progress = appInfo.securityScore
        binding.scoreText.text = appInfo.securityScore.toString()
        
        // Score color
        val scoreColor = when {
            appInfo.securityScore >= 80 -> R.color.safe_green
            appInfo.securityScore >= 60 -> R.color.info_blue
            appInfo.securityScore >= 40 -> R.color.warning_yellow
            else -> R.color.danger_red
        }
        binding.scoreProgress.progressTintList = ContextCompat.getColorStateList(this, scoreColor)

        // Risk Label
        val riskText = when (appInfo.riskLevel) {
            RiskLevel.SAFE -> getString(R.string.risk_safe)
            RiskLevel.LOW -> getString(R.string.risk_low)
            RiskLevel.MEDIUM -> getString(R.string.risk_medium)
            RiskLevel.HIGH -> getString(R.string.risk_high)
        }
        binding.riskLabel.text = riskText
        binding.riskLabel.setTextColor(ContextCompat.getColor(this, scoreColor))

        // Permissions
        val dangerousCount = appInfo.dangerousPermissionsCount
        val normalCount = appInfo.normalPermissionsCount
        binding.permissionSummary.text = "$dangerousCount خطيرة / $normalCount عادية"
        permissionAdapter.submitList(appInfo.permissions.sortedByDescending { it.isDangerous })

        // SDKs
        if (appInfo.sdks.isNotEmpty()) {
            val sdkText = appInfo.sdks.joinToString("\n") { "• ${it.name}: ${it.description}" }
            binding.sdkList.text = sdkText
        } else {
            binding.sdkList.text = "لم يتم اكتشاف SDKs معروفة"
        }

        // Privacy Report
        val report = appAnalyzer.generatePrivacyReport(appInfo)
        binding.privacyReport.text = report

        // Premium Banner
        binding.premiumBanner.visibility = if (appInfo.securityScore < 70) {
            View.VISIBLE
        } else {
            View.GONE
        }

        binding.btnUpgrade.setOnClickListener {
            startActivity(Intent(this, PremiumActivity::class.java))
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