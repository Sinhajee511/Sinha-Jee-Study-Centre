package com.example.ui.screens.classes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainViewModel
import com.example.data.local.entity.SubjectEntity
import com.example.ui.components.SinhaBottomNavigation
import com.example.ui.components.SinhaTopAppBar
import com.example.ui.theme.ScholarPrimary

@Composable
fun ClassSelectionScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val subjects by viewModel.subjects.collectAsState()

    val currentClass = user?.studentClass ?: "10"
    val currentStream = user?.stream ?: "Science"

    Scaffold(
        topBar = {
            SinhaTopAppBar(
                title = "पाठ्यपुस्तकें (Digital Textbooks)",
                subtitle = "कक्षा $currentClass • विषय सूची"
            )
        },
        bottomBar = {
            SinhaBottomNavigation(currentRoute = "class_selection", onNavigate = onNavigate)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Class Tabs (Class 10 / Class 12)
            TabRow(
                selectedTabIndex = if (currentClass == "10") 0 else 1,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = ScholarPrimary
            ) {
                Tab(
                    selected = currentClass == "10",
                    onClick = { viewModel.switchClass("10") },
                    text = {
                        Text(
                            text = "CLASS 10 (कक्षा 10)",
                            fontWeight = if (currentClass == "10") FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    modifier = Modifier.testTag("tab_class_10")
                )
                Tab(
                    selected = currentClass == "12",
                    onClick = { viewModel.switchClass("12") },
                    text = {
                        Text(
                            text = "CLASS 12 (कक्षा 12)",
                            fontWeight = if (currentClass == "12") FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    modifier = Modifier.testTag("tab_class_12")
                )
            }

            // If Class 12, Stream Selector chips
            if (currentClass == "12") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Science" to "विज्ञान", "Arts" to "कला", "Commerce" to "वाणिज्य").forEach { (streamKey, streamHi) ->
                        FilterChip(
                            selected = currentStream == streamKey,
                            onClick = { viewModel.switchStream(streamKey) },
                            label = { Text("$streamKey ($streamHi)") }
                        )
                    }
                }
            }

            // Subject List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(subjects) { subject ->
                    SubjectCard(
                        subject = subject,
                        onClick = {
                            onNavigate("subject_chapters/${subject.id}/${subject.titleHindi}")
                        }
                    )
                }

                // If user is Admin, allow Add Subject
                if (user?.isAdmin == true) {
                    item {
                        Button(
                            onClick = { onNavigate("admin_panel") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("नया विषय जोड़ें (Admin Add Subject)")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SubjectCard(
    subject: SubjectEntity,
    onClick: () -> Unit
) {
    val color = try {
        Color(android.graphics.Color.parseColor(subject.colorHex))
    } catch (e: Exception) {
        ScholarPrimary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("subject_card_${subject.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getSubjectIcon(subject.iconName),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subject.titleHindi,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${subject.titleEnglish} • संपूर्ण पाठ्यपुस्तक व नोट्स",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Open",
                    tint = ScholarPrimary,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

private fun getSubjectIcon(iconName: String): ImageVector {
    return when (iconName) {
        "science", "bolt" -> Icons.Default.Science
        "calculate" -> Icons.Default.Calculate
        "public" -> Icons.Default.Public
        "menu_book", "history_edu" -> Icons.Default.MenuBook
        "translate", "auto_stories" -> Icons.Default.Translate
        "eco" -> Icons.Default.Eco
        else -> Icons.Default.AutoStories
    }
}
