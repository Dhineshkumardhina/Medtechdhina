package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.model.HealthTimelineEvent
import com.example.model.RiskLevel
import com.example.model.Student
import com.example.ui.components.MetricStatCard
import com.example.ui.components.RiskBadge
import com.example.ui.components.SectionHeader
import com.example.ui.theme.EduAmberContainer
import com.example.ui.theme.EduAmberOnContainer
import com.example.ui.theme.EduAmberSecondary
import com.example.ui.theme.EduBlueTertiary
import com.example.ui.theme.EduTealContainer
import com.example.ui.theme.EduTealOnContainer
import com.example.ui.theme.EduTealPrimary
import com.example.ui.theme.RiskHighContainer
import com.example.ui.theme.RiskHighOnContainer
import com.example.ui.theme.RiskHighRed
import com.example.ui.theme.RiskLowContainer
import com.example.ui.theme.RiskLowGreen
import com.example.ui.theme.RiskLowOnContainer

@Composable
fun ParentScreen(
  student: Student,
  doctorReport: DoctorReport?,
  timeline: List<HealthTimelineEvent>,
  language: AppLanguage,
  onBookAppointment: () -> Unit
) {
  var selectedTab by remember { mutableStateOf(0) } // 0: Child Overview, 1: Digital Health Passport
  var showAppointmentModal by remember { mutableStateOf(false) }

  Column(modifier = Modifier.fillMaxSize()) {
    // Tabs
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = MaterialTheme.colorScheme.surface
    ) {
      Tab(
        selected = selectedTab == 0,
        onClick = { selectedTab = 0 },
        text = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (language == AppLanguage.TAMIL) "குழந்தை கண்ணோட்டம்" else "Child Overview")
          }
        },
        modifier = Modifier.testTag("tab_child_overview")
      )
      Tab(
        selected = selectedTab == 1,
        onClick = { selectedTab = 1 },
        text = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.HealthAndSafety, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (language == AppLanguage.TAMIL) "டிஜிட்டல் சுகாதார பாஸ்போர்ட்" else "Digital Health Passport")
          }
        },
        modifier = Modifier.testTag("tab_health_passport")
      )
    }

    if (selectedTab == 0) {
      // TAB 0: CHILD HEALTH STATUS & RECENT REPORTS
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        // Child Profile Hero Banner
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = EduTealContainer),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(46.dp)
                      .clip(CircleShape)
                      .background(EduTealPrimary),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = student.name.take(1),
                      fontWeight = FontWeight.Bold,
                      color = Color.White,
                      fontSize = 18.sp
                    )
                  }
                  Spacer(modifier = Modifier.width(12.dp))
                  Column {
                    Text(
                      text = if (language == AppLanguage.TAMIL) student.tamilName else student.name,
                      fontSize = 17.sp,
                      fontWeight = FontWeight.Bold,
                      color = EduTealOnContainer
                    )
                    Text(
                      text = "${student.gradeClass}${student.section} • Roll #${student.rollNo} • Age ${student.age}",
                      fontSize = 12.sp,
                      color = EduTealOnContainer.copy(alpha = 0.8f)
                    )
                  }
                }
                RiskBadge(riskLevel = student.riskLevel, language = language)
              }

              Spacer(modifier = Modifier.height(14.dp))

              // Status Summary Row
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                MetricStatCard(
                  title = "Attendance",
                  value = "${student.attendancePercent}%",
                  subtitle = if (student.attendancePercent < 75) "Low Attendance" else "Regular",
                  icon = Icons.Default.Assignment,
                  accentColor = if (student.attendancePercent < 75) RiskHighRed else RiskLowGreen,
                  modifier = Modifier.weight(1f)
                )
                MetricStatCard(
                  title = "BMI Status",
                  value = "${student.bmi}",
                  subtitle = student.bmiCategory,
                  icon = Icons.Default.HealthAndSafety,
                  accentColor = if (student.bmi < 15.5) RiskHighRed else RiskLowGreen,
                  modifier = Modifier.weight(1f)
                )
                MetricStatCard(
                  title = "Next Checkup",
                  value = student.nextFollowupDate?.takeLast(5) ?: "20 Jul",
                  subtitle = "Pediatric Clinic",
                  icon = Icons.Default.CalendarToday,
                  accentColor = EduAmberSecondary,
                  modifier = Modifier.weight(1f)
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(14.dp))
        }

        // Doctor Report Status Card
        item {
          ElevatedCard(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = RiskLowGreen,
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "Doctor Report Available",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                  )
                }
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(RiskLowContainer)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = "Approved by Dr. Arvind",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = RiskLowOnContainer
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "Clinical Impression: ${doctorReport?.clinicalImpression ?: "Nutritional anemia with chronic fatigue; low BMI secondary to micronutrient deficiency."}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
              )

              Spacer(modifier = Modifier.height(8.dp))
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(MaterialTheme.colorScheme.surfaceVariant)
                  .padding(10.dp)
              ) {
                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Medication, contentDescription = null, tint = EduTealPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = "Prescription:",
                      fontWeight = FontWeight.Bold,
                      fontSize = 12.sp,
                      color = EduTealPrimary
                    )
                  }
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = doctorReport?.prescription ?: "Syrup Ferrous Ascorbate + Folic Acid (5ml once daily x 30 days)",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                TextButton(onClick = { selectedTab = 1 }) {
                  Text("View Full Health Passport", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = EduTealPrimary)
                }

                Button(
                  onClick = { showAppointmentModal = true },
                  modifier = Modifier
                    .height(34.dp)
                    .testTag("book_appointment_button"),
                  colors = ButtonDefaults.buttonColors(containerColor = EduTealPrimary),
                  contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp)
                ) {
                  Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Book Checkup", fontSize = 11.sp)
                }
              }
            }
          }
          Spacer(modifier = Modifier.height(14.dp))
        }

        // Dietary & Nutrition Recommendations
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = EduAmberContainer),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Fastfood, contentDescription = null, tint = EduAmberSecondary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = if (language == AppLanguage.TAMIL) "மருத்துவரின் ஊட்டச்சத்து வழிகாட்டுதல்" else "Doctor's Nutrition & Diet Plan",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = EduAmberOnContainer
                )
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = doctorReport?.dietAdvice ?: "Iron-rich diet: Moringa leaves soup, boiled green lentils, jaggery with sesame seeds, beetroot, and bananas.",
                fontSize = 12.sp,
                color = EduAmberOnContainer,
                lineHeight = 17.sp
              )
            }
          }
          Spacer(modifier = Modifier.height(20.dp))
        }
      }
    } else {
      // TAB 1: DIGITAL HEALTH PASSPORT TIMELINE & VACCINATIONS
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        item {
          SectionHeader(
            title = if (language == AppLanguage.TAMIL) "டிஜிட்டல் சுகாதார காலவரிசை" else "Digital Health Passport Timeline",
            subtitle = "Continuous pediatric record across school years"
          )
        }

        items(timeline) { event ->
          TimelineEventCard(event = event, language = language)
        }

        item {
          Spacer(modifier = Modifier.height(16.dp))
          SectionHeader(
            title = if (language == AppLanguage.TAMIL) "தடுப்பூசி பாதுகாப்பு நிலை" else "Vaccination History & Coverage",
            subtitle = "WHO & National Immunization Schedule"
          )
        }

        item {
          VaccinationListCard()
          Spacer(modifier = Modifier.height(24.dp))
        }
      }
    }
  }

  // Appointment Booking Dialog
  if (showAppointmentModal) {
    AlertDialog(
      onDismissRequest = { showAppointmentModal = false },
      icon = {
        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = EduTealPrimary, modifier = Modifier.size(36.dp))
      },
      title = {
        Text("Book Pediatric Follow-up Checkup", fontWeight = FontWeight.Bold, fontSize = 16.sp)
      },
      text = {
        Column {
          Text("Doctor: Dr. Arvind Swaminathan (Pediatrician)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
          Text("Clinic: Government Model School Health Center", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Spacer(modifier = Modifier.height(10.dp))
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .padding(10.dp)
          ) {
            Column {
              Text("Recommended Follow-up Slot:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("Monday, 20 July 2026 • 10:30 AM", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = EduTealPrimary)
              Text("Purpose: Hemoglobin repeat check & diet evaluation", fontSize = 11.sp)
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            showAppointmentModal = false
            onBookAppointment()
          },
          colors = ButtonDefaults.buttonColors(containerColor = EduTealPrimary)
        ) {
          Text("Confirm Appointment", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showAppointmentModal = false }) {
          Text("Cancel", fontSize = 12.sp)
        }
      }
    )
  }
}

@Composable
fun TimelineEventCard(event: HealthTimelineEvent, language: AppLanguage) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
  ) {
    // Timeline connector
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.width(36.dp)
    ) {
      Box(
        modifier = Modifier
          .size(16.dp)
          .clip(CircleShape)
          .background(if (event.isDoctorApproved) RiskLowGreen else EduTealPrimary)
      )
      Box(
        modifier = Modifier
          .width(2.dp)
          .height(60.dp)
          .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
      )
    }

    ElevatedCard(
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .weight(1f)
        .padding(bottom = 8.dp)
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = event.date,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = EduTealPrimary
          )
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(if (event.isDoctorApproved) RiskLowContainer else MaterialTheme.colorScheme.surfaceVariant)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = event.badge,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = if (event.isDoctorApproved) RiskLowOnContainer else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = if (language == AppLanguage.TAMIL) event.tamilTitle else event.title,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = event.description,
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 15.sp
        )
      }
    }
  }
}

@Composable
fun VaccinationListCard() {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      VaccineRow("BCG (Tuberculosis)", "Dose 1", "Completed (Infancy)", isDone = true)
      VaccineRow("DPT (Diphtheria, Pertussis, Tetanus)", "Dose 3 + Boosters", "Completed", isDone = true)
      VaccineRow("MMR (Measles, Mumps, Rubella)", "Dose 2", "Completed", isDone = true)
      VaccineRow("Tetanus / Diphtheria (Td Booster)", "Age 10-12 Booster", "Scheduled for Camp 2026", isDone = false)
    }
  }
}

@Composable
fun VaccineRow(name: String, dose: String, status: String, isDone: Boolean) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.DateRange,
        contentDescription = null,
        tint = if (isDone) RiskLowGreen else EduAmberSecondary,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(10.dp))
      Column {
        Text(text = name, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        Text(text = dose, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
    Text(
      text = status,
      fontSize = 11.sp,
      fontWeight = FontWeight.Medium,
      color = if (isDone) RiskLowGreen else EduAmberSecondary
    )
  }
}
