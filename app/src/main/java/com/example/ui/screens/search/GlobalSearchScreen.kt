package com.example.ui.screens.search

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainViewModel
import com.example.ui.theme.ScholarGold
import com.example.ui.theme.ScholarPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalSearchScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit
) {
    val query by viewModel.searchQuery.collectAsState()
    val results by viewModel.searchResults.collectAsState()

    val suggestions = listOf("प्रकाश संश्लेषण", "Trigonometry", "रासायनिक अभिक्रिया", "Ohm's Law", "वास्तविक संख्याएं", "pH Scale")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { viewModel.onSearchQueryChanged(it) },
                        placeholder = { Text("पाठ, प्रश्न, सूत्र या नोट्स खोजें...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("global_search_input"),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Suggestion chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(suggestions) { s ->
                    AssistChip(
                        onClick = { viewModel.onSearchQueryChanged(s) },
                        label = { Text(s, fontSize = 12.sp) }
                    )
                }
            }

            if (query.isBlank()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(54.dp), tint = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "पूरे डिजिटल टेक्स्टबुक में तुरंत खोजें",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "उदाहरण: 'दर्पण सूत्र', 'अम्ल एवं क्षार', 'Coulomb'",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Chapters
                    if (results.chapters.isNotEmpty()) {
                        item {
                            Text("📚 पाठ्यपुस्तक अध्याय (${results.chapters.size})", fontWeight = FontWeight.Bold, color = ScholarPrimary)
                        }
                        items(results.chapters) { ch ->
                            SearchCard(
                                title = ch.titleHindi,
                                subtitle = "${ch.titleEnglish} • अध्याय ${ch.chapterNumber}",
                                icon = Icons.Default.AutoStories,
                                onClick = { onNavigate("textbook_reader/${ch.id}/${ch.titleHindi}") }
                            )
                        }
                    }

                    // Notes
                    if (results.notes.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("📝 नोट्स (${results.notes.size})", fontWeight = FontWeight.Bold, color = Color(0xFF00897B))
                        }
                        items(results.notes) { note ->
                            SearchCard(
                                title = note.title,
                                subtitle = note.content.take(60) + "...",
                                icon = Icons.Default.HistoryEdu,
                                onClick = { onNavigate("notes/${note.chapterId}") }
                            )
                        }
                    }

                    // Formulas
                    if (results.formulas.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("📐 सूत्र एवं नियम (${results.formulas.size})", fontWeight = FontWeight.Bold, color = ScholarGold)
                        }
                        items(results.formulas) { f ->
                            SearchCard(
                                title = f.formulaTitle,
                                subtitle = "${f.formulaMath} • ${f.topic}",
                                icon = Icons.Default.Functions,
                                onClick = { onNavigate("formulas") }
                            )
                        }
                    }

                    // Questions
                    if (results.questions.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("❓ प्रश्न बैंक (${results.questions.size})", fontWeight = FontWeight.Bold, color = Color(0xFFD81B60))
                        }
                        items(results.questions) { q ->
                            SearchCard(
                                title = q.questionText,
                                subtitle = "प्रकार: ${q.questionType} • सही उत्तर: ${q.correctAnswer}",
                                icon = Icons.Default.QuestionAnswer,
                                onClick = { onNavigate("question_bank/${q.chapterId}") }
                            )
                        }
                    }

                    if (results.chapters.isEmpty() && results.notes.isEmpty() && results.formulas.isEmpty() && results.questions.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                                Text("कोई परिणाम नहीं मिला। कृपया दूसरा शब्द खोजें।")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = ScholarPrimary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
