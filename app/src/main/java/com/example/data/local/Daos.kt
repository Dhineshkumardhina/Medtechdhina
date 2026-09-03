package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.model.RiskLevel
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {

  // --- CRUD Operations ---

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertStudent(student: StudentEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertStudents(students: List<StudentEntity>)

  @Update
  suspend fun updateStudent(student: StudentEntity): Int

  @Delete
  suspend fun deleteStudent(student: StudentEntity): Int

  @Query("DELETE FROM students WHERE id = :studentId")
  suspend fun deleteStudentById(studentId: String): Int

  @Query("DELETE FROM students")
  suspend fun clearAllStudents()

  // --- Dashboard & Analytical Queries ---

  @Query("SELECT * FROM students WHERE id = :studentId LIMIT 1")
  fun getStudentById(studentId: String): Flow<StudentEntity?>

  @Query("SELECT * FROM students WHERE id = :studentId LIMIT 1")
  suspend fun getStudentByIdDirect(studentId: String): StudentEntity?

  @Query("SELECT * FROM students ORDER BY priorityScore DESC")
  fun getAllStudentsSortedByPriority(): Flow<List<StudentEntity>>

  @Query("SELECT * FROM students WHERE gradeClass = :gradeClass AND section = :section ORDER BY rollNo ASC")
  fun getStudentsByClass(gradeClass: String, section: String): Flow<List<StudentEntity>>

  @Query("SELECT * FROM students WHERE riskLevel IN (:riskLevels) ORDER BY priorityScore DESC")
  fun getTriageQueue(riskLevels: List<RiskLevel> = listOf(RiskLevel.HIGH, RiskLevel.MEDIUM)): Flow<List<StudentEntity>>

  @Query("SELECT * FROM students WHERE hasPendingDoctorReview = 1 ORDER BY priorityScore DESC")
  fun getStudentsAwaitingReview(): Flow<List<StudentEntity>>

  @Query("SELECT COUNT(*) FROM students")
  fun getTotalStudentCount(): Flow<Int>

  @Query("SELECT COUNT(*) FROM students WHERE riskLevel = :riskLevel")
  fun getStudentCountByRiskLevel(riskLevel: RiskLevel): Flow<Int>

  @Query("SELECT AVG(attendancePercent) FROM students")
  fun getAverageAttendance(): Flow<Double?>
}

// Typealias so existing callers of ScreeningDao remain fully compatible
typealias ScreeningDao = HealthRecordDao

/**
 * HealthRecordDao provides complete CRUD operations and queries for student health records
 * (screening records, vitals history, and mobile digital health passport timelines).
 */
@Dao
interface HealthRecordDao {

  // --- CRUD Operations ---

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRecord(record: ScreeningRecordEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRecords(records: List<ScreeningRecordEntity>)

  // Compatibility aliases for screening terminology
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertScreening(screening: ScreeningRecordEntity): Long = insertRecord(screening)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertScreenings(screenings: List<ScreeningRecordEntity>) = insertRecords(screenings)

  @Update
  suspend fun updateRecord(record: ScreeningRecordEntity): Int

  @Update
  suspend fun updateScreening(screening: ScreeningRecordEntity): Int = updateRecord(screening)

  @Delete
  suspend fun deleteRecord(record: ScreeningRecordEntity): Int

  @Delete
  suspend fun deleteScreening(screening: ScreeningRecordEntity): Int = deleteRecord(screening)

  @Query("DELETE FROM screening_records WHERE id = :recordId")
  suspend fun deleteRecordById(recordId: String): Int

  @Query("DELETE FROM screening_records WHERE id = :screeningId")
  suspend fun deleteScreeningById(screeningId: String): Int = deleteRecordById(screeningId)

  // --- Health Passport & Dashboard Queries ---

  /**
   * Retrieves recent health records for a given student ID, ordered by timestamp (newest first).
   * Drives the Mobile Digital Health Passport and timeline views.
   */
  @Query("SELECT * FROM screening_records WHERE studentId = :studentId ORDER BY recordedTimestamp DESC")
  fun getRecentHealthRecordsByStudentId(studentId: String): Flow<List<ScreeningRecordEntity>>

  /**
   * Overloaded query supporting a customizable limit for compact dashboard widgets.
   */
  @Query("SELECT * FROM screening_records WHERE studentId = :studentId ORDER BY recordedTimestamp DESC LIMIT :limit")
  fun getRecentHealthRecordsByStudentId(studentId: String, limit: Int): Flow<List<ScreeningRecordEntity>>

  @Query("SELECT * FROM screening_records WHERE studentId = :studentId ORDER BY recordedTimestamp DESC LIMIT 1")
  fun getLatestScreeningByStudentId(studentId: String): Flow<ScreeningRecordEntity?>

  @Query("SELECT * FROM screening_records ORDER BY recordedTimestamp DESC")
  fun getAllScreenings(): Flow<List<ScreeningRecordEntity>>

  @Query("SELECT * FROM screening_records WHERE studentId = :studentId ORDER BY recordedTimestamp DESC")
  fun getScreeningsForStudent(studentId: String): Flow<List<ScreeningRecordEntity>>

  @Query("SELECT * FROM screening_records WHERE riskLevel = :riskLevel ORDER BY recordedTimestamp DESC")
  fun getScreeningsByRiskLevel(riskLevel: RiskLevel): Flow<List<ScreeningRecordEntity>>

  // --- Offline Synchronization Queries ---

  @Query("SELECT * FROM screening_records WHERE isSynced = 0 ORDER BY recordedTimestamp ASC")
  suspend fun getPendingUnsyncedScreenings(): List<ScreeningRecordEntity>

  @Query("SELECT COUNT(*) FROM screening_records WHERE isSynced = 0")
  fun getUnsyncedCount(): Flow<Int>

  @Query("UPDATE screening_records SET isSynced = 1 WHERE id = :recordId")
  suspend fun markScreeningAsSynced(recordId: String)

  @Query("UPDATE screening_records SET isSynced = 1 WHERE id IN (:recordIds)")
  suspend fun markScreeningsAsSynced(recordIds: List<String>)
}

@Dao
interface DoctorReportDao {

  // --- CRUD Operations ---

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertReport(report: DoctorReportEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertReports(reports: List<DoctorReportEntity>)

  @Update
  suspend fun updateReport(report: DoctorReportEntity): Int

  @Delete
  suspend fun deleteReport(report: DoctorReportEntity): Int

  @Query("DELETE FROM doctor_reports WHERE id = :reportId")
  suspend fun deleteReportById(reportId: String): Int

  // --- Dashboard & Timeline Queries ---

  /**
   * Retrieves recent approved physician reports for a student (e.g. for parent dashboard / timeline).
   */
  @Query("SELECT * FROM doctor_reports WHERE studentId = :studentId AND isApproved = 1 ORDER BY approvedTimestamp DESC LIMIT :limit")
  fun getRecentApprovedReportsByStudentId(studentId: String, limit: Int = 5): Flow<List<DoctorReportEntity>>

  @Query("SELECT * FROM doctor_reports WHERE studentId = :studentId ORDER BY approvedTimestamp DESC LIMIT 1")
  fun getLatestReportForStudent(studentId: String): Flow<DoctorReportEntity?>

  @Query("SELECT * FROM doctor_reports WHERE isApproved = 1 ORDER BY approvedTimestamp DESC")
  fun getAllApprovedReports(): Flow<List<DoctorReportEntity>>

  @Query("SELECT * FROM doctor_reports WHERE isApproved = 0 ORDER BY id DESC")
  fun getPendingDraftReports(): Flow<List<DoctorReportEntity>>

  // --- Offline Synchronization Queries ---

  @Query("SELECT * FROM doctor_reports WHERE isSynced = 0")
  suspend fun getUnsyncedDoctorReports(): List<DoctorReportEntity>

  @Query("UPDATE doctor_reports SET isSynced = 1 WHERE id = :reportId")
  suspend fun markReportAsSynced(reportId: String)
}
