package com.example.mediscan.db.dataclass

data class ReportWithModifiedLimits(
    val id: Int,
    val reportTypeId: Int,
    val testValue: Float,
    val lowerLimit: Float?,
    val upperLimit: Float?,
    val modifiedLowerLimit: Float?,
    val modifiedUpperLimit: Float?
)