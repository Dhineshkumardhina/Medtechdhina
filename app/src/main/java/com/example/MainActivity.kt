package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.data.EduHealthRepository
import com.example.model.AppLanguage
import com.example.model.NotificationAlert
import com.example.model.UserRole
import com.example.ui.components.SyncBanner
import com.example.ui.components.TopRoleHeader
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.DoctorScreen
import com.example.ui.screens.ParentScreen
import com.example.ui.screens.StudentScreen
import com.example.ui.screens.TeacherScreen
import com.example.ui.theme.EduTealContainer
import com.example.ui.theme.EduTealOnContainer
import com.example.ui.theme.EduTealPrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RiskHighContainer
import com.example.ui.theme.RiskHighOnContainer
import com.example.ui.theme.RiskHighRed

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        EduHealthApp()
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EduHealthApp() {
  val currentRole by EduHealthRepository.currentRole.collectAsState()
  val currentLanguage by EduHealthRepository.currentLanguage.collectAsState()
  val isOfflineMode by EduHealthRepository.isOfflineMode.collectAsState()
  val pendingOfflineCount by EduHealthRepository.pendingOfflineCount.collectAsState()
  val students by EduHealthRepository.students.collectAsState()
  val selectedStudentId by EduHealthRepository.selectedStudentId.collectAsState()
  val riskAssessments by EduHealthRepository.riskAssessments.collectAsState()
  val doctorReports by EduHealthRepository.doctorReports.collectAsState()
  val healthTimeline by EduHealthRepository.healthTimeline.collectAsState()
  val notifications by EduHealthRepository.notifications.collectAsState()
  val studentChallenges by EduHealthRepository.studentChallenges.collectAsState()

  var showNotificationsSheet by remember { mutableStateOf(false) }

  val activeStudent = students.find { it.id == selectedStudentId } ?: students.first()

  Scaffold(
    topBar = {
      Column {
        TopRoleHeader(
          currentRole = currentRole,
          currentLanguage = currentLanguage,
          notificationCount = notifications.count { !it.isRead },
          onRoleSelected = { EduHealthRepository.setRole(it) },
          onToggleLanguage = { EduHealthRepository.toggleLanguage() },
          onOpenNotifications = { showNotificationsSheet = true }
        )
        SyncBanner(
          isOffline = isOfflineMode,
          pendingCount = pendingOfflineCount,
          language = currentLanguage,
          onToggleOffline = { EduHealthRepository.toggleOfflineMode() },
          onSyncNow = { EduHealthRepository.triggerSync() }
        )
      }
    },
    bottomBar = {
      NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        modifier = Modifier.testTag("main_navigation_bar")
      ) {
        UserRole.values().forEach { role ->
          val isSelected = currentRole == role
          val (icon, label) = when (role) {
            UserRole.TEACHER -> Icons.Default.School to (if (currentLanguage == AppLanguage.TAMIL) "ஆசிரியர்" else "Teacher")
            UserRole.DOCTOR -> Icons.Default.MedicalServices to (if (currentLanguage == AppLanguage.TAMIL) "மருத்துவர்" else "Doctor")
            UserRole.PARENT -> Icons.Default.Person to (if (currentLanguage == AppLanguage.TAMIL) "பெற்றோர்" else "Parent")
            UserRole.STUDENT -> Icons.Default.Person to (if (currentLanguage == AppLanguage.TAMIL) "மாணவர்" else "Student")
            UserRole.ADMIN -> Icons.Default.School to (if (currentLanguage == AppLanguage.TAMIL) "நிர்வாகி" else "Admin")
          }

          NavigationBarItem(
            selected = isSelected,
            onClick = { EduHealthRepository.setRole(role) },
            icon = { Icon(icon, contentDescription = label) },
            label = { Text(label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = EduTealPrimary,
              selectedTextColor = EduTealPrimary,
              indicatorColor = EduTealContainer
            ),
            modifier = Modifier.testTag("nav_item_${role.name.lowercase()}")
          )
        }
      }
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      when (currentRole) {
        UserRole.TEACHER -> {
          TeacherScreen(
            students = students,
            language = currentLanguage,
            onSubmitScreening = { studentId, h, w, temp, syms, att ->
              EduHealthRepository.submitScreening(studentId, h, w, temp, syms, att)
            },
            onOpenStudentDetail = { studentId ->
              EduHealthRepository.selectStudent(studentId)
              EduHealthRepository.setRole(UserRole.DOCTOR)
            },
            onNavigateToDoctor = {
              EduHealthRepository.setRole(UserRole.DOCTOR)
            }
          )
        }

        UserRole.DOCTOR -> {
          DoctorScreen(
            students = students,
            riskAssessments = riskAssessments,
            doctorReports = doctorReports,
            selectedStudentId = selectedStudentId,
            language = currentLanguage,
            onSelectStudent = { studentId ->
              EduHealthRepository.selectStudent(studentId)
            },
            onApproveReport = { studentId, impression, rx, diet, exercise, labs, followup, remarks ->
              EduHealthRepository.approveDoctorReport(
                studentId, impression, rx, diet, exercise, labs, followup, remarks
              )
            },
            onNavigateToParentPassport = {
              EduHealthRepository.setRole(UserRole.PARENT)
            }
          )
        }

        UserRole.PARENT -> {
          ParentScreen(
            student = activeStudent,
            doctorReport = doctorReports[activeStudent.id],
            timeline = healthTimeline,
            language = currentLanguage,
            onBookAppointment = {
              EduHealthRepository.triggerSync()
            }
          )
        }

        UserRole.STUDENT -> {
          StudentScreen(
            student = activeStudent,
            challenges = studentChallenges,
            language = currentLanguage,
            onToggleChallenge = { challengeId ->
              EduHealthRepository.toggleChallenge(challengeId)
            }
          )
        }

        UserRole.ADMIN -> {
          AdminScreen(language = currentLanguage)
        }
      }
    }
  }

  // Notifications Sheet
  if (showNotificationsSheet) {
    ModalBottomSheet(
      onDismissRequest = { showNotificationsSheet = false },
      sheetState = rememberModalBottomSheetState()
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
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Notifications, contentDescription = null, tint = EduTealPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (currentLanguage == AppLanguage.TAMIL) "அறிவிப்புகள்" else "System Notifications",
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp
            )
          }
          IconButton(onClick = { showNotificationsSheet = false }) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
          items(notifications) { item ->
            NotificationRow(notification = item)
          }
        }
      }
    }
  }
}

@Composable
fun NotificationRow(notification: NotificationAlert) {
  Card(
    shape = RoundedCornerShape(10.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (notification.isUrgent) RiskHighContainer else MaterialTheme.colorScheme.surfaceVariant
    ),
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.Top
    ) {
      Box(
        modifier = Modifier
          .size(10.dp)
          .clip(CircleShape)
          .background(if (notification.isUrgent) RiskHighRed else EduTealPrimary)
          .padding(top = 4.dp)
      )
      Spacer(modifier = Modifier.width(10.dp))
      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = notification.title,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = if (notification.isUrgent) RiskHighOnContainer else MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = notification.timestamp,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = notification.message,
          fontSize = 11.sp,
          color = if (notification.isUrgent) RiskHighOnContainer else MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 15.sp
        )
      }
    }
  }
}

