package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.model.RiskLevel
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * Cached Student Entity representing demographic and health vitals summary.
 */
@Entity(tableName = "students")
data class StudentEntity(
  @PrimaryKey val id: String,
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
  val symptoms: List<String>,
  val riskLevel: RiskLevel,
  val priorityScore: Int,
  val hasPendingDoctorReview: Boolean,
  val lastScreeningDate: String,
  val nextFollowupDate: String?,
  val lastSyncedTimestamp: Long = System.currentTimeMillis()
)

/**
 * Health Screening record captured during school camps.
 * Includes sync status flag for offline-first support.
 */
@Entity(tableName = "screening_records")
data class ScreeningRecordEntity(
  @PrimaryKey val id: String,
  val studentId: String,
  val heightCm: Double,
  val weightKg: Double,
  val temperatureC: Double,
  val symptoms: List<String>,
  val attendancePercent: Int,
  val priorityScore: Int,
  val riskLevel: RiskLevel,
  val recordedByRole: String,
  val recordedTimestamp: String,
  val isSynced: Boolean = false,
  val retryCount: Int = 0
)

/**
 * Local cache of physician clinical impressions, prescriptions, and notes.
 */
@Entity(tableName = "doctor_reports")
data class DoctorReportEntity(
  @PrimaryKey val id: String,
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
  val isApproved: Boolean,
  val approvedTimestamp: String?,
  val isSynced: Boolean = true
)

/**
 * Type converters for Room to handle List<String> and Enums.
 */
class HealthTypeConverters {
  private val moshi = Moshi.Builder().build()
  private val listType = Types.newParameterizedType(List::class.java, String::class.java)
  private val adapter = moshi.adapter<List<String>>(listType)

  @TypeConverter
  fun fromStringList(list: List<String>?): String {
    return adapter.toJson(list ?: emptyList())
  }

  @TypeConverter
  fun toStringList(json: String?): List<String> {
    return if (json.isNullOrEmpty()) emptyList() else adapter.fromJson(json) ?: emptyList()
  }

  @TypeConverter
  fun fromRiskLevel(level: RiskLevel?): String {
    return level?.name ?: RiskLevel.LOW.name
  }

  @TypeConverter
  fun toRiskLevel(name: String?): RiskLevel {
    return try {
      RiskLevel.valueOf(name ?: RiskLevel.LOW.name)
    } catch (_: Exception) {
      RiskLevel.LOW
    }
  }
}
