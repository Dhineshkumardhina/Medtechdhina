package com.example.model

import java.util.UUID

enum class UserRole(val displayName: String, val tamilName: String) {
  TEACHER("Teacher", "ஆசிரியர்"),
  DOCTOR("Doctor", "மருத்துவர்"),
  PARENT("Parent", "பெற்றோர்"),
  STUDENT("Student", "மாணவர்"),
  ADMIN("School Admin", "நிர்வாகி")
}

enum class RiskLevel(val label: String, val tamilLabel: String) {
  HIGH("HIGH RISK", "அதிக ஆபத்து"),
  MEDIUM("MEDIUM RISK", "நடுத்தர ஆபத்து"),
  LOW("LOW RISK", "குறைந்த ஆபத்து")
}

enum class AppLanguage {
  ENGLISH,
  TAMIL
}

data class Student(
  val id: String = UUID.randomUUID().toString(),
  val name: String,
  val tamilName: String,
  val age: Int,
  val gradeClass: String,
  val section: String,
  val rollNo: String,
  val gender: String,
  val bloodGroup: String,
  val attendancePercent: Int,
  val heightCm: Double,
  val weightKg: Double,
  val symptoms: List<String> = emptyList(),
  val riskLevel: RiskLevel = RiskLevel.LOW,
  val priorityScore: Int = 20,
  val hasPendingDoctorReview: Boolean = false,
  val lastScreeningDate: String = "2026-06-15",
  val nextFollowupDate: String? = null
) {
  val bmi: Double
    get() {
      val heightM = heightCm / 100.0
      return if (heightM > 0) Math.round((weightKg / (heightM * heightM)) * 10.0) / 10.0 else 0.0
    }

  val bmiCategory: String
    get() = when {
      bmi < 15.5 -> "Underweight"
      bmi <= 21.0 -> "Normal"
      else -> "Overweight"
    }
}

data class HealthScreeningRecord(
  val id: String = UUID.randomUUID().toString(),
  val studentId: String,
  val heightCm: Double,
  val weightKg: Double,
  val temperatureC: Double = 37.0,
  val symptoms: List<String>,
  val attendancePercent: Int,
  val recordedByRole: String,
  val timestamp: String,
  val isOfflineSynced: Boolean = true
)

data class RiskAssessment(
  val id: String = UUID.randomUUID().toString(),
  val studentId: String,
  val priorityScore: Int,
  val riskLevel: RiskLevel,
  val contributingFactors: List<String>,
  val aiSummaryDraft: String,
  val suggestedAction: String,
  val status: String = "Pending Doctor Review"
)

data class DoctorReport(
  val id: String = UUID.randomUUID().toString(),
  val studentId: String,
  val doctorName: String,
  val doctorSpecialty: String,
  val clinicalImpression: String,
  val prescription: String,
  val dietAdvice: String,
  val exerciseAdvice: String,
  val labTestsRecommended: String,
  val followupDate: String,
  val schoolRemarks: String,
  val isApproved: Boolean = false,
  val approvedTimestamp: String? = null
)

data class HealthTimelineEvent(
  val id: String = UUID.randomUUID().toString(),
  val date: String,
  val title: String,
  val tamilTitle: String,
  val description: String,
  val badge: String,
  val isDoctorApproved: Boolean = false
)

data class NotificationAlert(
  val id: String = UUID.randomUUID().toString(),
  val title: String,
  val message: String,
  val targetRole: UserRole,
  val timestamp: String,
  val isUrgent: Boolean = false,
  var isRead: Boolean = false
)

data class StudentChallenge(
  val id: String,
  val title: String,
  val tamilTitle: String,
  val icon: String,
  var isCompleted: Boolean = false,
  val points: Int = 10
)
