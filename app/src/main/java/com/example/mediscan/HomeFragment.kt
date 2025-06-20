package com.example.mediscan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mediscan.databinding.FragmentHomeBinding
import com.example.mediscan.report.my_reports.MyReportsFragment

class HomeFragment : BaseFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onNavigationIconClick(iconTapped: View?) {
    
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("9155881234", "created $FRAG_NAME")
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goToPrescriptionBtn.setOnClickListener {
            (requireActivity() as MainActivity).loadFragment(requireActivity().supportFragmentManager, PrescriptionFragment.newInstance(), PrescriptionFragment.FRAG_NAME)
        }

        binding.goToReportAnalyser.setOnClickListener{
            (requireActivity() as MainActivity).loadFragment(requireActivity().supportFragmentManager, MyReportsFragment.newInstance(), MyReportsFragment.FRAG_NAME)
        }
        
        binding.goToMedicineBtn.setOnClickListener {
            (requireActivity() as MainActivity).loadFragment(requireActivity().supportFragmentManager, MedicineScanFragment.newInstance(), MedicineScanFragment.FRAG_NAME)
        }
    }
    
    override fun onResume() {
        super.onResume()
        initToolbar((activity as MainActivity).toolbar, FRAG_NAME)
    }
    
    override fun onDestroy() {
        Log.d("9155881234", "destroyed $FRAG_NAME")
        super.onDestroy()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        const val FRAG_NAME = "Home"
        
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}