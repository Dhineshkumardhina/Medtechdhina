package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.DoctorReport
import com.example.model.RiskAssessment
import com.example.model.RiskLevel
import com.example.model.Student
import com.example.ui.components.MedicalDisclaimerBanner
import com.example.ui.components.MetricStatCard
import com.example.ui.components.RiskBadge
import com.example.ui.components.SectionHeader
import com.example.ui.theme.EduAmberSecondary
import com.example.ui.theme.EduBlueTertiary
import com.example.ui.theme.EduTealContainer
import com.example.ui.theme.EduTealOnContainer
import com.example.ui.theme.EduTealPrimary
import com.example.ui.theme.RiskHighContainer
import com.example.ui.theme.RiskHighOnContainer
import com.example.ui.theme.RiskHighRed
import com.example.ui.theme.RiskLowGreen
import com.example.ui.theme.RiskMediumAmber

@Composable
fun DoctorScreen(
  students: List<Student>,
  riskAssessments: Map<String, RiskAssessment>,
  doctorReports: Map<String, DoctorReport>,
  selectedStudentId: String,
  language: AppLanguage,
  onSelectStudent: (String) -> Unit,
  onApproveReport: (studentId: String, impression: String, rx: String, diet: String, exercise: String, labs: String, followup: String, remarks: String) -> Unit,
  onNavigateToParentPassport: () -> Unit
) {
  var selectedTab by remember { mutableStateOf(0) } // 0: AI Priority Queue, 1: Clinical Review Form
  var showApprovalDialog by remember { mutableStateOf(false) }

  // Sorted Queue (Urgent highest priority first)
  val priorityQueue = remember(students) {
    students.sortedByDescending { it.priorityScore }
  }

  val activeStudent = students.find { it.id == selectedStudentId } ?: students.first()
  val activeAssessment = riskAssessments[activeStudent.id]
  val existingReport = doctorReports[activeStudent.id]

  // Editable Form fields
  var clinicalImpression by remember(activeStudent.id) {
    mutableStateOf(existingReport?.clinicalImpression ?: "Nutritional deficiency with chronic fatigue; evaluate for pediatric anemia.")
  }
  var prescription by remember(activeStudent.id) {
    mutableStateOf(existingReport?.prescription ?: "Syrup Ferrous Ascorbate + Folic Acid (5ml once daily after food x 30 days), Multivitamin syrup (5ml once daily).")
  }
  var dietAdvice by remember(activeStudent.id) {
    mutableStateOf(existingReport?.dietAdvice ?: "Iron-rich diet: Moringa leaves soup, boiled green lentils, jaggery with sesame seeds, beetroot, and bananas.")
  }
  var exerciseAdvice by remember(activeStudent.id) {
    mutableStateOf(existingReport?.exerciseAdvice ?: "Light walking and stretching; avoid strenuous games during peak afternoon heat until Hb normalizes.")
  }
  var labTests by remember(activeStudent.id) {
    mutableStateOf(existingReport?.labTestsRecommended ?: "Complete Blood Count (CBC), Serum Ferritin, Peripheral Smear.")
  }
  var followupDate by remember(activeStudent.id) {
    mutableStateOf(existingReport?.followupDate ?: "2026-07-20")
  }
  var schoolRemarks by remember(activeStudent.id) {
    mutableStateOf(existingReport?.schoolRemarks ?: "Provide mid-morning nutrition snack and permit 15-minute rest period if fatigue occurs during assembly.")
  }

  Column(modifier = Modifier.fillMaxSize()) {
    // Medical Disclaimer Banner at the very top of Doctor Workspace
    MedicalDisclaimerBanner()

    // Doctor Workspace Tabs
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = MaterialTheme.colorScheme.surface
    ) {
      Tab(
        selected = selectedTab == 0,
        onClick = { selectedTab = 0 },
        text = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.TrendingUp, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (language == AppLanguage.TAMIL) "AI முன்னுரிமை வரிசை" else "AI Priority Queue")
          }
        },
        modifier = Modifier.testTag("tab_priority_queue")
      )
      Tab(
        selected = selectedTab == 1,
        onClick = { selectedTab = 1 },
        text = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.MedicalServices, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (language == AppLanguage.TAMIL) "மருத்துவ அறிக்கை படிவம்" else "Clinical Review (${activeStudent.name.split(" ").first()})")
          }
        },
        modifier = Modifier.testTag("tab_clinical_review")
      )
    }

    if (selectedTab == 0) {
      // TAB 0: AI PRIORITY QUEUE
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        // Doctor Stats Row
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            MetricStatCard(
              title = "Today's Clinic",
              value = "4",
              subtitle = "Appointments",
              icon = Icons.Default.CalendarToday,
              accentColor = EduTealPrimary,
              modifier = Modifier.weight(1f)
            )
            MetricStatCard(
              title = "High Attention",
              value = "${students.count { it.riskLevel == RiskLevel.HIGH }}",
              subtitle = "Priority >80",
              icon = Icons.Default.Warning,
              accentColor = RiskHighRed,
              modifier = Modifier.weight(1f)
            )
            MetricStatCard(
              title = "Follow-ups Due",
              value = "2",
              subtitle = "Next 14 days",
              icon = Icons.Default.EventNote,
              accentColor = EduAmberSecondary,
              modifier = Modifier.weight(1f)
            )
          }
        }

        item {
          SectionHeader(
            title = if (language == AppLanguage.TAMIL) "AI முன்னுரிமை வரிசை (அவசர அடிப்படையில்)" else "AI Triage Priority Queue",
            subtitle = "Ranked automatically by BMI, absenteeism, and physical screening flags"
          )
        }

        items(priorityQueue) { student ->
          val assessment = riskAssessments[student.id]
          val isTopUrgent = student.priorityScore >= 80

          ElevatedCard(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
              containerColor = if (isTopUrgent) RiskHighContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp)
              .testTag("priority_queue_item_${student.id}")
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(34.dp)
                      .clip(CircleShape)
                      .background(if (isTopUrgent) RiskHighRed else EduTealPrimary),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = "${student.priorityScore}",
                      fontWeight = FontWeight.Bold,
                      color = Color.White,
                      fontSize = 13.sp
                    )
                  }
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(
                      text = if (language == AppLanguage.TAMIL) student.tamilName else student.name,
                      fontWeight = FontWeight.Bold,
                      fontSize = 15.sp
                    )
                    Text(
                      text = "${student.gradeClass}${student.section} • Age ${student.age} • Roll #${student.rollNo}",
                      fontSize = 11.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }
                RiskBadge(riskLevel = student.riskLevel, language = language)
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Explainable Risk Reason
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(MaterialTheme.colorScheme.surface)
                  .padding(10.dp)
              ) {
                Column {
                  Text(
                    text = "AI Contributing Factors:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = if (assessment != null && assessment.contributingFactors.isNotEmpty()) {
                      assessment.contributingFactors.joinToString(" • ")
                    } else {
                      "Low BMI (${student.bmi}) • Low Attendance (${student.attendancePercent}%) • ${student.symptoms.joinToString(", ")}"
                    },
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = if (student.hasPendingDoctorReview) "Pending Clinical Review" else "Doctor Reviewed",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = if (student.hasPendingDoctorReview) EduAmberSecondary else RiskLowGreen
                )

                Button(
                  onClick = {
                    onSelectStudent(student.id)
                    selectedTab = 1
                  },
                  modifier = Modifier
                    .height(34.dp)
                    .testTag("review_student_button_${student.id}"),
                  colors = ButtonDefaults.buttonColors(
                    containerColor = if (isTopUrgent) RiskHighRed else EduTealPrimary
                  ),
                  contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp)
                ) {
                  Text("Review Patient", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }
    } else {
      // TAB 1: STRUCTURED CLINICAL REPORT FORM & AI SUMMARY
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        // Patient Profile Bar
        item {
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = EduTealContainer),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = activeStudent.name,
                  fontWeight = FontWeight.Bold,
                  fontSize = 17.sp,
                  color = EduTealOnContainer
                )
                Text(
                  text = "${activeStudent.gradeClass}${activeStudent.section} • Age ${activeStudent.age} • Blood Group: ${activeStudent.bloodGroup}",
                  fontSize = 12.sp,
                  color = EduTealOnContainer.copy(alpha = 0.8f)
                )
                Text(
                  text = "Height: ${activeStudent.heightCm} cm • Weight: ${activeStudent.weightKg} kg • BMI: ${activeStudent.bmi} (${activeStudent.bmiCategory})",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = EduTealOnContainer
                )
              }
              RiskBadge(riskLevel = activeStudent.riskLevel, language = language)
            }
          }
          Spacer(modifier = Modifier.height(12.dp))
        }

        // Section: AI Generated Draft Summary
        item {
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F7FF)),
            modifier = Modifier
              .fillMaxWidth()
              .border(1.dp, Color(0xFF90CAF9), RoundedCornerShape(12.dp))
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E88E5)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Outlined.Psychology, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "AI Clinical Screening Summary (Draft)",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = Color(0xFF0D47A1)
                )
              }

              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = activeAssessment?.aiSummaryDraft
                  ?: "AI identified an anemia-related risk pattern based on Low BMI (${activeStudent.bmi}), low attendance (${activeStudent.attendancePercent}%), and reported fatigue. Medical evaluation recommended.",
                fontSize = 12.sp,
                color = Color(0xFF1A237E),
                lineHeight = 17.sp
              )

              Spacer(modifier = Modifier.height(8.dp))
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(6.dp))
                  .background(Color.White)
                  .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "Suggested Action: CBC Blood Test & Diet Plan",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = Color(0xFF1565C0)
                )
                Text(
                  text = "Doctor Decision Required",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = RiskHighRed
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(14.dp))
        }

        // Section: Doctor Structured Report Form
        item {
          Text(
            text = "Doctor Structured Clinical Form",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )
          Text(
            text = "Review, adjust diagnosis, prescribe treatment, and sign report.",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(10.dp))
        }

        // Form Fields
        item {
          OutlinedTextField(
            value = clinicalImpression,
            onValueChange = { clinicalImpression = it },
            label = { Text("Clinical Impression / Diagnosis") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("doctor_impression_input"),
            minLines = 2
          )
          Spacer(modifier = Modifier.height(10.dp))
        }

        item {
          OutlinedTextField(
            value = prescription,
            onValueChange = { prescription = it },
            label = { Text("Prescription & Dosage") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("doctor_rx_input"),
            minLines = 2
          )
          Spacer(modifier = Modifier.height(10.dp))
        }

        item {
          OutlinedTextField(
            value = dietAdvice,
            onValueChange = { dietAdvice = it },
            label = { Text("Nutritional & Dietary Recommendations") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("doctor_diet_input"),
            minLines = 2
          )
          Spacer(modifier = Modifier.height(10.dp))
        }

        item {
          OutlinedTextField(
            value = exerciseAdvice,
            onValueChange = { exerciseAdvice = it },
            label = { Text("Exercise & Physical Activity Guidance") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
          Spacer(modifier = Modifier.height(10.dp))
        }

        item {
          OutlinedTextField(
            value = labTests,
            onValueChange = { labTests = it },
            label = { Text("Recommended Diagnostic Tests") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
          Spacer(modifier = Modifier.height(10.dp))
        }

        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedTextField(
              value = followupDate,
              onValueChange = { followupDate = it },
              label = { Text("Follow-up Date") },
              modifier = Modifier.weight(1f),
              singleLine = true
            )
            OutlinedTextField(
              value = schoolRemarks,
              onValueChange = { schoolRemarks = it },
              label = { Text("School / Teacher Accommodation") },
              modifier = Modifier.weight(1f),
              singleLine = true
            )
          }
          Spacer(modifier = Modifier.height(20.dp))
        }

        // Action Buttons: Save Draft & Approve Report
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedButton(
              onClick = { /* Saved locally */ },
              modifier = Modifier
                .weight(1f)
                .height(48.dp)
            ) {
              Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Save Draft", fontSize = 12.sp)
            }

            Button(
              onClick = {
                onApproveReport(
                  activeStudent.id,
                  clinicalImpression,
                  prescription,
                  dietAdvice,
                  exerciseAdvice,
                  labTests,
                  followupDate,
                  schoolRemarks
                )
                showApprovalDialog = true
              },
              modifier = Modifier
                .weight(2f)
                .height(48.dp)
                .testTag("approve_report_button"),
              colors = ButtonDefaults.buttonColors(containerColor = EduTealPrimary)
            ) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Approve & Sign Report", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }

  // Approval Dialog
  if (showApprovalDialog) {
    AlertDialog(
      onDismissRequest = { showApprovalDialog = false },
      icon = {
        Icon(
          imageVector = Icons.Default.AssignmentTurnedIn,
          contentDescription = null,
          tint = RiskLowGreen,
          modifier = Modifier.size(42.dp)
        )
      },
      title = {
        Text("Clinical Report Approved & Signed", fontWeight = FontWeight.Bold, fontSize = 16.sp)
      },
      text = {
        Column {
          Text(
            text = "Dr. Arvind has finalized and signed the health report for ${activeStudent.name}.",
            fontSize = 13.sp
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "• Digital Health Passport updated with new clinical timeline entry.\n• Push notification dispatched to parent (${activeStudent.name}'s Father).\n• School accommodation notice flagged for Teacher (Mrs. Malini).",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            showApprovalDialog = false
            onNavigateToParentPassport()
          },
          colors = ButtonDefaults.buttonColors(containerColor = EduTealPrimary),
          modifier = Modifier.testTag("view_parent_passport_button")
        ) {
          Text("View Parent Passport", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showApprovalDialog = false }) {
          Text("Close", fontSize = 12.sp)
        }
      }
    )
  }
}
