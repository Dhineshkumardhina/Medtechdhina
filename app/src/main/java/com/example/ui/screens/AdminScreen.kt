package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.components.MetricStatCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.EduAmberSecondary
import com.example.ui.theme.EduTealContainer
import com.example.ui.theme.EduTealOnContainer
import com.example.ui.theme.EduTealPrimary
import com.example.ui.theme.RiskHighRed
import com.example.ui.theme.RiskLowContainer
import com.example.ui.theme.RiskLowGreen
import com.example.ui.theme.RiskLowOnContainer

data class AuditLogEntry(
  val action: String,
  val actor: String,
  val target: String,
  val time: String,
  val status: String = "SUCCESS"
)

@Composable
fun AdminScreen(language: AppLanguage) {
  val auditLogs = listOf(
    AuditLogEntry("APPROVE_CLINICAL_REPORT", "Dr. Arvind (Doctor)", "Rahul Kumar (std-101)", "11:30 AM"),
    AuditLogEntry("HEALTH_SCREENING_SUBMIT", "Mrs. Malini (Teacher)", "Class 7A Screening", "10:15 AM"),
    AuditLogEntry("VIEW_HEALTH_PASSPORT", "Suresh K. (Parent)", "Rahul Kumar (std-101)", "09:45 AM"),
    AuditLogEntry("RBAC_PERMISSION_CHECK", "System Guard", "Restricted Clinical Rx Route", "09:12 AM")
  )

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    // School Admin Hero Header
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
            Column {
              Text(
                text = if (language == AppLanguage.TAMIL) "பள்ளி சுகாதார மேலாண்மை" else "School Health & Education Administration",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = EduTealOnContainer
              )
              Text(
                text = "Government Model Higher Secondary School (Cluster #4)",
                fontSize = 12.sp,
                color = EduTealOnContainer.copy(alpha = 0.8f)
              )
            }
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(EduTealPrimary),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.School, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Key institutional metrics
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            MetricStatCard(
              title = "Students",
              value = "450",
              subtitle = "12 Sections",
              icon = Icons.Default.People,
              accentColor = EduTealPrimary,
              modifier = Modifier.weight(1f)
            )
            MetricStatCard(
              title = "Attendance",
              value = "88.4%",
              subtitle = "School-wide",
              icon = Icons.Default.AssignmentTurnedIn,
              accentColor = RiskLowGreen,
              modifier = Modifier.weight(1f)
            )
            MetricStatCard(
              title = "Doctor Care",
              value = "34",
              subtitle = "Active Rx Plans",
              icon = Icons.Default.CheckCircle,
              accentColor = EduAmberSecondary,
              modifier = Modifier.weight(1f)
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(14.dp))
    }

    // Nutritional Demographics Breakdown Card
    item {
      ElevatedCard(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = if (language == AppLanguage.TAMIL) "மாணவர் ஊட்டச்சத்து பரவல் விகிதம்" else "Student Pediatric Nutrition Distribution",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
          Spacer(modifier = Modifier.height(10.dp))

          // Normal BMI (72%)
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Normal BMI Range (18.5 - 24.9)", fontSize = 12.sp)
            Text("72% (324 students)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RiskLowGreen)
          }
          Spacer(modifier = Modifier.height(4.dp))
          LinearProgressIndicator(
            progress = { 0.72f },
            modifier = Modifier
              .fillMaxWidth()
              .height(6.dp)
              .clip(RoundedCornerShape(3.dp)),
            color = RiskLowGreen,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Underweight (21%)
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Underweight / Anemia Risk (< 18.5)", fontSize = 12.sp)
            Text("21% (95 students)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RiskHighRed)
          }
          Spacer(modifier = Modifier.height(4.dp))
          LinearProgressIndicator(
            progress = { 0.21f },
            modifier = Modifier
              .fillMaxWidth()
              .height(6.dp)
              .clip(RoundedCornerShape(3.dp)),
            color = RiskHighRed,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Overweight (7%)
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Overweight (> 25.0)", fontSize = 12.sp)
            Text("7% (31 students)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EduAmberSecondary)
          }
          Spacer(modifier = Modifier.height(4.dp))
          LinearProgressIndicator(
            progress = { 0.07f },
            modifier = Modifier
              .fillMaxWidth()
              .height(6.dp)
              .clip(RoundedCornerShape(3.dp)),
            color = EduAmberSecondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
          )
        }
      }
      Spacer(modifier = Modifier.height(14.dp))
    }

    // Health Camps & Doctor Schedule
    item {
      SectionHeader(
        title = if (language == AppLanguage.TAMIL) "சுகாதார முகாம்கள் அட்டவணை" else "Scheduled School Health Camps",
        subtitle = "Upcoming on-campus screening and doctor visits"
      )
    }

    item {
      ElevatedCard(
        shape = RoundedCornerShape(12.dp),
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
              Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = EduTealPrimary, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text("Term 1 Pediatric Anemia Screening Camp", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("Date: 25 July 2026 • 09:00 AM - 02:00 PM", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Assigned Medical Team: Dr. Arvind Swaminathan (Lead Pediatrician), Nurse Revathi • Target: Classes 5 to 8",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
      Spacer(modifier = Modifier.height(14.dp))
    }

    // Security & Audit Log Feed (Compliance & Least Privilege)
    item {
      SectionHeader(
        title = if (language == AppLanguage.TAMIL) "பாதுகாப்பு மற்றும் தணிக்கை பதிவு" else "HIPAA/Audit Access Logs",
        subtitle = "Immutable record of all Protected Health Information (PHI) requests"
      )
    }

    items(auditLogs) { log ->
      Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Security, contentDescription = null, tint = EduTealPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(log.action, fontWeight = FontWeight.Bold, fontSize = 12.sp)
              Text("Actor: ${log.actor} • Target: ${log.target}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
          Column(horizontalAlignment = Alignment.End) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(RiskLowContainer)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(log.status, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = RiskLowOnContainer)
            }
            Text(log.time, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(28.dp))
    }
  }
}
