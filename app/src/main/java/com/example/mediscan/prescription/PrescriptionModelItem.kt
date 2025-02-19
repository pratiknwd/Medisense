package com.example.mediscan.prescription


import com.google.gson.annotations.SerializedName

data class PrescriptionModelItem(
    val dose: String,
    val duration: String,
    val frequency: String,
    @SerializedName("medicine_name")
    val medicineName: String,
    val times: List<String>
)