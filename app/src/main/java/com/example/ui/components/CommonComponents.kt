package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.RiskLevel
import com.example.model.UserRole
import com.example.ui.theme.EduAmberContainer
import com.example.ui.theme.EduAmberOnContainer
import com.example.ui.theme.EduAmberSecondary
import com.example.ui.theme.EduTealContainer
import com.example.ui.theme.EduTealOnContainer
import com.example.ui.theme.EduTealPrimary
import com.example.ui.theme.RiskHighContainer
import com.example.ui.theme.RiskHighOnContainer
import com.example.ui.theme.RiskHighRed
import com.example.ui.theme.RiskLowContainer
import com.example.ui.theme.RiskLowGreen
import com.example.ui.theme.RiskLowOnContainer
import com.example.ui.theme.RiskMediumAmber
import com.example.ui.theme.RiskMediumContainer
import com.example.ui.theme.RiskMediumOnContainer

@Composable
fun RiskBadge(
  riskLevel: RiskLevel,
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  val (bgColor, textColor, icon) = when (riskLevel) {
    RiskLevel.HIGH -> Triple(RiskHighContainer, RiskHighOnContainer, Icons.Default.Warning)
    RiskLevel.MEDIUM -> Triple(RiskMediumContainer, RiskMediumOnContainer, Icons.Outlined.Info)
    RiskLevel.LOW -> Triple(RiskLowContainer, RiskLowOnContainer, Icons.Default.CheckCircle)
  }

  val label = if (language == AppLanguage.TAMIL) riskLevel.tamilLabel else riskLevel.label

  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
      .clip(RoundedCornerShape(20.dp))
      .background(bgColor)
      .border(1.dp, textColor.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
      .padding(horizontal = 10.dp, vertical = 4.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = label,
      tint = textColor,
      modifier = Modifier.size(14.dp)
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(
      text = label,
      color = textColor,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 0.5.sp
    )
  }
}

@Composable
fun SyncBanner(
  isOffline: Boolean,
  pendingCount: Int,
  language: AppLanguage,
  onToggleOffline: () -> Unit,
  onSyncNow: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isOffline) EduAmberContainer else EduTealContainer
    ),
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)
      ) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(if (isOffline) EduAmberSecondary.copy(alpha = 0.2f) else EduTealPrimary.copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (isOffline) Icons.Default.CloudOff else Icons.Default.CloudSync,
            contentDescription = "Sync State",
            tint = if (isOffline) EduAmberOnContainer else EduTealOnContainer,
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = if (isOffline) {
              if (language == AppLanguage.TAMIL) "ஆஃப்லைன் பயன்முறை ($pendingCount பதிவுகள்)" else "Offline Mode ($pendingCount pending sync)"
            } else {
              if (language == AppLanguage.TAMIL) "ஆன்லைன் (கிளவுட் இணைக்கப்பட்டது)" else "Online (Supabase Cloud Synced)"
            },
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = if (isOffline) EduAmberOnContainer else EduTealOnContainer
          )
          Text(
            text = if (isOffline) "Tap to toggle online mode" else "SQLite queue ready for offline",
            fontSize = 11.sp,
            color = if (isOffline) EduAmberOnContainer.copy(alpha = 0.8f) else EduTealOnContainer.copy(alpha = 0.8f)
          )
        }
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        if (isOffline && pendingCount > 0) {
          FilledTonalButton(
            onClick = onSyncNow,
            modifier = Modifier
              .testTag("sync_now_button")
              .height(34.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 0.dp)
          ) {
            Text("Sync", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
          Spacer(modifier = Modifier.width(6.dp))
        }

        TextButton(
          onClick = onToggleOffline,
          modifier = Modifier.height(34.dp),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
        ) {
          Text(
            text = if (isOffline) "Go Online" else "Go Offline",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isOffline) EduAmberOnContainer else EduTealOnContainer
          )
        }
      }
    }
  }
}

