package com.example.data

import com.example.model.AppLanguage
import com.example.model.DoctorReport
import com.example.model.HealthScreeningRecord
import com.example.model.HealthTimelineEvent
import com.example.model.NotificationAlert
import com.example.model.RiskAssessment
import com.example.model.RiskLevel
import com.example.model.Student
import com.example.model.StudentChallenge
import com.example.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object EduHealthRepository {

  // Current active role & language
  private val _currentRole = MutableStateFlow(UserRole.TEACHER)
  val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

  private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
  val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

  // Offline status & pending sync count
  private val _isOfflineMode = MutableStateFlow(false)
  val isOfflineMode: StateFlow<Boolean> = _isOfflineMode.asStateFlow()

  private val _pendingOfflineCount = MutableStateFlow(0)
  val pendingOfflineCount: StateFlow<Int> = _pendingOfflineCount.asStateFlow()

  // Synthetic demo students
  private val initialStudents = listOf(
    Student(
      id = "std-101",
      name = "Rahul Kumar",
      tamilName = "ராகுல் குமார்",
      age = 12,
      gradeClass = "Class 7",
      section = "A",
      rollNo = "14",
      gender = "Male",
      bloodGroup = "B+",
      attendancePercent = 68,
      heightCm = 142.0,
      weightKg = 29.8,
      symptoms = listOf("Fatigue", "Pale Conjunctiva", "Lethargy in Class"),
      riskLevel = RiskLevel.HIGH,
      priorityScore = 92,
      hasPendingDoctorReview = true,
      lastScreeningDate = "2026-07-02",
      nextFollowupDate = "2026-07-20"
    ),
    Student(
      id = "std-102",
      name = "Ananya Sharma",
      tamilName = "அனன்யா சர்மா",
      age = 11,
      gradeClass = "Class 6",
      section = "B",
      rollNo = "08",
      gender = "Female",
      bloodGroup = "O+",
      attendancePercent = 94,
      heightCm = 138.0,
      weightKg = 34.2,
      symptoms = listOf("Mild Allergy", "Seasonal Sneezing"),
      riskLevel = RiskLevel.MEDIUM,
      priorityScore = 58,
      hasPendingDoctorReview = false,
      lastScreeningDate = "2026-06-28",
      nextFollowupDate = null
    ),
    Student(
      id = "std-103",
      name = "Priya Dharshini",
      tamilName = "பிரியா தர்ஷினி",
      age = 10,
      gradeClass = "Class 5",
      section = "A",
      rollNo = "21",
      gender = "Female",
      bloodGroup = "A+",
      attendancePercent = 78,
      heightCm = 132.0,
      weightKg = 26.1,
      symptoms = listOf("Frequent Headache", "Underweight"),
      riskLevel = RiskLevel.MEDIUM,
      priorityScore = 65,
      hasPendingDoctorReview = true,
      lastScreeningDate = "2026-06-25",
      nextFollowupDate = "2026-07-15"
    ),
    Student(
      id = "std-104",
      name = "Vikram Singh",
      tamilName = "விக்ரம் சிங்",
      age = 12,
      gradeClass = "Class 7",
      section = "A",
      rollNo = "33",
      gender = "Male",
      bloodGroup = "AB+",
      attendancePercent = 96,
      heightCm = 145.0,
      weightKg = 37.0,
      symptoms = emptyList(),
      riskLevel = RiskLevel.LOW,
      priorityScore = 18,
      hasPendingDoctorReview = false,
      lastScreeningDate = "2026-06-18",
      nextFollowupDate = null
    ),
    Student(
      id = "std-105",
      name = "Meera Nair",
      tamilName = "மீரா நாயர்",
      age = 11,
      gradeClass = "Class 6",
      section = "A",
      rollNo = "19",
      gender = "Female",
      bloodGroup = "O-",
      attendancePercent = 91,
      heightCm = 139.0,
      weightKg = 33.5,
      symptoms = emptyList(),
      riskLevel = RiskLevel.LOW,
      priorityScore = 22,
      hasPendingDoctorReview = false,
      lastScreeningDate = "2026-06-12",
      nextFollowupDate = null
    )
  )

  private val _students = MutableStateFlow(initialStudents)
  val students: StateFlow<List<Student>> = _students.asStateFlow()

  // Selected student for detail / review / screening
  private val _selectedStudentId = MutableStateFlow("std-101")
  val selectedStudentId: StateFlow<String> = _selectedStudentId.asStateFlow()

  // AI Risk Assessments
  private val initialAssessments = mapOf(
    "std-101" to RiskAssessment(
      studentId = "std-101",
      priorityScore = 92,
      riskLevel = RiskLevel.HIGH,
      contributingFactors = listOf(
        "Low BMI (14.8 kg/m² — below 5th pediatric percentile)",
        "Frequent Absenteeism (Attendance 68% — 14 missed days)",
        "Persistent fatigue & lethargy reported during morning classes",
        "Clinical screening noted pale conjunctiva & low energy"
      ),
      aiSummaryDraft = "AI identified an anemia-related risk pattern correlated with low BMI and frequent absenteeism. Medical screening indicates possible iron-deficiency pattern. Clinical evaluation by a licensed physician is strongly recommended.",
      suggestedAction = "Pediatric evaluation with Complete Blood Count (CBC) and hemoglobin profiling.",
      status = "Pending Doctor Review"
    )
  )
  private val _riskAssessments = MutableStateFlow(initialAssessments)
  val riskAssessments: StateFlow<Map<String, RiskAssessment>> = _riskAssessments.asStateFlow()

  // Doctor Reports
  private val initialReports = mapOf(
    "std-101" to DoctorReport(
      studentId = "std-101",
      doctorName = "Dr. Arvind Swaminathan, MD",
      doctorSpecialty = "Pediatric Health & Nutrition",
      clinicalImpression = "Nutritional anemia with chronic fatigue; low BMI secondary to micronutrient deficiency.",
      prescription = "Syrup Ferrous Ascorbate + Folic Acid (5ml once daily after food x 30 days), Multivitamin syrup (5ml once daily).",
      dietAdvice = "Iron-rich diet: Moringa leaves soup, boiled green lentils, jaggery with sesame seeds, beetroot, and bananas.",
      exerciseAdvice = "Light walking and stretching; avoid rigorous sports during peak afternoon heat until Hb normalizes.",
      labTestsRecommended = "Complete Blood Count (CBC), Serum Ferritin, Peripheral Smear.",
      followupDate = "2026-07-20",
      schoolRemarks = "Provide mid-morning nutrition snack and permit 15-minute rest period if fatigue occurs during assembly.",
      isApproved = true,
      approvedTimestamp = "2026-07-02 11:30 AM"
    )
  )
  private val _doctorReports = MutableStateFlow(initialReports)
  val doctorReports: StateFlow<Map<String, DoctorReport>> = _doctorReports.asStateFlow()

  // Health Timeline for Rahul Kumar
  private val initialTimeline = listOf(
    HealthTimelineEvent(
      date = "15 Jan 2025",
      title = "Annual School Health Camp",
      tamilTitle = "வருடாந்திர பள்ளி சுகாதார முகாம்",
      description = "Routine height and weight screening recorded. Basic vitals normal.",
      badge = "Routine Check"
    ),
    HealthTimelineEvent(
      date = "18 March 2026",
      title = "Low BMI & Fatigue Flagged",
      tamilTitle = "குறைந்த பிஎம்ஐ & சோர்வு கண்டறியப்பட்டது",
      description = "Class teacher noted lethargy; attendance dropped to 68%. Underweight recorded.",
      badge = "Screening Flag"
    ),
    HealthTimelineEvent(
      date = "02 July 2026",
      title = "Doctor Review & Pediatric Rx",
      tamilTitle = "மருத்துவர் ஆய்வு & ஊட்டச்சத்து பரிந்துரை",
      description = "Dr. Arvind confirmed nutritional anemia pattern. Started iron & nutrition therapy.",
      badge = "Clinical Report Approved",
      isDoctorApproved = true
    ),
    HealthTimelineEvent(
      date = "20 July 2026",
      title = "Scheduled Follow-up Checkup",
      tamilTitle = "திட்டமிடப்பட்ட மறு ஆய்வு முகாம்",
      description = "Repeat hemoglobin test and evaluate response to dietary supplements.",
      badge = "Upcoming Follow-up"
    )
  )
  private val _healthTimeline = MutableStateFlow(initialTimeline)
  val healthTimeline: StateFlow<List<HealthTimelineEvent>> = _healthTimeline.asStateFlow()

  // Notifications
  private val initialNotifications = listOf(
    NotificationAlert(
      title = "Doctor Report Available",
      message = "Dr. Arvind has approved Rahul's Health Report & Nutrition Plan with follow-up on 20 July.",
      targetRole = UserRole.PARENT,
      timestamp = "Today, 11:32 AM",
      isUrgent = false
    ),
    NotificationAlert(
      title = "High Risk Student Flagged",
      message = "Rahul Kumar (Class 7A) requires clinical evaluation: Low BMI + Low Attendance + Fatigue.",
      targetRole = UserRole.DOCTOR,
      timestamp = "Today, 10:15 AM",
      isUrgent = true
    ),
    NotificationAlert(
      title = "Health Camp Screening Completed",
      message = "Class 7A screening recorded 42 students. 2 students routed to Doctor Priority Queue.",
      targetRole = UserRole.TEACHER,
      timestamp = "Yesterday, 03:45 PM"
    )
  )
  private val _notifications = MutableStateFlow(initialNotifications)
  val notifications: StateFlow<List<NotificationAlert>> = _notifications.asStateFlow()

  // Student Gamification Challenges
  private val initialChallenges = listOf(
    StudentChallenge("c1", "Drink 6 glasses of clean water", "6 டம்ளர் சுத்தமான தண்ணீர் குடிக்கவும்", "water_drop", true),
    StudentChallenge("c2", "Eat leafy greens / salad with lunch", "மதிய உணவில் கீரை அல்லது காய்கறி சாப்பிடவும்", "nutrition", true),
    StudentChallenge("c3", "20-minute active play or brisk walk", "20 நிமிடம் நடைப்பயிற்சி அல்லது விளையாட்டு", "directions_run", false),
    StudentChallenge("c4", "Take prescribed iron tonic after meal", "உணவுக்குப் பின் ஊட்டச்சத்து மருந்து உட்கொள்ளவும்", "medication", false),
    StudentChallenge("c5", "Sleep 8 hours before 9:30 PM", "இரவு 9:30 மணிக்குள் தூங்கச் செல்லவும்", "bedtime", false)
  )
  private val _studentChallenges = MutableStateFlow(initialChallenges)
  val studentChallenges: StateFlow<List<StudentChallenge>> = _studentChallenges.asStateFlow()

  // Actions
  fun setRole(role: UserRole) {
    _currentRole.value = role
  }

  fun toggleLanguage() {
    _currentLanguage.update { if (it == AppLanguage.ENGLISH) AppLanguage.TAMIL else AppLanguage.ENGLISH }
  }

  fun selectStudent(studentId: String) {
    _selectedStudentId.value = studentId
  }

  fun toggleOfflineMode() {
    _isOfflineMode.update { !it }
  }

  fun triggerSync() {
    _pendingOfflineCount.value = 0
    _notifications.update { current ->
      listOf(
        NotificationAlert(
          title = "Offline Records Synced",
          message = "All offline health screenings successfully uploaded to Supabase database.",
          targetRole = UserRole.TEACHER,
          timestamp = "Just now"
        )
      ) + current
    }
  }

  // Teacher submits a new health camp screening
  fun submitScreening(
    studentId: String,
    heightCm: Double,
    weightKg: Double,
    temperatureC: Double,
    symptoms: List<String>,
    attendancePercent: Int
  ) {
    // 1. Calculate BMI & Category
    val heightM = heightCm / 100.0
    val bmi = if (heightM > 0) Math.round((weightKg / (heightM * heightM)) * 10.0) / 10.0 else 0.0

    // 2. Hybrid AI Rule & Score Engine
    val hasFatigue = symptoms.any { it.contains("Fatigue", ignoreCase = true) || it.contains("Lethargy", ignoreCase = true) }
    val hasPale = symptoms.any { it.contains("Pale", ignoreCase = true) }
    val isUnderweight = bmi < 15.5
    val isLowAttendance = attendancePercent < 75

    val priorityScore: Int
    val riskLevel: RiskLevel
    val contributingFactors = mutableListOf<String>()

    if (isUnderweight && isLowAttendance && (hasFatigue || hasPale)) {
      priorityScore = 92
      riskLevel = RiskLevel.HIGH
      contributingFactors.add("Low BMI ($bmi kg/m² — pediatric underweight threshold)")
      contributingFactors.add("Attendance below threshold ($attendancePercent%)")
      contributingFactors.add("Symptoms noted: ${symptoms.joinToString(", ")}")
    } else if (isUnderweight || isLowAttendance || symptoms.isNotEmpty()) {
      priorityScore = 65
      riskLevel = RiskLevel.MEDIUM
      if (isUnderweight) contributingFactors.add("BMI indicates underweight ($bmi kg/m²)")
      if (isLowAttendance) contributingFactors.add("Lower attendance ($attendancePercent%)")
      if (symptoms.isNotEmpty()) contributingFactors.add("Symptoms: ${symptoms.joinToString(", ")}")
    } else {
      priorityScore = 20
      riskLevel = RiskLevel.LOW
      contributingFactors.add("Normal growth parameters and consistent attendance")
    }

    // 3. AI Draft Summary
    val aiSummary = if (riskLevel == RiskLevel.HIGH) {
      "AI identified an anemia-related risk pattern based on Low BMI ($bmi), Attendance ($attendancePercent%), and reported symptoms (${symptoms.joinToString()}). Note: AI provides screening support only; final clinical diagnosis must be conducted by a qualified doctor."
    } else if (riskLevel == RiskLevel.MEDIUM) {
      "AI identified moderate nutritional/attendance observation. Recommend routine pediatric checkup and dietary counseling."
    } else {
      "Student parameters are within healthy baseline for age group."
    }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val today = dateFormat.format(Date())

    // 4. Update Student
    _students.update { list ->
      list.map { student ->
        if (student.id == studentId) {
          student.copy(
            heightCm = heightCm,
            weightKg = weightKg,
            attendancePercent = attendancePercent,
            symptoms = symptoms,
            riskLevel = riskLevel,
            priorityScore = priorityScore,
            hasPendingDoctorReview = riskLevel == RiskLevel.HIGH || riskLevel == RiskLevel.MEDIUM,
            lastScreeningDate = today
          )
        } else student
      }
    }

    // 5. Store Risk Assessment
    val assessment = RiskAssessment(
      studentId = studentId,
      priorityScore = priorityScore,
      riskLevel = riskLevel,
      contributingFactors = contributingFactors,
      aiSummaryDraft = aiSummary,
      suggestedAction = if (riskLevel == RiskLevel.HIGH) "Urgent pediatric review & blood panel test recommended" else "Scheduled routine observation",
      status = "Pending Doctor Review"
    )
    _riskAssessments.update { it + (studentId to assessment) }

    // If offline, increment pending counter
    if (_isOfflineMode.value) {
      _pendingOfflineCount.update { it + 1 }
    }

    // 6. Push Doctor & Teacher notifications
    val studentName = _students.value.find { it.id == studentId }?.name ?: "Student"
    _notifications.update { current ->
      listOf(
        NotificationAlert(
          title = if (riskLevel == RiskLevel.HIGH) "High Priority Screening: $studentName" else "Screening Updated: $studentName",
          message = "Priority Score: $priorityScore ($riskLevel). Added to Doctor Triage Queue.",
          targetRole = UserRole.DOCTOR,
          timestamp = "Just now",
          isUrgent = riskLevel == RiskLevel.HIGH
        )
      ) + current
    }
  }

  // Doctor approves clinical report
  fun approveDoctorReport(
    studentId: String,
    clinicalImpression: String,
    prescription: String,
    dietAdvice: String,
    exerciseAdvice: String,
    labTests: String,
    followupDate: String,
    remarks: String
  ) {
    val report = DoctorReport(
      studentId = studentId,
      doctorName = "Dr. Arvind Swaminathan, MD",
      doctorSpecialty = "Pediatric Health & Nutrition",
      clinicalImpression = clinicalImpression,
      prescription = prescription,
      dietAdvice = dietAdvice,
      exerciseAdvice = exerciseAdvice,
      labTestsRecommended = labTests,
      followupDate = followupDate,
      schoolRemarks = remarks,
      isApproved = true,
      approvedTimestamp = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).format(Date())
    )

    _doctorReports.update { it + (studentId to report) }

    // Update student state (review cleared)
    _students.update { list ->
      list.map { student ->
        if (student.id == studentId) {
          student.copy(
            hasPendingDoctorReview = false,
            nextFollowupDate = followupDate
          )
        } else student
      }
    }

    // Append to timeline
    val studentName = _students.value.find { it.id == studentId }?.name ?: "Child"
    _healthTimeline.update { current ->
      current + HealthTimelineEvent(
        date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
        title = "Doctor Report Approved",
        tamilTitle = "மருத்துவர் அறிக்கை அங்கீகரிக்கப்பட்டது",
        description = "Dr. Arvind approved treatment plan: $clinicalImpression. Follow-up: $followupDate",
        badge = "Doctor Approved",
        isDoctorApproved = true
      )
    }

    // Dispatch parent notification
    _notifications.update { current ->
      listOf(
        NotificationAlert(
          title = "Doctor Report Ready for $studentName",
          message = "Dr. Arvind approved clinical diagnosis and diet plan. Check Health Passport.",
          targetRole = UserRole.PARENT,
          timestamp = "Just now",
          isUrgent = false
        )
      ) + current
    }
  }

  fun toggleChallenge(challengeId: String) {
    _studentChallenges.update { list ->
      list.map {
        if (it.id == challengeId) it.copy(isCompleted = !it.isCompleted) else it
      }
    }
  }
}
