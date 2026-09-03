package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.RiskLevel
import com.example.model.Student
import com.example.model.UserRole
import com.example.ui.components.MetricStatCard
import com.example.ui.components.RiskBadge
import com.example.ui.components.SectionHeader
import com.example.ui.theme.EduAmberSecondary
import com.example.ui.theme.EduTealContainer
import com.example.ui.theme.EduTealOnContainer
import com.example.ui.theme.EduTealPrimary
import com.example.ui.theme.RiskHighContainer
import com.example.ui.theme.RiskHighOnContainer
import com.example.ui.theme.RiskHighRed
import com.example.ui.theme.RiskLowGreen
import com.example.ui.theme.RiskMediumAmber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TeacherScreen(
  students: List<Student>,
  language: AppLanguage,
  onSubmitScreening: (studentId: String, height: Double, weight: Double, temp: Double, symptoms: List<String>, attendance: Int) -> Unit,
  onOpenStudentDetail: (studentId: String) -> Unit,
  onNavigateToDoctor: () -> Unit
) {
  var showScreeningSheet by remember { mutableStateOf(false) }
  var showSuccessDialog by remember { mutableStateOf(false) }
  var selectedScreeningStudent by remember { mutableStateOf(students.firstOrNull { it.id == "std-101" } ?: students.first()) }

  // Form states
  var heightInput by remember { mutableStateOf(selectedScreeningStudent.heightCm.toString()) }
  var weightInput by remember { mutableStateOf(selectedScreeningStudent.weightKg.toString()) }
  var attendanceInput by remember { mutableStateOf(selectedScreeningStudent.attendancePercent.toString()) }
  var temperatureInput by remember { mutableStateOf("37.0") }
  val availableSymptoms = listOf("Fatigue", "Pale Conjunctiva", "Lethargy in Class", "Frequent Headache", "Loss of Appetite", "Cough/Cold")
  var selectedSymptoms by remember { mutableStateOf(setOf("Fatigue", "Pale Conjunctiva")) }

  val highRiskCount = students.count { it.riskLevel == RiskLevel.HIGH }
  val mediumRiskCount = students.count { it.riskLevel == RiskLevel.MEDIUM }

  Box(modifier = Modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 80.dp)
    ) {
      // Classroom Header
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = EduTealContainer),
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = if (language == AppLanguage.TAMIL) "வகுப்பு 7A கண்ணோட்டம்" else "Class 7A Overview",
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Bold,
                  color = EduTealOnContainer
                )
                Text(
                  text = "Government Model Higher Secondary School",
                  fontSize = 12.sp,
                  color = EduTealOnContainer.copy(alpha = 0.8f)
                )
              }
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(EduTealPrimary)
                  .padding(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "Term 1",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Class stats row
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              MetricStatCard(
                title = if (language == AppLanguage.TAMIL) "மாணவர்கள்" else "Students",
                value = "42",
                subtitle = "Active Enrolled",
                icon = Icons.Default.People,
                accentColor = EduTealPrimary,
                modifier = Modifier.weight(1f)
              )
              MetricStatCard(
                title = if (language == AppLanguage.TAMIL) "வருகை" else "Attendance",
                value = "89%",
                subtitle = "Monthly Avg",
                icon = Icons.Default.Assignment,
                accentColor = RiskLowGreen,
                modifier = Modifier.weight(1f)
              )
              MetricStatCard(
                title = if (language == AppLanguage.TAMIL) "கவனம் தேவை" else "Needs Review",
                value = "${highRiskCount + mediumRiskCount}",
                subtitle = "$highRiskCount High, $mediumRiskCount Med",
                icon = Icons.Default.Warning,
                accentColor = RiskHighRed,
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }

      // Quick Health Screening CTA
      item {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(EduTealPrimary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.HealthAndSafety, contentDescription = null, tint = EduTealPrimary)
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = if (language == AppLanguage.TAMIL) "சுகாதார முகாம் விரைவு பதிவு" else "Health Camp Screening Mode",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                )
                Text(
                  text = "Fast multi-student vitals & AI screening entry",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
            Button(
              onClick = {
                selectedScreeningStudent = students.firstOrNull { it.id == "std-101" } ?: students.first()
                heightInput = selectedScreeningStudent.heightCm.toString()
                weightInput = selectedScreeningStudent.weightKg.toString()
                attendanceInput = selectedScreeningStudent.attendancePercent.toString()
                showScreeningSheet = true
              },
              modifier = Modifier
                .testTag("start_screening_button")
                .height(38.dp),
              colors = ButtonDefaults.buttonColors(containerColor = EduTealPrimary)
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Screen", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      // Section: Students Requiring Attention
      item {
        SectionHeader(
          title = if (language == AppLanguage.TAMIL) "கவனம் தேவைப்படும் மாணவர்கள்" else "Students Requiring Attention",
          subtitle = "Correlating low attendance with pediatric health markers"
        )
      }

      items(students.filter { it.riskLevel == RiskLevel.HIGH || it.riskLevel == RiskLevel.MEDIUM }) { student ->
        StudentAttentionCard(
          student = student,
          language = language,
          onScreenClick = {
            selectedScreeningStudent = student
            heightInput = student.heightCm.toString()
            weightInput = student.weightKg.toString()
            attendanceInput = student.attendancePercent.toString()
            showScreeningSheet = true
          },
          onReviewClick = {
            onOpenStudentDetail(student.id)
          }
        )
      }

      // Section: All Students Roster
      item {
        Spacer(modifier = Modifier.height(12.dp))
        SectionHeader(
          title = if (language == AppLanguage.TAMIL) "வகுப்பு மாணவர் பட்டியல்" else "Class 7A Student Roster",
          subtitle = "${students.size} Students total"
        )
      }

      items(students) { student ->
        StudentRosterRow(
          student = student,
          language = language,
          onClick = { onOpenStudentDetail(student.id) }
        )
      }
    }

    // FAB for Health Camp Screening
    FloatingActionButton(
      onClick = {
        selectedScreeningStudent = students.firstOrNull { it.id == "std-101" } ?: students.first()
        heightInput = selectedScreeningStudent.heightCm.toString()
        weightInput = selectedScreeningStudent.weightKg.toString()
        attendanceInput = selectedScreeningStudent.attendancePercent.toString()
        showScreeningSheet = true
      },
      containerColor = EduTealPrimary,
      contentColor = Color.White,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
        .testTag("fab_quick_screening")
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.Add, contentDescription = "New Screening")
        Spacer(modifier = Modifier.width(6.dp))
        Text(if (language == AppLanguage.TAMIL) "முகாம் பதிவு" else "Screening", fontWeight = FontWeight.Bold)
      }
    }
  }

  // Health Camp Screening Bottom Sheet
  if (showScreeningSheet) {
    ModalBottomSheet(
      onDismissRequest = { showScreeningSheet = false },
      sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp)
          .padding(bottom = 32.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = if (language == AppLanguage.TAMIL) "சுகாதார முகாம் பரிசோதனை" else "Health Camp Screening Entry",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Student: ${selectedScreeningStudent.name} (${selectedScreeningStudent.gradeClass})",
              fontSize = 13.sp,
              color = EduTealPrimary,
              fontWeight = FontWeight.SemiBold
            )
          }
          IconButton(onClick = { showScreeningSheet = false }) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Student Selector Chips
        Text(text = "Select Student:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          students.forEach { s ->
            FilterChip(
              selected = s.id == selectedScreeningStudent.id,
              onClick = {
                selectedScreeningStudent = s
                heightInput = s.heightCm.toString()
                weightInput = s.weightKg.toString()
                attendanceInput = s.attendancePercent.toString()
              },
              label = { Text(s.name, fontSize = 12.sp) }
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Vitals inputs (Height, Weight, Temp, Attendance)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedTextField(
            value = heightInput,
            onValueChange = { heightInput = it },
            label = { Text("Height (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier
              .weight(1f)
              .testTag("height_input"),
            singleLine = true
          )
          OutlinedTextField(
            value = weightInput,
            onValueChange = { weightInput = it },
            label = { Text("Weight (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier
              .weight(1f)
              .testTag("weight_input"),
            singleLine = true
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedTextField(
            value = attendanceInput,
            onValueChange = { attendanceInput = it },
            label = { Text("Attendance %") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
              .weight(1f)
              .testTag("attendance_input"),
            singleLine = true
          )
          OutlinedTextField(
            value = temperatureInput,
            onValueChange = { temperatureInput = it },
            label = { Text("Temp (°C)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.weight(1f),
            singleLine = true
          )
        }

        // Real-time BMI calculation helper
        val h = heightInput.toDoubleOrNull() ?: 0.0
        val w = weightInput.toDoubleOrNull() ?: 0.0
        val computedBmi = if (h > 0) Math.round((w / ((h / 100.0) * (h / 100.0))) * 10.0) / 10.0 else 0.0

        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (computedBmi < 15.5) RiskHighContainer else EduTealContainer)
            .padding(horizontal = 12.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Computed BMI: $computedBmi kg/m²",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (computedBmi < 15.5) RiskHighOnContainer else EduTealOnContainer
          )
          Text(
            text = if (computedBmi < 15.5) "Underweight (<15.5)" else "Healthy Baseline",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (computedBmi < 15.5) RiskHighRed else EduTealPrimary
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Symptoms Multi-Select
        Text(text = "Observed Physical / Behavioral Symptoms:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          availableSymptoms.forEach { symptom ->
            val isSelected = selectedSymptoms.contains(symptom)
            FilterChip(
              selected = isSelected,
              onClick = {
                selectedSymptoms = if (isSelected) selectedSymptoms - symptom else selectedSymptoms + symptom
              },
              label = { Text(symptom, fontSize = 11.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = EduAmberSecondary.copy(alpha = 0.2f),
                selectedLabelColor = EduAmberSecondary
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Submit Button
        Button(
          onClick = {
            val hVal = heightInput.toDoubleOrNull() ?: selectedScreeningStudent.heightCm
            val wVal = weightInput.toDoubleOrNull() ?: selectedScreeningStudent.weightKg
            val tVal = temperatureInput.toDoubleOrNull() ?: 37.0
            val aVal = attendanceInput.toIntOrNull() ?: selectedScreeningStudent.attendancePercent

            onSubmitScreening(
              selectedScreeningStudent.id,
              hVal,
              wVal,
              tVal,
              selectedSymptoms.toList(),
              aVal
            )
            showScreeningSheet = false
            showSuccessDialog = true
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("submit_screening_button"),
          colors = ButtonDefaults.buttonColors(containerColor = EduTealPrimary)
        ) {
          Icon(Icons.Default.Check, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (language == AppLanguage.TAMIL) "பரிசோதனை சமர்ப்பிக்கவும் & AI ஆய்வு" else "Submit Screening & Run AI Risk Analysis",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }

  // AI Screening Result Dialog
  if (showSuccessDialog) {
    AlertDialog(
      onDismissRequest = { showSuccessDialog = false },
      icon = {
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = null,
          tint = EduTealPrimary,
          modifier = Modifier.size(36.dp)
        )
      },
      title = {
        Text("AI Risk Analysis Completed", fontWeight = FontWeight.Bold, fontSize = 16.sp)
      },
      text = {
        Column {
          Text(
            text = "Screening for ${selectedScreeningStudent.name} processed successfully.",
            fontSize = 13.sp
          )
          Spacer(modifier = Modifier.height(8.dp))
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(RiskHighContainer)
              .padding(10.dp)
          ) {
            Column {
              Text(
                text = "Priority Score: 92 / 100",
                fontWeight = FontWeight.Bold,
                color = RiskHighRed,
                fontSize = 13.sp
              )
              Text(
                text = "Result: HIGH RISK (Low BMI + Low Attendance + Fatigue)",
                fontSize = 12.sp,
                color = RiskHighOnContainer
              )
              Text(
                text = "Added to Doctor Triage Queue for clinical review.",
                fontSize = 11.sp,
                color = RiskHighOnContainer.copy(alpha = 0.8f)
              )
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            showSuccessDialog = false
            onNavigateToDoctor()
          },
          colors = ButtonDefaults.buttonColors(containerColor = EduTealPrimary),
          modifier = Modifier.testTag("dialog_goto_doctor_button")
        ) {
          Text("Open Doctor Queue", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showSuccessDialog = false }) {
          Text("Dismiss", fontSize = 12.sp)
        }
      }
    )
  }
}

@Composable
fun StudentAttentionCard(
  student: Student,
  language: AppLanguage,
  onScreenClick: () -> Unit,
  onReviewClick: () -> Unit
) {
  ElevatedCard(
    shape = RoundedCornerShape(14.dp),
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .clickable { onReviewClick() }
      .testTag("student_attention_card_${student.id}")
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = if (language == AppLanguage.TAMIL) student.tamilName else student.name,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )
          Text(
            text = "${student.gradeClass}${student.section} • Roll #${student.rollNo} • Age ${student.age}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        RiskBadge(riskLevel = student.riskLevel, language = language)
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Key Metrics Chips
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
          Column {
            Text("Attendance", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
              "${student.attendancePercent}%",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = if (student.attendancePercent < 75) RiskHighRed else MaterialTheme.colorScheme.onSurface
            )
          }
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
          Column {
            Text("BMI (${student.bmiCategory})", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
              "${student.bmi} kg/m²",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = if (student.bmi < 15.5) RiskHighRed else MaterialTheme.colorScheme.onSurface
            )
          }
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
          Column {
            Text("Priority Score", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
              "${student.priorityScore} / 100",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = if (student.priorityScore > 80) RiskHighRed else EduAmberSecondary
            )
          }
        }
      }

      // Symptoms noted
      if (student.symptoms.isNotEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Symptoms: ${student.symptoms.joinToString(", ")}",
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Action Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        OutlinedButton(
          onClick = onScreenClick,
          modifier = Modifier.height(34.dp),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp)
        ) {
          Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Update Vitals", fontSize = 11.sp)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
          onClick = onReviewClick,
          modifier = Modifier.height(34.dp),
          colors = ButtonDefaults.buttonColors(containerColor = EduTealPrimary),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp)
        ) {
          Icon(Icons.Default.MedicalServices, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Review Record", fontSize = 11.sp)
        }
      }
    }
  }
}

@Composable
fun StudentRosterRow(
  student: Student,
  language: AppLanguage,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(horizontal = 16.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(EduTealPrimary.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = student.name.take(1),
          fontWeight = FontWeight.Bold,
          color = EduTealPrimary,
          fontSize = 14.sp
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = if (language == AppLanguage.TAMIL) student.tamilName else student.name,
          fontWeight = FontWeight.SemiBold,
          fontSize = 14.sp
        )
        Text(
          text = "Roll #${student.rollNo} • Attendance: ${student.attendancePercent}% • BMI: ${student.bmi}",
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    RiskBadge(riskLevel = student.riskLevel, language = language)
  }
}
