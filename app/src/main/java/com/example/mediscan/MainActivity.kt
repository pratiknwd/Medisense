package com.example.mediscan

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.mediscan.databinding.ActivityMainBinding
import com.example.mediscan.full_report.views.FullReportFragment
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

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
    }
    
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> loadFragment(supportFragmentManager, HomeFragment.newInstance(), HomeFragment.FRAG_NAME)
            R.id.nav_pres -> loadFragment(supportFragmentManager, PrescriptionFragment.newInstance(), PrescriptionFragment.FRAG_NAME)
            R.id.nav_medicine -> loadFragment(supportFragmentManager, MedicineScanFragment.newInstance(), MedicineScanFragment.FRAG_NAME)
            R.id.nav_profile -> loadFragment(supportFragmentManager, ProfileFragment.newInstance(), ProfileFragment.FRAG_NAME)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    
    fun openFullReportFragment() {
        val fragment = FullReportFragment.newInstance()
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
}