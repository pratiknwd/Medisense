package com.example.mediscan.medicineplan.addmedicneplan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediscan.db.AppDatabase
import com.example.mediscan.db.dao.MedicineDetailsDao
import com.example.mediscan.db.dao.MedicinePlanDao
import com.example.mediscan.db.entity.MedicineDetails
import com.example.mediscan.db.entity.MedicinePlan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicinePlanViewModel(application: Application) : AndroidViewModel(application) {

    private val medicinePlanDao: MedicinePlanDao
    private val medicineDetailsDao: MedicineDetailsDao

    init {
        val db = AppDatabase.getDatabase(application)
        medicinePlanDao = db.medicinePlanDao()
        medicineDetailsDao = db.medicineDetailsDao()
    }

    fun insertMedicinePlanWithDetails(plan: MedicinePlan, details: List<MedicineDetails>) {
        viewModelScope.launch(Dispatchers.IO) {
            val planId = medicinePlanDao.insertMedicinePlan(plan).toInt()
            details.forEach {
                medicineDetailsDao.insertMedicineDetails(it.copy(planId = planId))
            }
        }
    }
}
