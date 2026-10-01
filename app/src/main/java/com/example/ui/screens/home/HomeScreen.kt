package com.example.ui.screens.home

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainViewModel
import com.example.R
import com.example.data.local.entity.ChapterEntity
import com.example.ui.components.SinhaBottomNavigation
import com.example.ui.components.StatBadge
import com.example.ui.theme.ScholarEmerald
import com.example.ui.theme.ScholarGold
import com.example.ui.theme.ScholarPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val recentlyRead by viewModel.recentlyRead.collectAsState()
    val subjects by viewModel.subjects.collectAsState()

    var target1 by remember { mutableStateOf(true) }
    var target2 by remember { mutableStateOf(false) }
    var target3 by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(ScholarPrimary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoStories,
                                contentDescription = null,
                                tint = ScholarPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "SINHA JEE STUDY CENTRE",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp,
                                color = ScholarPrimary
                            )
                            Text(
                                text = "Digital Textbook • बिहार बोर्ड",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onNavigate("search") },
                        modifier = Modifier.testTag("home_search_button")
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(
                        onClick = { onNavigate("notifications") },
                        modifier = Modifier.testTag("home_notifications_button")
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            SinhaBottomNavigation(currentRoute = "home", onNavigate = onNavigate)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigate("search") },
                containerColor = ScholarPrimary,
                contentColor = Color.White,
                modifier = Modifier.testTag("floating_search_button")
            ) {
                Icon(Icons.Default.Search, contentDescription = "Global Search")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Welcome Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                    )
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Welcome to SINHA JEE STUDY CENTRE",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = ScholarPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "नमस्ते, ${user?.name ?: "विद्यार्थी"}! 👋",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = ScholarPrimary
                            ) {
                                Text(
                                    text = "कक्षा ${user?.studentClass ?: "10"}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Banner image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(RoundedCornerShape(14.dp))
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.study_banner),
                                contentDescription = "Study Banner",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Stats Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatBadge(
                                icon = Icons.Default.TrendingUp,
                                label = "Streak",
                                value = "${user?.studyStreakDays ?: 3} Days 🔥",
                                iconTint = ScholarGold,
                                modifier = Modifier.weight(1f)
                            )
                            StatBadge(
                                icon = Icons.Default.CheckCircle,
                                label = "Completed",
                                value = "2 Chapters 📚",
                                iconTint = ScholarEmerald,
                                modifier = Modifier.weight(1f)
                            )
                            StatBadge(
                                icon = Icons.Default.Equalizer,
                                label = "Score",
                                value = "82% ⭐",
                                iconTint = ScholarPrimary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Continue Reading Card
            item {
                val lastChapter = recentlyRead.firstOrNull()
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (lastChapter != null) {
                                onNavigate("textbook_reader/${lastChapter.id}/${lastChapter.titleHindi}")
                            } else {
                                onNavigate("class_selection")
                            }
                        }
                        .testTag("continue_reading_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(ScholarPrimary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = ScholarPrimary
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Continue Reading (जहाँ छोड़ा था)",
                                style = MaterialTheme.typography.labelSmall,
                                color = ScholarPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = lastChapter?.titleHindi ?: "विज्ञान • अध्याय 1 : रासायनिक अभिक्रियाएं",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "अनुभाग: ${lastChapter?.lastReadSection ?: "1.4 रासायनिक अभिक्रियाओं के प्रकार"} • पृष्ठ ${lastChapter?.lastReadPage ?: 8}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { (lastChapter?.readingProgressPercent ?: 65) / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = ScholarPrimary
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Open",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Main 8 Cards Grid
            item {
                val activeChapterId = recentlyRead.firstOrNull()?.id ?: if (user?.studentClass == "12") "c12_phy_ch1" else "c10_sci_ch1"
                Text(
                    text = "अध्ययन सामग्री (Study Tools)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        MainFeatureCard(
                            title = "My Textbooks",
                            subtitleHi = "मेरी पाठ्यपुस्तकें",
                            icon = Icons.Default.AutoStories,
                            color = ScholarPrimary,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("nav_textbooks"),
                            onClick = { onNavigate("class_selection") }
                        )
                        MainFeatureCard(
                            title = "Notes",
                            subtitleHi = "हस्तलिखित नोट्स",
                            icon = Icons.Default.HistoryEdu,
                            color = Color(0xFF00897B),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("nav_notes"),
                            onClick = { onNavigate("notes/$activeChapterId") }
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        MainFeatureCard(
                            title = "Practice Questions",
                            subtitleHi = "प्रश्न बैंक & हल",
                            icon = Icons.Default.Equalizer,
                            color = Color(0xFFE65100),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("nav_questions"),
                            onClick = { onNavigate("question_bank/$activeChapterId") }
                        )
                        MainFeatureCard(
                            title = "MCQ Quiz",
                            subtitleHi = "अध्यायवार क्विज़",
                            icon = Icons.Default.Quiz,
                            color = Color(0xFFD81B60),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("nav_quiz"),
                            onClick = { onNavigate("quiz/all/all") }
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        MainFeatureCard(
                            title = "My Progress",
                            subtitleHi = "प्रगति और रिपोर्ट",
                            icon = Icons.Default.TrendingUp,
                            color = Color(0xFF3949AB),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("nav_progress"),
                            onClick = { onNavigate("progress") }
                        )
                        MainFeatureCard(
                            title = "Bookmarks",
                            subtitleHi = "सहेजे गए पाठ",
                            icon = Icons.Default.Bookmark,
                            color = ScholarGold,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("nav_bookmarks"),
                            onClick = { onNavigate("bookmarks") }
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        MainFeatureCard(
                            title = "Downloads",
                            subtitleHi = "ऑफलाइन सामग्री",
                            icon = Icons.Default.Download,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("nav_downloads"),
                            onClick = { onNavigate("downloads") }
                        )
                        MainFeatureCard(
                            title = "Formulas & PYQ",
                            subtitleHi = "फॉर्मूला व गत वर्ष",
                            icon = Icons.Default.Functions,
                            color = Color(0xFF6A1B9A),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("nav_formulas"),
                            onClick = { onNavigate("formulas") }
                        )
                    }
                }
            }

            // Today's Study Target
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Today's Study (आज का अध्ययन लक्ष्य)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "3 में से 1 पूर्ण",
                                style = MaterialTheme.typography.labelSmall,
                                color = ScholarPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        DailyTargetItem(
                            title = "1 अध्याय पूरा पढ़ें (Read 1 Chapter)",
                            subtitle = "विज्ञान • रासायनिक अभिक्रियाएं",
                            isChecked = target1,
                            onToggle = { target1 = it }
                        )
                        DailyTargetItem(
                            title = "20 MCQs का अभ्यास करें (Practice 20 MCQs)",
                            subtitle = "लक्ष्य: 85% सटीकता",
                            isChecked = target2,
                            onToggle = { target2 = it }
                        )
                        DailyTargetItem(
                            title = "1 मॉक क्विज़ पूरा करें (Complete 1 Quiz)",
                            subtitle = "समय: 15 मिनट",
                            isChecked = target3,
                            onToggle = { target3 = it }
                        )
                    }
                }
            }

            // Recommended Chapters Carousel
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "अनुशंसित अध्याय (Recommended)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "सभी देखें →",
                        style = MaterialTheme.typography.labelMedium,
                        color = ScholarPrimary,
                        modifier = Modifier.clickable { onNavigate("class_selection") }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(listOf(
                        Triple("अम्ल, क्षारक एवं लवण", "विज्ञान • अध्याय 2", "c10_sci_ch2"),
                        Triple("जैव प्रक्रम (Life Processes)", "विज्ञान • अध्याय 6", "c10_sci_ch6"),
                        Triple("वास्तविक संख्याएं", "गणित • अध्याय 1", "c10_math_ch1"),
                        Triple("विद्युत आवेश तथा क्षेत्र", "भौतिकी • कक्षा 12", "c12_phy_ch1")
                    )) { (title, subtitle, chapId) ->
                        Card(
                            modifier = Modifier
                                .width(220.dp)
                                .clickable { onNavigate("textbook_reader/$chapId/$title") },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = subtitle,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ScholarPrimary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "पढ़ना शुरू करें →",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = ScholarPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Quick Revision Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate("formulas") },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ScholarGold.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Functions,
                            contentDescription = null,
                            tint = ScholarGold,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "स्मार्ट फॉर्मूला रिवीजन (Quick Revision)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "गणित व विज्ञान के सभी महत्वपूर्ण सूत्र एवं नियम एक जगह",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MainFeatureCard(
    title: String,
    subtitleHi: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitleHi,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DailyTargetItem(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(!isChecked) }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onToggle(it) }
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isChecked) FontWeight.Normal else FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
