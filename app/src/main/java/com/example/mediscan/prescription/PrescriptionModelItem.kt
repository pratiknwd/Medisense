package com.example.mediscan.prescription


import com.google.gson.annotations.SerializedName

data class PrescriptionModelItem(
    var dose: String,
    var duration: String,
    @SerializedName("food_instruction")
    var foodInstruction: String,
    var frequency: String,
    @SerializedName("medicine_name")
    var medicineName: String,
    var times: List<String>,
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