package com.example.mediscan

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mediscan.auth.PEF_USER_ID
import com.example.mediscan.auth.PEF_USER_NAME
import com.example.mediscan.auth.SHARED_PREF_NAME
import com.example.mediscan.auth.SignInActivity
import com.example.mediscan.databinding.ActivityMainBinding
import com.example.mediscan.db.AppDatabase
import com.example.mediscan.medicineplan.addmedicneplan.AddMedicinePlanFragment
import com.example.mediscan.db.entity.MedicinePlan
import com.example.mediscan.medicineplan.MedicinePlanFragment
import com.example.mediscan.report.SmartReportFragment
import com.example.mediscan.report.my_reports.MyReportsFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import kotlinx.coroutines.launch

// https://ai.google.dev/api?lang=android
// https://developers.google.com/maps/documentation/android-sdk/secrets-gradle-plugin#groovy

class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var viewModel: SharedViewModel
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        
        // View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        
        // Set up Toolbar
        setSupportActionBar(binding.toolbar)
        
        // Set up Drawer Toggle
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.open_drawer, R.string.close_drawer)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toolbar = binding.toolbar.apply {
            setTitleTextColor(resources.getColor(R.color.white, theme))
        }
        
        binding.navView.setNavigationItemSelectedListener(this)
        
        if (savedInstanceState == null) {
            loadFragment(supportFragmentManager, HomeFragment(), HomeFragment.FRAG_NAME)
            binding.navView.setCheckedItem(R.id.nav_home)
        }
        
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            checkCameraPermission()
        }
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0) // Get the first header view
        val userNameTextView = headerView.findViewById<TextView>(R.id.tvUserName)
        
        // Fetch logged-in user name (Replace this with your actual logic)
        val loggedUserName = getLoggedUserName(applicationContext)
        userNameTextView.text = "Welcome $loggedUserName"
        
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val list = db.reportDao().getReportsByType(0)
            Log.d("RoomTest", "Number of items: $list")
        }
    }
    
    
    private fun getLoggedUserName(context: Context): String {
        val sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val username = sharedPref.getString(PEF_USER_NAME, "Guest") ?: "Guest"
        return username.replaceFirstChar { it.uppercaseChar() }
    }
    
    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted, do nothing
            }
            
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // Show explanation and request permission
                showPermissionRationaleDialog()
            }
            
            else -> {
                // Request permission
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
    
    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Camera Permission Needed")
            .setMessage("This app requires camera access to take photos.")
            .setPositiveButton("Allow") { _, _ -> requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA) }
            .setNegativeButton("Deny", null)
            .show()
    }
    
    private val requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Camera permission not granted", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> loadFragment(supportFragmentManager, HomeFragment.newInstance(), HomeFragment.FRAG_NAME)
            R.id.nav_pres -> loadFragment(supportFragmentManager, PrescriptionFragment.newInstance(), PrescriptionFragment.FRAG_NAME)
            R.id.nav_medicine -> loadFragment(supportFragmentManager, MedicineScanFragment.newInstance(), MedicineScanFragment.FRAG_NAME)
            R.id.nav_scan_report -> loadFragment(supportFragmentManager, ScanReportFragment.newInstance(), ScanReportFragment.FRAG_NAME)
            R.id.nav_my_reports -> loadFragment(supportFragmentManager, MyReportsFragment.newInstance(), MyReportsFragment.FRAG_NAME)
            R.id.nav_create_medicine_plan -> loadFragment(supportFragmentManager, AddMedicinePlanFragment.newInstance(), AddMedicinePlanFragment.FRAG_NAME)
            R.id.nav_medicine_plan -> loadFragment(supportFragmentManager, MedicinePlanFragment.newInstance(), MedicinePlanFragment.FRAG_NAME)


            R.id.nav_logout -> {
                logoutUser(this)
                return true
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    
    fun openFullReportFragment(reportTypeId: Int) {
        val fragment = SmartReportFragment.newInstance(reportTypeId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frag_container, fragment, SmartReportFragment.FRAG_NAME)
            .addToBackStack(SmartReportFragment.FRAG_NAME)
            .commit()
    }
    
    
    fun loadFragment(fragmentManager: FragmentManager, fragment: Fragment, tag: String) {
        fragmentManager.beginTransaction()
            .replace(R.id.frag_container, fragment, tag)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }
    
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
    
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        
        val count = supportFragmentManager.backStackEntryCount
        if (count == 1) {
            finish()
            return
        }
        super.onBackPressed()
    }
    
    fun logoutUser(context: Context) {
        val sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove(PEF_USER_ID)
            apply()
        }
        val signInActivity = Intent(this, SignInActivity::class.java)
        startActivity(signInActivity)
        finish()
    }
}