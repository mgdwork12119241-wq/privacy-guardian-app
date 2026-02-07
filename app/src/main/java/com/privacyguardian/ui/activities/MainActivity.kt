package com.privacyguardian.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.privacyguardian.R
import com.privacyguardian.data.model.AppInfo
import com.privacyguardian.data.model.RiskLevel
import com.privacyguardian.databinding.ActivityMainBinding
import com.privacyguardian.ui.adapters.AppsAdapter
import com.privacyguardian.utils.AppAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appAnalyzer: AppAnalyzer
    private lateinit var appsAdapter: AppsAdapter
    
    private var allApps: List<AppInfo> = emptyList()
    private var currentFilter: RiskFilter = RiskFilter.ALL

    private enum class RiskFilter {
        ALL, HIGH, MEDIUM, LOW, SAFE
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        
        appAnalyzer = AppAnalyzer(this)
        setupRecyclerView()
        setupSearchView()
        setupChips()
        setupFab()

        checkPermissions()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11+, we need QUERY_ALL_PACKAGES permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.QUERY_ALL_PACKAGES) 
                != PackageManager.PERMISSION_GRANTED) {
                // On Android 11+, this permission is granted via manifest declaration
                // But we still need to handle the case where user denies it
                loadApps()
            } else {
                loadApps()
            }
        } else {
            loadApps()
        }
    }

    private fun setupRecyclerView() {
        appsAdapter = AppsAdapter { appInfo ->
            openAppDetails(appInfo)
        }
        
        binding.recyclerApps.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = appsAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterApps(newText ?: "")
                return true
            }
        })
    }

    private fun setupChips() {
        binding.chipAll.setOnClickListener {
            currentFilter = RiskFilter.ALL
            updateChipSelection(it as Chip)
            filterApps(binding.searchView.query.toString())
        }

        binding.chipHighRisk.setOnClickListener {
            currentFilter = RiskFilter.HIGH
            updateChipSelection(it as Chip)
            filterApps(binding.searchView.query.toString())
        }

        binding.chipMediumRisk.setOnClickListener {
            currentFilter = RiskFilter.MEDIUM
            updateChipSelection(it as Chip)
            filterApps(binding.searchView.query.toString())
        }
    }

    private fun updateChipSelection(selectedChip: Chip) {
        listOf(binding.chipAll, binding.chipHighRisk, binding.chipMediumRisk).forEach { chip ->
            chip.isChecked = chip == selectedChip
            chip.chipBackgroundColor = if (chip == selectedChip) {
                ContextCompat.getColorStateList(this, R.color.primary)
            } else {
                ContextCompat.getColorStateList(this, R.color.surface_dark)
            }
        }
    }

    private fun setupFab() {
        binding.fabScan.setOnClickListener {
            scanAllApps()
        }
    }

    private fun loadApps() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            binding.emptyState.visibility = View.GONE

            try {
                allApps = withContext(Dispatchers.IO) {
                    appAnalyzer.analyzeAllApps()
                }
                
                filterApps("")
                
                if (allApps.isEmpty()) {
                    binding.emptyState.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "خطأ في تحميل التطبيقات: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun filterApps(query: String) {
        val filtered = allApps.filter { app ->
            val matchesSearch = app.appName.contains(query, ignoreCase = true) ||
                    app.packageName.contains(query, ignoreCase = true)
            
            val matchesFilter = when (currentFilter) {
                RiskFilter.ALL -> true
                RiskFilter.HIGH -> app.riskLevel == RiskLevel.HIGH
                RiskFilter.MEDIUM -> app.riskLevel == RiskLevel.MEDIUM
                RiskFilter.LOW -> app.riskLevel == RiskLevel.LOW
                RiskFilter.SAFE -> app.riskLevel == RiskLevel.SAFE
            }
            
            matchesSearch && matchesFilter
        }
        
        appsAdapter.submitList(filtered)
        
        if (filtered.isEmpty() && allApps.isNotEmpty()) {
            binding.emptyState.visibility = View.VISIBLE
        } else {
            binding.emptyState.visibility = View.GONE
        }
    }

    private fun scanAllApps() {
        val highRiskCount = allApps.count { it.riskLevel == RiskLevel.HIGH }
        val mediumRiskCount = allApps.count { it.riskLevel == RiskLevel.MEDIUM }
        
        val message = buildString {
            appendLine("نتائج الفحص:")
            appendLine("إجمالي التطبيقات: ${allApps.size}")
            appendLine("تطبيقات عالية الخطورة: $highRiskCount")
            appendLine("تطبيقات متوسطة الخطورة: $mediumRiskCount")
            appendLine()
            appendLine("هل تريد عرض التطبيقات عالية الخطورة؟")
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("فحص الأمان")
            .setMessage(message)
            .setPositiveButton("عرض") { _, _ ->
                currentFilter = RiskFilter.HIGH
                updateChipSelection(binding.chipHighRisk)
                filterApps(binding.searchView.query.toString())
            }
            .setNegativeButton("إغلاق", null)
            .show()
    }

    private fun openAppDetails(appInfo: AppInfo) {
        val intent = Intent(this, AppDetailsActivity::class.java).apply {
            putExtra(AppDetailsActivity.EXTRA_PACKAGE_NAME, appInfo.packageName)
        }
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            R.id.action_premium -> {
                startActivity(Intent(this, PremiumActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload apps when returning to refresh any changes
        if (allApps.isEmpty()) {
            loadApps()
        }
    }
}