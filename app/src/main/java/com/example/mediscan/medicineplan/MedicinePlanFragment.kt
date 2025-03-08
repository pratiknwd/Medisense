//package com.example.mediscan.medicineplan
//
//import android.content.Context
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.content.ContentProviderCompat.requireContext
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.mediscan.BaseFragment
//import com.example.mediscan.auth.PEF_USER_ID
//import com.example.mediscan.auth.SHARED_PREF_NAME
//import com.example.mediscan.db.AppDatabase
//import com.example.mediscan.db.dao.MedicineDetailsDao
//import com.example.mediscan.db.dao.MedicinePlanDao
//import com.example.mediscan.db.entity.MedicinePlan
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class MedicinePlanFragment : Fragment() {
//
//    private lateinit var binding: FragmentMedicinePlanBinding
//    private lateinit var medicinePlanDao: MedicinePlanDao
//    private lateinit var medicineDetailsDao: MedicineDetailsDao
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentMedicinePlanBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        medicinePlanDao = AppDatabase.getDatabase(requireContext()).medicinePlanDao()
//        medicineDetailsDao = AppDatabase.getDatabase(requireContext()).medicineDetailsDao()
//
//        fetchMedicinePlans()
//    }
//
//    private fun getUserId(context: Context): Int {
//        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
//        return sharedPreferences.getInt(PEF_USER_ID, -1)  // Default value -1 if not found
//    }
//
//    private fun fetchMedicinePlans() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val userId = getUserId(requireContext()) // Fetch user ID from preferences
//            val allPlans = medicinePlanDao.getMedicinePlansByUser(userId)
//
//            val activePlans = allPlans.filter { it.status }
//            val inactivePlans = allPlans.filter { !it.status }
//
//            withContext(Dispatchers.Main) {
//                setupRecyclerView(activePlans, inactivePlans)
//            }
//        }
//    }
//
//    private fun setupRecyclerView(activePlans: List<MedicinePlan>, inactivePlans: List<MedicinePlan>) {
//        binding.recyclerViewActive.layoutManager = LinearLayoutManager(requireContext())
//        binding.recyclerViewInactive.layoutManager = LinearLayoutManager(requireContext())
//
//        binding.recyclerViewActive.adapter = MedicinePlanAdapter(activePlans) { plan ->
//            openMedicineDetails(plan.planId)
//        }
//        binding.recyclerViewInactive.adapter = MedicinePlanAdapter(inactivePlans) { plan ->
//            openMedicineDetails(plan.planId)
//        }
//    }
//
//    private fun openMedicineDetails(planId: Int) {
//        val action = MedicinePlanFragmentDirections.actionMedicinePlanToMedicineDetails(planId)
//        findNavController().navigate(action)
//    }
//}
