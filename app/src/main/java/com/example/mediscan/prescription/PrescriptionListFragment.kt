package com.example.mediscan.prescription

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediscan.BaseFragment
import com.example.mediscan.MainActivity
import com.example.mediscan.R
import com.example.mediscan.SharedViewModel
import com.example.mediscan.databinding.FragmentPrescriptionListBinding

class PrescriptionListFragment : BaseFragment() {
    private var _binding: FragmentPrescriptionListBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var adapter: PrescriptionEditableRV
    
    
    override fun onNavigationIconClick(iconTapped: View?) {
    
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        adapter = PrescriptionEditableRV(sharedViewModel.prescriptionModel, ::showEditDeleteOptions)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPrescriptionListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.prescriptionEditableList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@PrescriptionListFragment.adapter
        }
        
        binding.btnSaveMedicines.setOnClickListener {
            sharedViewModel.insertPrescriptionData()
            sharedViewModel.prescriptionModel.clear()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
    
    private fun showEditDeleteOptions(index: Int) {
        val options = arrayOf("Edit Medicine", "Delete Medicine")
        AlertDialog
            .Builder(context)
            .setTitle("Actions")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> onEdit(index)
                    1 -> onDelete(index)
                }
            }
            .show()
    }
    
    private fun onEdit(index: Int) {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.frag_container, EditMedicineDetailsFragment.newInstance(index))
            .addToBackStack(null)
            .commit()
    }
    
    private fun onDelete(index: Int) {
        sharedViewModel.prescriptionModel.removeAt(index)
        adapter.notifyItemRemoved(index)
    }
    
    override fun onResume() {
        super.onResume()
        initToolbar((activity as MainActivity).toolbar, "Prescription List")
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        const val FRAG_NAME = "PrescriptionListFragment"
        
        @JvmStatic
        fun newInstance() = PrescriptionListFragment()
    }
}