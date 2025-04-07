package com.example.mediscan.medicineplan

import androidx.room.Embedded
import androidx.room.Relation
import com.example.mediscan.db.entity.MedicineDetails
import com.example.mediscan.db.entity.MedicinePlan

data class MedicinePlanWithDetails(
    @Embedded val plan: MedicinePlan,
    @Relation(
        parentColumn = "planId",
        entityColumn = "planId"
    )
    val medicineDetails: List<MedicineDetails>,
)
