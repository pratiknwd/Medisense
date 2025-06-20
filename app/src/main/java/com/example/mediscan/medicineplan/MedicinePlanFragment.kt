package com.example.mediscan.medicineplan

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediscan.BaseFragment
import com.example.mediscan.MainActivity
import com.example.mediscan.auth.PEF_USER_ID
import com.example.mediscan.auth.SHARED_PREF_NAME
import com.example.mediscan.databinding.FragmentMedicinePlanBinding
import com.example.mediscan.db.AppDatabase
import kotlinx.coroutines.launch

class MedicinePlanFragment : BaseFragment() {
    private var _binding: FragmentMedicinePlanBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MedicinePlanRecyclerAdapter
    private val medicinePlans: MutableList<MedicinePlanWithDetails> = mutableListOf()
    
    override fun onNavigationIconClick(iconTapped: View?) {
        
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = MedicinePlanRecyclerAdapter(medicinePlans, ::onActiveClicked, ::onEditClicked, ::onDeleteClicked)
        
    }
    
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.medicinePlanRV.apply {
            adapter = this@MedicinePlanFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        
        Log.d("9155881234", "onViewCreated")
    }


    fun onActiveClicked(planId: Int): Unit {
        Log.d("9155881234", "onActiveClicked for planId: $planId")
        showConfirmationDialog(planId) // Show confirmation dialog before blocking
    }

    private fun showConfirmationDialog(planId: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Inactivation")
            .setMessage("Are you sure you want to make this plan inactive?")
            .setPositiveButton("Yes") { dialog, _ ->
                blockMedicinePlan(planId)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    
    fun onEditClicked(planId: Int): Unit {
        Log.d("9155881234", "onEditClicked")
    }
    
    fun onDeleteClicked(planId: Int): Unit {
        showConfirmationDialogforDelete(planId)
        Log.d("9155881234", "onDeleteClicked")
    }

    private fun showConfirmationDialogforDelete(planId: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to Delete this plan?")
            .setPositiveButton("Yes") { dialog, _ ->
                DeleteMedicnePlan(planId)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    
    private fun loadData() {
        lifecycleScope.launch {
            val sharedPreferences = requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt(PEF_USER_ID, -1)  // Default value -1 if not found
            if (userId == -1) return@launch
            val database = AppDatabase.getDatabase(requireContext().applicationContext)
            val plans = database.medicineDao().getActiveMedicinePlansForUser(userId)
            medicinePlans.clear()
            medicinePlans.addAll(plans)
            adapter.notifyChanges()
        }
    }

    private fun blockMedicinePlan(planId: Int) {
        lifecycleScope.launch {
            val database = AppDatabase.getDatabase(requireContext().applicationContext)
            database.medicineDao().blockMedicinePlan(planId) // Call the DAO method to update status
            loadData() // Reload data to refresh the RecyclerView
        }
    }

    private fun DeleteMedicnePlan(planId: Int){
        lifecycleScope.launch {
            val database = AppDatabase.getDatabase(requireContext().applicationContext)
            database.medicineDao().delteMedicinePlan(planId)
            loadData()
        }
    }
    
    override fun onResume() {
        super.onResume()
        initToolbar((activity as MainActivity).toolbar, "Medicine Plan")
        loadData()
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMedicinePlanBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        const val FRAG_NAME = "MedicinePlanFragment"
        
        
        @JvmStatic
        fun newInstance(/*param1: String, param2: String*/) =
            MedicinePlanFragment().apply {
                arguments = Bundle().apply {
                    /*putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)*/
                }
            }
    }
}