package com.example.mediscan.prescription


import com.google.gson.annotations.SerializedName

data class PrescriptionModelItem(
    val dose: String,
    val duration: String,
    @SerializedName("food_instruction")
    val foodInstruction: String,
    val frequency: String,
    @SerializedName("medicine_name")
    val medicineName: String,
    val times: List<String>,
) {
    override fun toString(): String {
        return """
                Medicine Name   : $medicineName
                Dose            : $dose
                Frequency       : $frequency
                Duration        : $duration
                Food Instruction: $foodInstruction
                Times           : ${times.joinToString(", ")}
        """.trimIndent()
    }
}