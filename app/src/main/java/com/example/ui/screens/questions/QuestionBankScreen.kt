package com.example.ui.screens.questions

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.MainViewModel
import com.example.data.local.entity.QuestionEntity
import com.example.ui.components.SinhaTopAppBar
import com.example.ui.theme.ScholarEmerald
import com.example.ui.theme.ScholarGold
import com.example.ui.theme.ScholarPrimary

@Composable
fun QuestionBankScreen(
    chapterId: String,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val studentClass = user?.studentClass ?: "10"

    val chapterQuestionsFlow = remember(chapterId) {
        if (chapterId.isNotBlank() && chapterId != "all") {
            viewModel.repository.getQuestionsByChapter(chapterId)
        } else {
            viewModel.repository.getQuestionsByClassFlow(studentClass)
        }
    }
    val chapterQuestions by chapterQuestionsFlow.collectAsState(initial = emptyList())
    val allQuestions by viewModel.repository.getAllQuestionsFlow().collectAsState(initial = emptyList())

    var selectedFilter by remember { mutableStateOf("ALL") }
    var searchQuery by remember { mutableStateOf("") }

    val baseQuestions = when {
        chapterQuestions.isNotEmpty() -> chapterQuestions
        allQuestions.isNotEmpty() -> allQuestions
        else -> emptyList()
    }

    val typeFiltered = if (selectedFilter == "ALL") {
        baseQuestions
    } else {
        baseQuestions.filter { it.questionType == selectedFilter }
    }

    val finalQuestions = if (searchQuery.isBlank()) {
        typeFiltered
    } else {
        typeFiltered.filter {
            it.questionText.contains(searchQuery, ignoreCase = true) ||
            it.explanation.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            SinhaTopAppBar(
                title = "प्रश्न बैंक (Question Bank)",
                subtitle = "MCQ • लघु • दीर्घ • गत वर्ष प्रश्न",
                showBackButton = true,
                onBackClick = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Search Input inside question bank
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("प्रश्न खोजें (Search questions)...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("question_bank_search"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Category Filters
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "ALL" to "सभी",
                    "MCQ" to "MCQ वस्तुनिष्ठ",
                    "SHORT" to "लघु उत्तरीय",
                    "PYQ" to "बोर्ड प्रश्न"
                ).forEach { (type, label) ->
                    FilterChip(
                        selected = selectedFilter == type,
                        onClick = { selectedFilter = type },
                        label = { Text(label, fontSize = 12.sp) }
                    )
                }
            }

            if (finalQuestions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "इस श्रेणी में कोई प्रश्न नहीं मिला।",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(finalQuestions) { q ->
                        InteractiveQuestionCard(
                            question = q,
                            onBookmark = {
                                viewModel.toggleBookmark("question", q.id, q.questionText, q.questionType)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InteractiveQuestionCard(
    question: QuestionEntity,
    onBookmark: () -> Unit
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var showSubjectiveAnswer by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("question_item_${question.id}"),
        shape = RoundedCornerShape(16.dp),
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
                    color = when (question.questionType) {
                        "MCQ" -> ScholarPrimary.copy(alpha = 0.12f)
                        "PYQ" -> ScholarGold.copy(alpha = 0.15f)
                        else -> ScholarEmerald.copy(alpha = 0.12f)
                    }
                ) {
                    Text(
                        text = if (question.year != null) "${question.questionType} (${question.year})" else question.questionType,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (question.questionType == "PYQ") ScholarGold else ScholarPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                IconButton(onClick = onBookmark) {
                    Icon(
                        imageVector = if (question.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (question.isBookmarked) ScholarGold else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = question.questionText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            // If MCQ
            if (question.questionType == "MCQ" || (question.questionType == "PYQ" && question.optionA != null)) {
                Spacer(modifier = Modifier.height(12.dp))
                val options = listOfNotNull(
                    question.optionA?.let { "A" to it },
                    question.optionB?.let { "B" to it },
                    question.optionC?.let { "C" to it },
                    question.optionD?.let { "D" to it }
                )

                options.forEach { (key, optText) ->
                    val isSelected = selectedOption == key
                    val isCorrect = question.correctAnswer.equals(key, ignoreCase = true)

                    val bg = when {
                        isSubmitted && isCorrect -> ScholarEmerald.copy(alpha = 0.2f)
                        isSubmitted && isSelected && !isCorrect -> Color.Red.copy(alpha = 0.15f)
                        isSelected -> ScholarPrimary.copy(alpha = 0.15f)
                        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(bg)
                            .clickable(enabled = !isSubmitted) { selectedOption = key }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$key.",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(28.dp),
                            color = if (isSelected) ScholarPrimary else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = optText,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSubmitted && isCorrect) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = ScholarEmerald)
                        } else if (isSubmitted && isSelected && !isCorrect) {
                            Icon(Icons.Default.Cancel, contentDescription = "Wrong", tint = Color.Red)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (!isSubmitted) {
                    Button(
                        onClick = { isSubmitted = true },
                        enabled = selectedOption != null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ScholarPrimary)
                    ) {
                        Text("उत्तर जांचें (Check Answer)")
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                            .padding(12.dp)
                    ) {
                        Column {
                            val isCorrectAnswer = selectedOption.equals(question.correctAnswer, ignoreCase = true)
                            Text(
                                text = if (isCorrectAnswer) "शानदार! सही उत्तर: विकल्प ${question.correctAnswer}" else "गलत उत्तर! सही उत्तर विकल्प ${question.correctAnswer} है।",
                                fontWeight = FontWeight.Bold,
                                color = if (isCorrectAnswer) ScholarEmerald else Color.Red
                            )
                            if (question.explanation.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "व्याख्या: ${question.explanation}",
                                    style = MaterialTheme.typography.bodySmall,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            } else {
                // Short or Long Subjective question
                Spacer(modifier = Modifier.height(12.dp))

                if (!showSubjectiveAnswer) {
                    OutlinedButton(
                        onClick = { showSubjectiveAnswer = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("आदर्श उत्तर देखें (Show Model Answer)")
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "आदर्श उत्तर (Model Answer):",
                                fontWeight = FontWeight.Bold,
                                color = ScholarPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = question.correctAnswer,
                                style = MaterialTheme.typography.bodyMedium,
                                lineHeight = 20.sp
                            )
                            if (question.explanation.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "मुख्य बिंदु: ${question.explanation}",
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
}
