package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.Student
import com.example.model.StudentChallenge
import com.example.ui.components.SectionHeader
import com.example.ui.theme.EduAmberContainer
import com.example.ui.theme.EduAmberOnContainer
import com.example.ui.theme.EduAmberSecondary
import com.example.ui.theme.EduTealContainer
import com.example.ui.theme.EduTealOnContainer
import com.example.ui.theme.EduTealPrimary
import com.example.ui.theme.RiskLowContainer
import com.example.ui.theme.RiskLowGreen
import com.example.ui.theme.RiskLowOnContainer

@Composable
fun StudentScreen(
  student: Student,
  challenges: List<StudentChallenge>,
  language: AppLanguage,
  onToggleChallenge: (String) -> Unit
) {
  val completedCount = challenges.count { it.isCompleted }
  val progress = if (challenges.isNotEmpty()) completedCount.toFloat() / challenges.size else 0f

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    // Welcoming Hero Card
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
                text = if (language == AppLanguage.TAMIL) "வணக்கம், ${student.tamilName}!" else "Welcome, ${student.name.split(" ").first()}!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = EduTealOnContainer
              )
              Text(
                text = if (language == AppLanguage.TAMIL) "இன்றைய ஆரோக்கிய இலக்குகளை முடிப்போம்!" else "Let's build healthy strength & energy today!",
                fontSize = 12.sp,
                color = EduTealOnContainer.copy(alpha = 0.8f)
              )
            }
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(EduAmberSecondary),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Whatshot, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Score & Streak bar
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (language == AppLanguage.TAMIL) "சுகாதார புள்ளிகள்: 78/100" else "Health Score: 78 / 100",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = EduTealOnContainer
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = EduAmberSecondary, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "7-Day Streak!",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = EduAmberSecondary
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))
          LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(RoundedCornerShape(4.dp)),
            color = EduTealPrimary,
            trackColor = Color.White.copy(alpha = 0.6f),
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "$completedCount of ${challenges.size} habits completed today",
            fontSize = 11.sp,
            color = EduTealOnContainer.copy(alpha = 0.8f)
          )
        }
      }
      Spacer(modifier = Modifier.height(14.dp))
    }

    // Daily Health Tip
    item {
      ElevatedCard(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(EduAmberContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Star, contentDescription = null, tint = EduAmberSecondary, modifier = Modifier.size(20.dp))
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = if (language == AppLanguage.TAMIL) "இன்றைய ஆரோக்கிய குறிப்பு" else "Daily Energy Tip",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = if (language == AppLanguage.TAMIL)
                "கீரை மற்றும் வெல்லம் சாப்பிடுவது உங்கள் இரத்தத்தில் இரும்புச்சத்தை அதிகரித்து உடலுக்கு அதிக ஆற்றலைத் தரும்!"
              else
                "Did you know? Iron from greens and jaggery helps your blood deliver fresh energy to your brain so you stay sharp in school!",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 15.sp
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(14.dp))
    }

    // Section: Daily Habit Challenges
    item {
      SectionHeader(
        title = if (language == AppLanguage.TAMIL) "இன்றைய பழக்கவழக்க சவால்கள்" else "Daily Health Challenges",
        subtitle = "Complete to earn badges and keep your health streak"
      )
    }

    items(challenges) { challenge ->
      val icon = when (challenge.icon) {
        "water_drop" -> Icons.Default.LocalDrink
        "nutrition" -> Icons.Default.Restaurant
        "directions_run" -> Icons.Default.DirectionsRun
        "medication" -> Icons.Default.Medication
        else -> Icons.Default.Bedtime
      }

      ElevatedCard(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (challenge.isCompleted) RiskLowContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp)
          .testTag("challenge_item_${challenge.id}")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(if (challenge.isCompleted) RiskLowContainer else EduTealContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (challenge.isCompleted) RiskLowGreen else EduTealPrimary,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = if (language == AppLanguage.TAMIL) challenge.tamilTitle else challenge.title,
                fontSize = 13.sp,
                fontWeight = if (challenge.isCompleted) FontWeight.SemiBold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "+${challenge.points} Energy Points",
                fontSize = 10.sp,
                color = if (challenge.isCompleted) RiskLowGreen else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Checkbox(
            checked = challenge.isCompleted,
            onCheckedChange = { onToggleChallenge(challenge.id) },
            colors = CheckboxDefaults.colors(checkedColor = EduTealPrimary),
            modifier = Modifier.testTag("checkbox_${challenge.id}")
          )
        }
      }
    }

    // Badges Section
    item {
      Spacer(modifier = Modifier.height(14.dp))
      SectionHeader(
        title = if (language == AppLanguage.TAMIL) "உங்கள் சாதனைக் குறியீடுகள்" else "Earned Badges",
        subtitle = "3 unlocked this term"
      )
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        BadgeCard("Hydration Champ", "7-Day Streak", Icons.Default.LocalDrink, RiskLowGreen, Modifier.weight(1f))
        BadgeCard("Iron Explorer", "Nutrition Goal", Icons.Default.Restaurant, EduAmberSecondary, Modifier.weight(1f))
        BadgeCard("Active Spirit", "Step Champion", Icons.Default.DirectionsRun, EduTealPrimary, Modifier.weight(1f))
      }
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}

@Composable
fun BadgeCard(
  title: String,
  subtitle: String,
  icon: ImageVector,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(38.dp)
          .clip(CircleShape)
          .background(color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
      Text(text = subtitle, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}