@Composable
fun TopRoleHeader(
  currentRole: UserRole,
  currentLanguage: AppLanguage,
  notificationCount: Int,
  onRoleSelected: (UserRole) -> Unit,
  onToggleLanguage: () -> Unit,
  onOpenNotifications: () -> Unit
) {
  var menuExpanded by remember { mutableStateOf(false) }

  Surface(
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    modifier = Modifier.fillMaxWidth()
  ) {
    Column {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // App brand & Title
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(EduTealPrimary),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.MedicalServices,
              contentDescription = "EduHealth Logo",
              tint = Color.White,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "EduHealth Connect",
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = if (currentLanguage == AppLanguage.TAMIL) "ஆரோக்கியமான குழந்தைகள் சிறப்பாக கற்பார்கள்" else "Healthy Children Learn Better",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        // Action Icons
        Row(verticalAlignment = Alignment.CenterVertically) {
          // Language Switcher
          IconButton(
            onClick = onToggleLanguage,
            modifier = Modifier
              .testTag("language_toggle")
              .size(38.dp)
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = if (currentLanguage == AppLanguage.ENGLISH) "EN" else "தமிழ்",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = EduTealPrimary
              )
            }
          }

          // Notification Bell
          IconButton(
            onClick = onOpenNotifications,
            modifier = Modifier
              .testTag("notifications_button")
              .size(38.dp)
          ) {
            BadgedBox(
              badge = {
                if (notificationCount > 0) {
                  Badge(containerColor = RiskHighRed) {
                    Text(notificationCount.toString(), fontSize = 10.sp, color = Color.White)
                  }
                }
              }
            ) {
              Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      // Role Selection Selector Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = if (currentLanguage == AppLanguage.TAMIL) "பங்கு:" else "Role:",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.width(6.dp))
          Box {
            FilledTonalButton(
              onClick = { menuExpanded = true },
              modifier = Modifier
                .testTag("role_selector_button")
                .height(32.dp),
              contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 0.dp),
              colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = EduTealContainer,
                contentColor = EduTealOnContainer
              )
            ) {
              val roleIcon = when (currentRole) {
                UserRole.TEACHER -> Icons.Default.School
                UserRole.DOCTOR -> Icons.Default.MedicalServices
                UserRole.PARENT -> Icons.Default.Person
                UserRole.STUDENT -> Icons.Default.Person
                UserRole.ADMIN -> Icons.Default.School
              }
              Icon(imageVector = roleIcon, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (currentLanguage == AppLanguage.TAMIL) currentRole.tamilName else currentRole.displayName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }

            DropdownMenu(
              expanded = menuExpanded,
              onDismissRequest = { menuExpanded = false }
            ) {
              UserRole.values().forEach { role ->
                DropdownMenuItem(
                  text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      val icon = when (role) {
                        UserRole.TEACHER -> Icons.Default.School
                        UserRole.DOCTOR -> Icons.Default.MedicalServices
                        UserRole.PARENT -> Icons.Default.Person
                        UserRole.STUDENT -> Icons.Default.Person
                        UserRole.ADMIN -> Icons.Default.School
                      }
                      Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = EduTealPrimary)
                      Spacer(modifier = Modifier.width(8.dp))
                      Text(
                        if (currentLanguage == AppLanguage.TAMIL) role.tamilName else role.displayName,
                        fontWeight = if (role == currentRole) FontWeight.Bold else FontWeight.Normal
                      )
                    }
                  },
                  onClick = {
                    onRoleSelected(role)
                    menuExpanded = false
                  }
                )
              }
            }
          }
        }

        // Quick active user indicator
        Text(
          text = when (currentRole) {
            UserRole.TEACHER -> "Mrs. Malini (Class 7A)"
            UserRole.DOCTOR -> "Dr. Arvind (Pediatrician)"
            UserRole.PARENT -> "Suresh K. (Rahul's Father)"
            UserRole.STUDENT -> "Rahul Kumar (Class 7A)"
            UserRole.ADMIN -> "Principal K. Sundaram"
          },
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      Spacer(modifier = Modifier.height(6.dp))
    }
  }
}

@Composable
fun MetricStatCard(
  title: String,
  value: String,
  subtitle: String,
  icon: ImageVector,
  accentColor: Color,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = modifier
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Box(
          modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(accentColor.copy(alpha = 0.12f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
        }
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = value,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = subtitle,
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun SectionHeader(
  title: String,
  subtitle: String? = null,
  actionLabel: String? = null,
  onActionClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      if (subtitle != null) {
        Text(
          text = subtitle,
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
    if (actionLabel != null && onActionClick != null) {
      TextButton(onClick = onActionClick) {
        Text(actionLabel, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = EduTealPrimary)
        Spacer(modifier = Modifier.width(2.dp))
        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp), tint = EduTealPrimary)
      }
    }
  }
}

@Composable
fun MedicalDisclaimerBanner(modifier: Modifier = Modifier) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .clip(RoundedCornerShape(8.dp))
      .background(Color(0xFFFFF8E1))
      .border(1.dp, Color(0xFFFFD54F), RoundedCornerShape(8.dp))
      .padding(horizontal = 12.dp, vertical = 8.dp),
    verticalAlignment = Alignment.Top
  ) {
    Icon(
      imageVector = Icons.Outlined.Info,
      contentDescription = "Medical Disclaimer",
      tint = Color(0xFFF57F17),
      modifier = Modifier
        .size(18.dp)
        .padding(top = 1.dp)
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = "Clinical Screening & Decision Support System only. AI does NOT diagnose disease. All final medical evaluations, prescriptions, and interventions are administered by qualified healthcare professionals.",
      fontSize = 11.sp,
      color = Color(0xFF5D4037),
      lineHeight = 15.sp
    )
  }
}
