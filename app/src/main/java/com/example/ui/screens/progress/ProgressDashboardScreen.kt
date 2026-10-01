package com.example.ui.screens.progress

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.QueryBuilder
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainViewModel
import com.example.ui.components.ProgressCard
import com.example.ui.components.SinhaBottomNavigation
import com.example.ui.components.SinhaTopAppBar
import com.example.ui.components.StatBadge
import com.example.ui.theme.ScholarEmerald
import com.example.ui.theme.ScholarGold
import com.example.ui.theme.ScholarPrimary

@Composable
fun ProgressDashboardScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val quizAttempts by viewModel.quizAttempts.collectAsState()

    Scaffold(
        topBar = {
            SinhaTopAppBar(
                title = "मेरी प्रगति रिपोर्ट (My Progress)",
                subtitle = "अध्ययन विश्लेषण एवं परीक्षा स्कोर"
            )
        },
        bottomBar = {
            SinhaBottomNavigation(currentRoute = "progress", onNavigate = onNavigate)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Overall Score Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "कुल औसत प्रदर्शन (Overall Accuracy)",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "84%",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Black,
                            color = ScholarPrimary
                        )
                        Text(
                            text = "बहुत बढ़िया! आपकी बोर्ड परीक्षा की तैयारी सही दिशा में है।",
                            style = MaterialTheme.typography.bodySmall,
                            color = ScholarEmerald,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Grid stats
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatBox(label = "क्विज़ प्रयास", value = "${quizAttempts.size.coerceAtLeast(4)} टेस्ट", icon = Icons.Default.Quiz)
                            StatBox(label = "हल प्रश्न", value = "120 MCQs", icon = Icons.Default.Equalizer)
                            StatBox(label = "अध्ययन समय", value = "${user?.totalStudyMinutes ?: 240} मिनट", icon = Icons.Default.QueryBuilder)
                            StatBox(label = "सटीकता", value = "88%", icon = Icons.Default.AutoAwesome)
                        }
                    }
                }
            }

            // Subject-wise Performance Bars
            item {
                Text(
                    text = "विषयवार प्रगति (Subject Performance)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SubjectProgressRow(name = "विज्ञान (Science)", progress = 84, color = ScholarPrimary)
                    SubjectProgressRow(name = "गणित (Mathematics)", progress = 78, color = ScholarGold)
                    SubjectProgressRow(name = "हिंदी (Hindi)", progress = 91, color = ScholarEmerald)
                    SubjectProgressRow(name = "अंग्रेजी (English)", progress = 86, color = Color(0xFF8E24AA))
                    SubjectProgressRow(name = "सामाजिक विज्ञान (Social Science)", progress = 75, color = Color(0xFF00897B))
                }
            }

            // Smart Revision Recommendations
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = ScholarGold.copy(alpha = 0.12f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ScholarGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "स्मार्ट रिवीजन सुझाव (Smart Revision)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• त्रिकोणमिति में 3 प्रश्न गलत हुए थे — 'सर्वसमिकाएं' दोबारा पढ़ें।\n• प्रकाश परावर्तन के दर्पण सूत्र का अभ्यास आवश्यक है।\n• हिंदी में व्याकरण अनुभाग में 100% अंक प्राप्त हुए! शाबाश!",
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // Achievements Badges
            item {
                Text(
                    text = "उपलब्धियां (Achievements)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AchievementBadge(title = "First Quiz", desc = "प्रथम टेस्ट पूर्ण", icon = Icons.Default.EmojiEvents, isUnlocked = true, modifier = Modifier.weight(1f))
                    AchievementBadge(title = "10 Chapters", desc = "10 अध्याय पूर्ण", icon = Icons.Default.MenuBook, isUnlocked = true, modifier = Modifier.weight(1f))
                    AchievementBadge(title = "7-Day Streak", desc = "7 दिन निरंतरता", icon = Icons.Default.LocalFireDepartment, isUnlocked = true, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun StatBox(label: String, value: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = null, tint = ScholarPrimary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun SubjectProgressRow(name: String, progress: Int, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(text = "$progress%", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = color)
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = color
            )
        }
    }
}

@Composable
private fun AchievementBadge(
    title: String,
    desc: String,
    icon: ImageVector,
    isUnlocked: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) ScholarGold.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isUnlocked) ScholarGold else Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
            Text(text = desc, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        }
    }
}
