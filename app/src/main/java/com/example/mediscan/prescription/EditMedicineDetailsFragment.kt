package com.example.mediscan.prescription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.mediscan.BaseFragment
import com.example.mediscan.MainActivity
import com.example.mediscan.SharedViewModel
import com.example.mediscan.databinding.FragmentEditMedicineDetailsBinding

class EditMedicineDetailsFragment : BaseFragment() {
    private var _binding: FragmentEditMedicineDetailsBinding? = null
    private val binding get() = _binding!!
    private val indexToEdit by lazy {
        requireArguments().getInt("indexToEdit")
    }
    private lateinit var sharedViewModel: SharedViewModel
    
    override fun onNavigationIconClick(iconTapped: View?) {
    
    }
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEditMedicineDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val model = sharedViewModel.prescriptionModel[indexToEdit]
        binding.apply {
            medicineName.setText(model.medicineName)
            dose.setText(model.dose)
            frequency.setText(model.frequency)
            duration.setText(model.duration)
            foodInstruction.setText(model.foodInstruction)
            times.setText(model.times.joinToString(","))
        }
        
        binding.saveChanges.setOnClickListener { saveChanges() }
    }
    
    private fun saveChanges() {
        val model = sharedViewModel.prescriptionModel[indexToEdit]
        model.apply {
            medicineName = binding.medicineName.text.toString()
            dose = binding.dose.text.toString()
            frequency = binding.frequency.text.toString()
            duration = binding.duration.text.toString()
            foodInstruction = binding.foodInstruction.text.toString()
            times = binding.times.text.toString().split(",").map { it.trim() }
        }
        requireActivity().onBackPressed()
    }
    
    override fun onResume() {
        super.onResume()
        initToolbar((activity as MainActivity).toolbar, "Edit Medicine Details")
    }
    
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
    
    companion object {
        const val FRAG_NAME = "EditMedicineDetailsFragment"
        
        @JvmStatic
        fun newInstance(indexToEdit: Int) = EditMedicineDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt("indexToEdit", indexToEdit)
            }
        }
    }
}