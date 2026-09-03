package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data Transfer Objects (DTOs) for communicating with the backend / ai-service API.
 */

@JsonClass(generateAdapter = true)
data class StudentDto(
  @Json(name = "id") val id: String,
  @Json(name = "name") val name: String,
  @Json(name = "tamil_name") val tamilName: String?,
  @Json(name = "age") val age: Int,
  @Json(name = "grade_class") val gradeClass: String,
  @Json(name = "section") val section: String,
  @Json(name = "roll_no") val rollNo: String,
  @Json(name = "gender") val gender: String,
  @Json(name = "blood_group") val bloodGroup: String,
  @Json(name = "attendance_percent") val attendancePercent: Int,
  @Json(name = "height_cm") val heightCm: Double,
  @Json(name = "weight_kg") val weightKg: Double,
  @Json(name = "symptoms") val symptoms: List<String> = emptyList(),
  @Json(name = "risk_level") val riskLevel: String,
  @Json(name = "priority_score") val priorityScore: Int,
  @Json(name = "has_pending_doctor_review") val hasPendingDoctorReview: Boolean,
  @Json(name = "last_screening_date") val lastScreeningDate: String
)

@JsonClass(generateAdapter = true)
data class ScreeningRequestDto(
  @Json(name = "student_id") val studentId: String,
  @Json(name = "height_cm") val heightCm: Double,
  @Json(name = "weight_kg") val weightKg: Double,
  @Json(name = "attendance_percent") val attendancePercent: Int,
  @Json(name = "symptoms") val symptoms: List<String>,
  @Json(name = "age") val age: Int
)

@JsonClass(generateAdapter = true)
data class RiskEvaluationDto(
  @Json(name = "student_id") val studentId: String,
  @Json(name = "bmi") val bmi: Double,
  @Json(name = "bmi_category") val bmiCategory: String,
  @Json(name = "priority_score") val priorityScore: Int,
  @Json(name = "risk_level") val riskLevel: String,
  @Json(name = "contributing_factors") val contributingFactors: List<String>,
  @Json(name = "requires_doctor_review") val requiresDoctorReview: Boolean
)

@JsonClass(generateAdapter = true)
data class DraftReportRequestDto(
  @Json(name = "screening_data") val screeningData: ScreeningRequestDto,
  @Json(name = "risk_evaluation") val riskEvaluation: RiskEvaluationDto
)

@JsonClass(generateAdapter = true)
data class DraftReportResponseDto(
  @Json(name = "student_id") val studentId: String,
  @Json(name = "clinical_summary_draft") val clinicalSummaryDraft: String,
  @Json(name = "suggested_actions") val suggestedActions: List<String>,
  @Json(name = "suggested_dietary_advice") val suggestedDietaryAdvice: String,
  @Json(name = "disclaimer") val disclaimer: String
)

@JsonClass(generateAdapter = true)
data class DoctorApprovalRequestDto(
  @Json(name = "student_id") val studentId: String,
  @Json(name = "doctor_name") val doctorName: String,
  @Json(name = "clinical_impression") val clinicalImpression: String,
  @Json(name = "prescription") val prescription: String,
  @Json(name = "diet_advice") val dietAdvice: String,
  @Json(name = "exercise_advice") val exerciseAdvice: String,
  @Json(name = "lab_tests") val labTests: String,
  @Json(name = "followup_date") val followupDate: String,
  @Json(name = "school_remarks") val schoolRemarks: String
)

@JsonClass(generateAdapter = true)
data class SyncResponseDto(
  @Json(name = "synced_records_count") val syncedRecordsCount: Int,
  @Json(name = "timestamp") val timestamp: String,
  @Json(name = "status") val status: String
)

@JsonClass(generateAdapter = true)
data class HealthCheckDto(
  @Json(name = "status") val status: String,
  @Json(name = "service") val service: String
)
