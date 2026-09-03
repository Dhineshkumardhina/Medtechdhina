package com.example.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit interface defining endpoints for communication with the EduHealth backend service.
 */
interface EduHealthApiService {

  /**
   * Health check / ping endpoint
   */
  @GET("health")
  suspend fun getHealthStatus(): Response<HealthCheckDto>

  /**
   * Fetches all registered students, optionally filtered by class/section.
   */
  @GET("api/v1/students")
  suspend fun getStudents(
    @Query("grade") grade: String? = null,
    @Query("section") section: String? = null
  ): Response<List<StudentDto>>

  /**
   * Fetches single student profile by ID
   */
  @GET("api/v1/students/{id}")
  suspend fun getStudentById(
    @Path("id") studentId: String
  ): Response<StudentDto>

  /**
   * Sends pediatric screening vitals to the AI Risk Scoring Engine.
   * Returns calculated BMI, risk level, triage priority score (0-100), and flags.
   */
  @POST("api/v1/risk/calculate")
  suspend fun calculateRiskScore(
    @Body request: ScreeningRequestDto
  ): Response<RiskEvaluationDto>

  /**
   * Requests an LLM-assisted (Gemini) clinical summary draft for physician review.
   */
  @POST("api/v1/reports/draft")
  suspend fun generateReportDraft(
    @Body request: DraftReportRequestDto
  ): Response<DraftReportResponseDto>

  /**
   * Submits a finalized, signed doctor report to update the student's longitudinal record.
   */
  @POST("api/v1/doctor/approve")
  suspend fun submitDoctorReport(
    @Body request: DoctorApprovalRequestDto
  ): Response<SyncResponseDto>

  /**
   * Offline sync endpoint: uploads batches of locally collected screening records.
   */
  @POST("api/v1/sync/screenings")
  suspend fun syncScreeningRecords(
    @Body records: List<ScreeningRequestDto>
  ): Response<SyncResponseDto>
}
