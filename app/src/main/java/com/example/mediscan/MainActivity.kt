package com.example.mediscan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mediscan.auth.PEF_USER_ID
import com.example.mediscan.auth.SHARED_PREF_NAME
import com.example.mediscan.auth.SignInActivity
import com.example.mediscan.databinding.ActivityMainBinding
import com.example.mediscan.db.AppDatabase
import com.example.mediscan.db.dao.DocumentDao
import com.example.mediscan.db.dao.DocumentTypeDao
import com.example.mediscan.db.dao.MedicinePlanDao
import com.example.mediscan.db.dao.ReportDao
import com.example.mediscan.db.dao.ReportTypeDao
import com.example.mediscan.db.dao.UserDao
import com.example.mediscan.db.dao.UserFoodTimingDao
import com.example.mediscan.db.entity.Document
import com.example.mediscan.db.entity.DocumentType
import com.example.mediscan.db.entity.User
import com.example.mediscan.db.entity.UserFoodTiming
import com.example.mediscan.full_report.views.FullReportFragment
import com.example.mediscan.my_reports.MyReportsFragment
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// https://ai.google.dev/api?lang=android
// https://developers.google.com/maps/documentation/android-sdk/secrets-gradle-plugin#groovy

class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var viewModel: SharedViewModel
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var medicinePlanDao: MedicinePlanDao
    private lateinit var userFoodTimingDao: UserFoodTimingDao
    private lateinit var documentDao: DocumentDao
    private lateinit var documentTypeDao: DocumentTypeDao
    private lateinit var reportDao: ReportDao
    private lateinit var reportTypeDao: ReportTypeDao
    
    
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
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
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
        
        db = AppDatabase.getDatabase(applicationContext)
        
        // Initialize DAOs
        userDao = db.userDao()
        medicinePlanDao = db.medicinePlanDao()
        userFoodTimingDao = db.userFoodTimingDao()
        documentDao = db.documentDao()
        documentTypeDao = db.documentTypeDao()
        reportDao = db.reportDao()
        reportTypeDao = db.reportTypeDao()
        
        // Insert dummy data
        // insertDummyData()
        
        // Retrieve dummy data
        lifecycleScope.launch(Dispatchers.IO) {
            val users = userDao.getAllUsers()
            for (user in users) {
                Log.d("9155881234", "User: ${user.userName}, ${user.email}")
            }
        }
        
    }
    
    private fun insertDummyData() {
        lifecycleScope.launch(Dispatchers.IO) {
            
            // 1️⃣ Insert User First
            val user = User(userId = 1, userName = "John Doe", userAge = 25, sex = "Male", email = "john@example.com", password = "password123")
            userDao.insertUser(user)
            
            // 2️⃣ Insert Document Type First (Before Document)
            val documentType = DocumentType(documentTypeId = 1, documentName = "Prescription")
            documentTypeDao.insertDocumentType(documentType)
            
            // 3️⃣ Insert Document After Ensuring User and DocumentType Exist
            val document = Document(documentId = 1, userId = 1, documentTypeId = 1, documentLink = "https://")
            documentDao.insertDocument(document)
            
            // 4️⃣ Insert Medicine Plan (Depends on User)
//            val medicinePlan = MedicinePlan(planId = 1, userId = 1, status = true, medicineName = "Paracetamol", dose = "500mg", frequency = "1-0-1", duration = "7 days", times = "Morning, Night", foodInstruction = "After food", startDate = "2025-03-07")
//            medicinePlanDao.insertMedicinePlan(medicinePlan)
            
            // 5️⃣ Insert User Food Timing (Depends on User)
            val userFoodTiming = UserFoodTiming(itineraryId = 1, userId = 1, breakfastTime = "8:00 AM", lunchTime = "1:00 PM", dinnerTime = "8:00 PM")
            userFoodTimingDao.insertUserFoodTiming(userFoodTiming)
            
            // 6️⃣ Insert Report Type First (Before Report)
            /*val reportType = ReportType(reportTypeId = 1, userId = 1, documentId = 1, reportTypeName = "Blood Test")
            reportTypeDao.insertReportType(reportType)*/
            
            /*// 7️⃣ Insert Report After Ensuring User and Document Exist
            val report = Report(reportId = 1, userId = 1, documentId = 1, reportTypeId = 1, testName = "Blood Sugar", result = 90, unit = "mg/dL", upperLimit = 140, lowerLimit = 70)
            reportDao.insertReport(report)*/
            
            println("✅ Dummy data inserted successfully!")
        }
    }
    
    
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> loadFragment(supportFragmentManager, HomeFragment.newInstance(), HomeFragment.FRAG_NAME)
            R.id.nav_pres -> loadFragment(supportFragmentManager, PrescriptionFragment.newInstance(), PrescriptionFragment.FRAG_NAME)
            R.id.nav_medicine -> loadFragment(supportFragmentManager, MedicineScanFragment.newInstance(), MedicineScanFragment.FRAG_NAME)
            R.id.nav_profile -> loadFragment(supportFragmentManager, ProfileFragment.newInstance(), ProfileFragment.FRAG_NAME)
            R.id.nav_scan_report -> loadFragment(supportFragmentManager, ScanReportFragment.newInstance(), ScanReportFragment.FRAG_NAME)
            R.id.nav_my_reports -> loadFragment(supportFragmentManager, MyReportsFragment.newInstance(), MyReportsFragment.FRAG_NAME)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    
    fun openFullReportFragment(reportTypeId: Int) {
        val fragment = FullReportFragment.newInstance(reportTypeId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frag_container, fragment, FullReportFragment.FRAG_NAME)
            .addToBackStack(FullReportFragment.FRAG_NAME)
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