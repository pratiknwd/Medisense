package com.example.mediscan.db.dataclass

data class ReportWithModifiedLimits(
    val reportId: Int,
    val userId: Int,
    val reportTypeId: Int,
    val testName: String?,
    val testValue: Float?,
    val unit: String?,
    val upperLimit: Float?,
    val lowerLimit: Float?,
    val explanation: String?,
    val modifiedLowerLimit: Float?,
    val bioReferenceInterval: String?,
    val modifiedUpperLimit: Float?
)