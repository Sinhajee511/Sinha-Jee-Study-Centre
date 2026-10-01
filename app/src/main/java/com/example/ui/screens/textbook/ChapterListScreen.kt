package com.example.ui.screens.textbook

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileDownloadDone
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainViewModel
import com.example.data.local.entity.ChapterEntity
import com.example.ui.components.SinhaTopAppBar
import com.example.ui.theme.ScholarEmerald
import com.example.ui.theme.ScholarGold
import com.example.ui.theme.ScholarPrimary

@Composable
fun ChapterListScreen(
    subjectId: String,
    subjectTitle: String,
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit
) {
    val chaptersFlow = rememberChapterFlow(viewModel, subjectId)
    val chapters by chaptersFlow.collectAsState(initial = emptyList())
    val user by viewModel.currentUser.collectAsState()

    Scaffold(
        topBar = {
            SinhaTopAppBar(
                title = subjectTitle,
                subtitle = "अध्याय सूची (All Chapters)",
                showBackButton = true,
                onBackClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (chapters.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = ScholarPrimary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "इस विषय के डिजिटल अध्याय तैयार हो रहे हैं",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "सिन्हा जी स्टडी सेंटर की विशेषज्ञ टीम द्वारा बिहार बोर्ड पाठ्यक्रम के अनुसार सामग्री तैयार की जा रही है।",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = onBack,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("अन्य विषय देखें (Back to Subjects)")
                            }
                        }
                    }
                }
            } else {
                items(chapters) { chapter ->
                    ChapterCard(
                        chapter = chapter,
                        onReadClick = { onNavigate("textbook_reader/${chapter.id}/$subjectTitle") },
                        onNotesClick = { onNavigate("notes/${chapter.id}") },
                        onQuestionsClick = { onNavigate("question_bank/${chapter.id}") },
                        onQuizClick = { onNavigate("quiz/$subjectId/${chapter.id}") },
                        onToggleDownload = { viewModel.toggleDownload(chapter.id, chapter.isDownloaded) }
                    )
                }
            }

            if (user?.isAdmin == true) {
                item {
                    Button(
                        onClick = { onNavigate("admin_panel") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ScholarGold)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("इस विषय में अध्याय जोड़ें (Admin Add Chapter)")
                    }
                }
            }
        }
    }
}

@Composable
private fun ChapterCard(
    chapter: ChapterEntity,
    onReadClick: () -> Unit,
    onNotesClick: () -> Unit,
    onQuestionsClick: () -> Unit,
    onQuizClick: () -> Unit,
    onToggleDownload: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onReadClick() }
            .testTag("chapter_card_${chapter.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ScholarPrimary.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "अध्याय ${chapter.chapterNumber}",
                        style = MaterialTheme.typography.labelSmall,
                        color = ScholarPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleDownload,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (chapter.isDownloaded) Icons.Default.FileDownloadDone else Icons.Default.FileDownload,
                            contentDescription = "Download",
                            tint = if (chapter.isDownloaded) ScholarEmerald else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = chapter.titleHindi,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = chapter.titleEnglish,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Progress bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LinearProgressIndicator(
                    progress = { chapter.readingProgressPercent / 100f },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (chapter.readingProgressPercent >= 100) ScholarEmerald else ScholarPrimary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "${chapter.readingProgressPercent}%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = onReadClick,
                    label = { Text("📖 पढ़ें", fontSize = 12.sp) }
                )
                AssistChip(
                    onClick = onNotesClick,
                    label = { Text("📝 नोट्स", fontSize = 12.sp) }
                )
                AssistChip(
                    onClick = onQuestionsClick,
                    label = { Text("❓ प्रश्न", fontSize = 12.sp) }
                )
                AssistChip(
                    onClick = onQuizClick,
                    label = { Text("🎯 क्विज़", fontSize = 12.sp) }
                )
            }
        }
    }
}

@Composable
private fun rememberChapterFlow(viewModel: MainViewModel, subjectId: String) =
    androidx.compose.runtime.remember(subjectId) {
        viewModel.repository.getChaptersBySubject(subjectId)
    }
